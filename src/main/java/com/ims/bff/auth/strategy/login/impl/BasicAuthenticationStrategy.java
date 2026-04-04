package com.ims.bff.auth.strategy.login.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ims.bff.auth.AuthConstants;
import com.ims.bff.auth.dto.AuthProviderResponse;
import com.ims.bff.auth.enums.AuthType;
import com.ims.bff.auth.properties.AuthProperties;
import com.ims.bff.auth.strategy.login.AuthenticationStrategy;

@Component
public class BasicAuthenticationStrategy implements AuthenticationStrategy {

    private static final String BASIC_DISPLAY_NAME = "Basic Login";

    private final AuthProperties authProperties;

    public BasicAuthenticationStrategy(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    @Override
    public AuthType supportedType() {
        return AuthType.BASIC;
    }

    @Override
    public List<AuthProviderResponse> getSupportedProviders() {
        if (!authProperties.getBasic().isEnabled()) {
            return List.of();
        }

        return List.of(new AuthProviderResponse(
                AuthType.BASIC,
                AuthConstants.PROVIDER_BASIC,
                BASIC_DISPLAY_NAME,
                AuthConstants.AUTH_BASIC_LOGIN_PATH));
    }

    @Override
    public String resolveLoginPath(String provider) {
        if (!AuthConstants.PROVIDER_BASIC.equalsIgnoreCase(provider) || !authProperties.getBasic().isEnabled()) {
            throw new IllegalArgumentException("Unsupported basic authentication provider: " + provider);
        }
        return AuthConstants.AUTH_BASIC_LOGIN_PATH;
    }
}
