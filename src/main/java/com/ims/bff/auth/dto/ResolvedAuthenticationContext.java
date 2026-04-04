package com.ims.bff.auth.dto;

import com.ims.bff.auth.enums.AuthType;

public record ResolvedAuthenticationContext(
        AuthType authType,
        String provider) {
}
