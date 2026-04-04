package com.ims.bff.auth.dto;

import com.ims.bff.auth.enums.AuthType;

public record AuthProviderResponse(
        AuthType type,
        String provider,
        String displayName,
        String loginPath) {
}
