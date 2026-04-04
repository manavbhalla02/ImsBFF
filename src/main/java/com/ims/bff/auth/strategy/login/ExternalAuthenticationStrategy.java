package com.ims.bff.auth.strategy.login;

import java.util.List;

import com.ims.bff.auth.dto.AuthProviderResponse;
import com.ims.bff.auth.enums.AuthType;
import com.ims.bff.auth.properties.AuthProperties.ExternalProvider;

public interface ExternalAuthenticationStrategy extends AuthenticationStrategy {

    List<ExternalProvider> configuredProviders();

    @Override
    AuthType supportedType();

    @Override
    List<AuthProviderResponse> getSupportedProviders();

    @Override
    String resolveLoginPath(String provider);
}
