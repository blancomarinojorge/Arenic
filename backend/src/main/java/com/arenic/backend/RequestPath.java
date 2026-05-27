package com.arenic.backend;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RequestPath {
    @GetMapping("/public")
    public String getPublicPage(){
        return "Hola!";
    }

    @GetMapping("/secured")
    public Map<String, Object> getSecured(@AuthenticationPrincipal OAuth2User principal){
        return principal.getAttributes();
    }
}
