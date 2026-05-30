package com.arenic.backend.config.security;

import com.arenic.backend.modules.club.internal.model.enums.ClubMembershipRole;
import com.arenic.backend.modules.identity.internal.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class CustomUserDetails implements UserDetails {
    private final User user;
    private final Map<UUID, List<ClubMembershipRole>> clubRoles;

    public CustomUserDetails(User user, Map<UUID, List<ClubMembershipRole>> clubRoles) {
        this.user = user;
        this.clubRoles = clubRoles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getName();
    }
}
