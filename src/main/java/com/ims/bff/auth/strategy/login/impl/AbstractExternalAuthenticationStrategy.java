package com.ims.bff.auth.strategy.login.impl;

import java.util.List;

import org.springframework.util.StringUtils;

import com.ims.bff.auth.AuthConstants;
import com.ims.bff.auth.dto.AuthProviderResponse;
import com.ims.bff.auth.properties.AuthProperties.ExternalProvider;
import com.ims.bff.auth.strategy.login.ExternalAuthenticationStrategy;

public abstract class AbstractExternalAuthenticationStrategy implements ExternalAuthenticationStrategy {

    @Override
    public List<AuthProviderResponse> getSupportedProviders() {
        return configuredProviders().stream()
                .filter(ExternalProvider::isEnabled)
                .filter(provider -> StringUtils.hasText(provider.getKey()))
                .map(provider -> new AuthProviderResponse(
                        supportedType(),
                        provider.getKey(),
                        resolveDisplayName(provider),
                        resolveLoginPath(provider.getKey())))
                .toList();
    }

    @Override
    public String resolveLoginPath(String provider) {
        boolean supported = configuredProviders().stream()
                .filter(ExternalProvider::isEnabled)
                .map(ExternalProvider::getKey)
                .anyMatch(configuredProvider -> configuredProvider.equalsIgnoreCase(provider));

        if (!supported) {
            throw new IllegalArgumentException("Unsupported " + supportedType() + " provider: " + provider);
        }

        return AuthConstants.OAUTH2_AUTHORIZATION_BASE_URI + "/" + provider;
    }

    private String resolveDisplayName(ExternalProvider provider) {
        if (StringUtils.hasText(provider.getDisplayName())) {
            return provider.getDisplayName();
        }
        return provider.getKey();
    }
}
