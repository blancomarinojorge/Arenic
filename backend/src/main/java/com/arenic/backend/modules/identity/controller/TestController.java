package com.arenic.backend.modules.identity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TestController {

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication){
        Jwt jwt = (Jwt) authentication.getPrincipal();

        String email = jwt.getClaimAsString("email");
        String userId = jwt.getClaimAsString("sub");

        return ResponseEntity.ok("Email " + email + "User: " + userId);
    }
}
