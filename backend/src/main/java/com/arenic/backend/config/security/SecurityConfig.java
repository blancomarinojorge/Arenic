package com.arenic.backend.config.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import io.jsonwebtoken.security.Jwk;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    RsaKeyProvider rsaKeyProvider;

    public SecurityConfig(RsaKeyProvider rsaKeyProvider){
        this.rsaKeyProvider = rsaKeyProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                // 1. Disable csrf since we don't need it for a stateless authentication, there`s no session in the browser anyone can use.
                .csrf(AbstractHttpConfigurer::disable)
                // 2.
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 3. We make de session creation policy stateless
                /*
                * - What it means: This tells Spring Security never to create an HttpSession on the server, and never to store or look for security context data inside a server-side session cookie.
                * - Why it matters: This enforces strict REST principles. Every single incoming HTTP request is treated as completely independent. The server remembers absolutely nothing about the
                *   client between requests. The responsibility of proving identity shifts entirely to the client, who must provide a valid token with every individual submission.
                * */
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 4. Configuring general url authentication security
                .authorizeHttpRequests(auth -> auth
                        // Anyone can access the auth endpoints
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**").permitAll()
                        // Public info
                        .requestMatchers("/clubs/**").permitAll()
                        // Any other request must be authenticated
                        .anyRequest().authenticated()
                )

                /*
                * What it means: This tells your application to act as an OAuth2 Resource Server that protects its endpoints using JSON Web Tokens (JWTs).
                *
                * - How it operates behind the scenes: When a request hits a protected endpoint, Spring activates a specialized filter called the BearerTokenAuthenticationFilter.
                *   1 It reads the incoming request headers looking for an Authorization: Bearer <TOKEN> string.
                *   2 If found, it extracts the token string and hands it directly to your custom jwtDecoder() bean.
                *   3 The decoder verifies the cryptographic signature of the JWT using your public RSA key and checks that the expiration timestamp hasn't passed.
                *   4 If validation succeeds, Spring extracts the user's details and claims from the token payload, creates an Authentication context token, and securely allows the request to proceed to your Controller.
                *
                *   Request with Authorization: Bearer <token>
                *           │
                *           ▼
                *   BearerTokenAuthenticationFilter
                *           │
                *           ├─ jwtDecoder().decode(token)
                *           │     ├─ Verifies RSA signature
                *           │     └─ Checks expiry claim
                *           │
                *           ▼
                *   JwtAuthenticationConverter  (Spring's default)
                *           │
                *           ├─ Reads claims directly from the token payload
                *           │     ├─ sub  → principal name
                *           │     └─ scope/scp → granted authorities
                *           │
                *           ▼
                *   JwtAuthenticationToken stored in SecurityContextHolder
                *           │
                *           NO database call at all
                *
                * */
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                            /* Use the created decoder for verifying the request header JWT */
                            .decoder(jwtDecoder())

                            /* I don't need to customize this as the default is enough for our case */
                            //.jwtAuthenticationConverter()
                        )
                );

        return http.build();
    }


    /*

            [ Your Login Controller ]
                   │
                   ▼  (Hands over raw email & password)
        ┌─────────────────────────────────┐
        │     AuthenticationManager       │  <── The Bean you configured above
        └─────────────────────────────────┘
                   │
                   ▼  (Delegates verification to...)
        ┌─────────────────────────────────┐
        │   DaoAuthenticationProvider     │  <── The internal Spring provider
        └─────────────────────────────────┘
                   │
                   ├─► 1. Calls your CustomUserDetailsService.loadUserByUsername(email)
                   │      └─► Returns the DB User (containing the encrypted password hash)
                   │
                   └─► 2. Calls your PasswordEncoder.matches(rawPassword, encodedPassword)
                          └─► Checks if the typed password matches the DB hash

      -------------------

      This is used in @PostMapping("/login") :

      authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.email(), request.password())
      );

      - What happens: You wrap the raw input (email and password) into an unauthenticated packaging container called a UsernamePasswordAuthenticationToken.
      - The Spring Leap: You hand this package to the AuthenticationManager bean. This bean passes it to the DaoAuthenticationProvider you defined, which
        calls your CustomUserDetailsService.loadUserByUsername(), fetches the database user, and checks the password hash via your PasswordEncoder.
      - The Magic Safety Net: If the passwords do not match, or if the user doesn't exist, this line instantly throws a BadCredentialsException or
        UsernameNotFoundException. Spring catches it, halts execution immediately, and sends an HTTP 401 Unauthorized back to the browser. Your code below this line will never run if the login fails.

    * */
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(this.rsaKeyProvider.getPublicKey()).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(this.rsaKeyProvider.getPublicKey())
                .privateKey(this.rsaKeyProvider.getPrivateKey())
                .build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Allow Angular frontend origin explicitly
        configuration.setAllowedOrigins(List.of(
                "http://localhost:4200",
                "https://arenic.online",
                "https://www.arenic.online"
        ));

        // Allow standard HTTP methods
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Allow all headers (browser preflight can send arbitrary request headers)
        configuration.setAllowedHeaders(List.of("*"));

        // Allow browser credentials if you plan to use HTTP-only cookies later
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Apply this configuration to every single endpoint in the application
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
