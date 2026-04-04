package com.ims.bff.auth.strategy.login.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ims.bff.auth.enums.AuthType;
import com.ims.bff.auth.properties.AuthProperties;
import com.ims.bff.auth.properties.AuthProperties.ExternalProvider;

@Component
public class OAuth2AuthenticationStrategy extends AbstractExternalAuthenticationStrategy {

    private final AuthProperties authProperties;

    public OAuth2AuthenticationStrategy(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    @Override
    public List<ExternalProvider> configuredProviders() {
        return authProperties.getOauthProviders();
    }

    @Override
    public AuthType supportedType() {
        return AuthType.OAUTH2;
    }
}
