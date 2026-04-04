package com.ims.bff.auth.strategy.result;

import org.springframework.security.core.Authentication;

import com.ims.bff.auth.AuthConstants;
import com.ims.bff.auth.dto.AuthenticationResponse;
import com.ims.bff.auth.enums.AuthType;

public interface AuthenticationResultStrategy {

    AuthType supportedType();

    String supportedProvider();

    AuthenticationResponse handleCallback(Authentication authentication);

    AuthenticationResponse resolveAuthenticatedUser(Authentication authentication);

    default boolean supports(AuthType authType, String provider) {
        return supportedType() == authType
                && supportedProvider().equalsIgnoreCase(provider);
    }

    default boolean isDefaultStrategy() {
        return AuthConstants.DEFAULT_PROVIDER_KEY.equals(supportedProvider());
    }
}
