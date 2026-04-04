package com.ims.bff.auth.strategy.login.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ims.bff.auth.enums.AuthType;
import com.ims.bff.auth.properties.AuthProperties;
import com.ims.bff.auth.properties.AuthProperties.ExternalProvider;

@Component
public class SsoAuthenticationStrategy extends AbstractExternalAuthenticationStrategy {

    private final AuthProperties authProperties;

    public SsoAuthenticationStrategy(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    @Override
    public List<ExternalProvider> configuredProviders() {
        return authProperties.getSsoProviders();
    }

    @Override
    public AuthType supportedType() {
        return AuthType.SSO;
    }
}
