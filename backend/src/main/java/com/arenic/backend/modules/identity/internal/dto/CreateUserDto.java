package com.arenic.backend.modules.identity.internal.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateUserDto(
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    String email,

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    String password,

    @NotBlank(message = "Name is required")
    String name,

    @NotBlank(message = "First surname is required")
    String surname1,

    // Optional fields don't need @NotBlank
    String surname2
) {}