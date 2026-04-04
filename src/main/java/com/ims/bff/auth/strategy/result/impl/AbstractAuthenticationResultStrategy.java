package com.ims.bff.auth.strategy.result.impl;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.ims.bff.auth.dto.AuthenticationResponse;
import com.ims.bff.auth.enums.AuthType;
import com.ims.bff.auth.strategy.result.AuthenticationResultStrategy;

public abstract class AbstractAuthenticationResultStrategy implements AuthenticationResultStrategy {

    protected AuthenticationResponse buildResponse(
            AuthType authType,
            String provider,
            String username,
            List<String> authorities,
            String message) {
        return new AuthenticationResponse(authType.name(), provider, username, authorities, message);
    }

    protected List<String> extractAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .toList();
    }
}
