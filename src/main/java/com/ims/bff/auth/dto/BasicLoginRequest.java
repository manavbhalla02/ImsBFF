package com.ims.bff.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record BasicLoginRequest(
        @NotBlank String username,
        @NotBlank String password) {
}
