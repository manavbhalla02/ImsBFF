package com.ims.bff.auth.strategy.result.impl;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.ims.bff.auth.dto.AuthenticationResponse;
public abstract class AbstractExternalAuthenticationResultStrategy extends AbstractAuthenticationResultStrategy {

    @Override
    public AuthenticationResponse handleCallback(Authentication authentication) {
        return buildResponse(
                supportedType(),
                supportedProvider(),
                resolveUsername(authentication),
                extractAuthorities(authentication),
                successMessage());
    }

    @Override
    public AuthenticationResponse resolveAuthenticatedUser(Authentication authentication) {
        return buildResponse(
                supportedType(),
                supportedProvider(),
                resolveUsername(authentication),
                extractAuthorities(authentication),
                currentUserMessage());
    }

    protected String resolveUsername(Authentication authentication) {
        if (authentication.getPrincipal() instanceof OAuth2User oauth2User) {
            return resolveUsername(oauth2User.getAttributes(), authentication.getName());
        }

        return authentication.getName();
    }

    protected abstract String successMessage();

    protected abstract String currentUserMessage();

    protected abstract String resolveUsername(Map<String, Object> attributes, String fallbackUsername);

    protected String firstNonBlank(String fallbackValue, Object... candidates) {
        for (Object candidate : candidates) {
            if (candidate instanceof String value && !value.isBlank()) {
                return value;
            }
        }
        return fallbackValue;
    }

    protected String joinNames(
            String fallbackValue,
            Object firstName,
            Object lastName,
            Object alternateName) {
        String resolvedFirstName = firstName instanceof String value ? value.strip() : "";
        String resolvedLastName = lastName instanceof String value ? value.strip() : "";
        String fullName = (resolvedFirstName + " " + resolvedLastName).trim();
        if (!fullName.isBlank()) {
            return fullName;
        }
        return firstNonBlank(fallbackValue, alternateName);
    }
}
