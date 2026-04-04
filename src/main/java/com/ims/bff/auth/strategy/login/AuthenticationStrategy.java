package com.ims.bff.auth.strategy.login;

import java.util.List;

import com.ims.bff.auth.dto.AuthProviderResponse;
import com.ims.bff.auth.enums.AuthType;

public interface AuthenticationStrategy {

    AuthType supportedType();

    List<AuthProviderResponse> getSupportedProviders();

    String resolveLoginPath(String provider);
}
