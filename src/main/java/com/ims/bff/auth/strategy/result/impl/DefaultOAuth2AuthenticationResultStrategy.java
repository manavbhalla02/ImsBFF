package com.ims.bff.auth.strategy.result.impl;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.ims.bff.auth.AuthConstants;
import com.ims.bff.auth.enums.AuthType;

@Component
public class DefaultOAuth2AuthenticationResultStrategy extends AbstractExternalAuthenticationResultStrategy {

    @Override
    public AuthType supportedType() {
        return AuthType.OAUTH2;
    }

    @Override
    public String supportedProvider() {
        return AuthConstants.DEFAULT_PROVIDER_KEY;
    }

    @Override
    protected String successMessage() {
        return "OAuth2 callback handled successfully";
    }

    @Override
    protected String currentUserMessage() {
        return "OAuth2 user resolved successfully";
    }

    @Override
    protected String resolveUsername(Map<String, Object> attributes, String fallbackUsername) {
        return firstNonBlank(
                fallbackUsername,
                attributes.get("email"),
                attributes.get("preferred_username"),
                attributes.get("name"),
                attributes.get("sub"),
                attributes.get("id"));
    }
}
