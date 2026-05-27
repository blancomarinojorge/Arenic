package com.arenic.backend.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class RsaKeyProvider {

    private final RSAPublicKey publicKey;
    private final RSAPrivateKey privateKey;

    public RsaKeyProvider(
            @Value("${spring.security.jwt.private-key}") String privateKeyStr,
            @Value("${spring.security.jwt.public-key}") String publicKeyStr
    ) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            // Clean any unintended whitespace and decode Base64
            byte[] publicBytes = Base64.getDecoder().decode(publicKeyStr.replaceAll("\\s", ""));
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicBytes);
            this.publicKey = (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);

            byte[] privateBytes = Base64.getDecoder().decode(privateKeyStr.replaceAll("\\s", ""));
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateBytes);
            this.privateKey = (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);

        } catch (Exception e) {
            throw new IllegalStateException("Could not initialize RSA cryptographic keys", e);
        }
    }

    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }
}