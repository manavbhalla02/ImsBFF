package com.ims.bff.auth.dto;

import java.util.List;

public record AuthenticationResponse(
        String authenticationType,
        String provider,
        String username,
        List<String> authorities,
        String message) {
}
