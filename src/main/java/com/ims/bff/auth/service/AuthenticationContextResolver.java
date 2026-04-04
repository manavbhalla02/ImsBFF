package com.ims.bff.auth.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

import com.ims.bff.auth.AuthConstants;
import com.ims.bff.auth.dto.ResolvedAuthenticationContext;
import com.ims.bff.auth.enums.AuthType;
import com.ims.bff.auth.properties.AuthProperties;

@Component
public class AuthenticationContextResolver {

    private final AuthProperties authProperties;

    public AuthenticationContextResolver(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    public ResolvedAuthenticationContext resolve(Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticationToken oauth2AuthenticationToken) {
            System.out.println("Resolving authentication context for OAuth2 token with authorities: " + oauth2AuthenticationToken.getAuthorities());
            System.out.println("Resolved provider: " + oauth2AuthenticationToken.getAuthorizedClientRegistrationId());
            String provider = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();
            return new ResolvedAuthenticationContext(resolveExternalType(provider), provider);
        }

        return new ResolvedAuthenticationContext(AuthType.BASIC, AuthConstants.PROVIDER_BASIC);
    }

    private AuthType resolveExternalType(String provider) {
        if (isConfiguredSsoProvider(provider)) {
            return AuthType.SSO;
        }

        if (isConfiguredOauthProvider(provider)) {
            return AuthType.OAUTH2;
        }

        if (AuthConstants.PROVIDER_OKTA.equalsIgnoreCase(provider)) {
            return AuthType.SSO;
        }

        return AuthType.OAUTH2;
    }

    private boolean isConfiguredOauthProvider(String provider) {
        return authProperties.getOauthProviders().stream()
                .map(AuthProperties.ExternalProvider::getKey)
                .filter(key -> key != null)
                .anyMatch(key -> key.equalsIgnoreCase(provider));
    }

    private boolean isConfiguredSsoProvider(String provider) {
        return authProperties.getSsoProviders().stream()
                .map(AuthProperties.ExternalProvider::getKey)
                .filter(key -> key != null)
                .anyMatch(key -> key.equalsIgnoreCase(provider));
    }
}
