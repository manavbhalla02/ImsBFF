package com.ims.bff.auth.strategy.result.impl;

import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;

import com.ims.bff.auth.AuthConstants;
import com.ims.bff.auth.dto.AuthenticationResponse;
import com.ims.bff.auth.enums.AuthType;

@Component
public class BasicAuthenticationResultStrategy extends AbstractAuthenticationResultStrategy {

    @Override
    public AuthType supportedType() {
        return AuthType.BASIC;
    }

    @Override
    public String supportedProvider() {
        return AuthConstants.PROVIDER_BASIC;
    }

    @Override
    public AuthenticationResponse handleCallback(Authentication authentication) {
        return buildResponse(
                AuthType.BASIC,
                AuthConstants.PROVIDER_BASIC,
                authentication.getName(),
                extractAuthorities(authentication),
                "Basic authentication handled successfully");
    }

    @Override
    public AuthenticationResponse resolveAuthenticatedUser(Authentication authentication) {
        return buildResponse(
                AuthType.BASIC,
                AuthConstants.PROVIDER_BASIC,
                authentication.getName(),
                extractAuthorities(authentication),
                "Basic user resolved successfully");
    }
}
