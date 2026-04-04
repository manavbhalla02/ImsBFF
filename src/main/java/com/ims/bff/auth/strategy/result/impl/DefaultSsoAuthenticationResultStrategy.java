package com.ims.bff.auth.strategy.result.impl;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.ims.bff.auth.AuthConstants;
import com.ims.bff.auth.enums.AuthType;

@Component
public class DefaultSsoAuthenticationResultStrategy extends AbstractExternalAuthenticationResultStrategy {

    @Override
    public AuthType supportedType() {
        return AuthType.SSO;
    }

    @Override
    public String supportedProvider() {
        return AuthConstants.DEFAULT_PROVIDER_KEY;
    }

    @Override
    protected String successMessage() {
        return "SSO callback handled successfully";
    }

    @Override
    protected String currentUserMessage() {
        return "SSO user resolved successfully";
    }

    @Override
    protected String resolveUsername(Map<String, Object> attributes, String fallbackUsername) {
        return firstNonBlank(
                fallbackUsername,
                attributes.get("preferred_username"),
                attributes.get("email"),
                attributes.get("name"),
                attributes.get("sub"),
                attributes.get("id"));
    }
}
