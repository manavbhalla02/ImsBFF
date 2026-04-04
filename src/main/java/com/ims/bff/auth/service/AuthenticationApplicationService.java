package com.ims.bff.auth.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.ims.bff.auth.dto.AuthProviderResponse;
import com.ims.bff.auth.dto.AuthProvidersResponse;
import com.ims.bff.auth.dto.AuthenticationResponse;
import com.ims.bff.auth.dto.BasicLoginRequest;
import com.ims.bff.auth.dto.ResolvedAuthenticationContext;
import com.ims.bff.auth.enums.AuthType;
import com.ims.bff.auth.factory.AuthenticationResultStrategyFactory;
import com.ims.bff.auth.factory.AuthenticationStrategyFactory;

@Service
public class AuthenticationApplicationService {

    private final AuthenticationStrategyFactory strategyFactory;
    private final AuthenticationResultStrategyFactory resultStrategyFactory;
    private final AuthenticationManager authenticationManager;
    private final AuthenticationContextResolver authenticationContextResolver;

    public AuthenticationApplicationService(
            AuthenticationStrategyFactory strategyFactory,
            AuthenticationResultStrategyFactory resultStrategyFactory,
            AuthenticationManager authenticationManager,
            AuthenticationContextResolver authenticationContextResolver) {
        this.strategyFactory = strategyFactory;
        this.resultStrategyFactory = resultStrategyFactory;
        this.authenticationManager = authenticationManager;
        this.authenticationContextResolver = authenticationContextResolver;
    }

    public AuthProvidersResponse getSupportedProviders() {
        List<AuthProviderResponse> providers = strategyFactory.getAllStrategies().stream()
                .flatMap(strategy -> strategy.getSupportedProviders().stream())
                .sorted(Comparator.comparing(AuthProviderResponse::type).thenComparing(AuthProviderResponse::provider))
                .toList();
        return new AuthProvidersResponse(providers);
    }

    public AuthenticationResponse loginWithBasic(BasicLoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(request.username(), request.password()));
        return handleAuthenticationSuccess(authentication);
    }

    public String resolveExternalLoginPath(AuthType authType, String provider) {
        return strategyFactory.getStrategy(authType).resolveLoginPath(provider);
    }

    public String resolveExternalLoginPath(String authType, String provider) {
        return resolveExternalLoginPath(AuthType.valueOf(authType.toUpperCase()), provider);
    }

    public AuthenticationResponse getAuthenticatedUser(Authentication authentication) {
        if (authentication == null) {
            return null;
        }

        ResolvedAuthenticationContext context = authenticationContextResolver.resolve(authentication);
        return resultStrategyFactory.getStrategy(context.authType(), context.provider())
                .resolveAuthenticatedUser(authentication);
    }

    public AuthenticationResponse handleAuthenticationSuccess(Authentication authentication) {
        ResolvedAuthenticationContext context = authenticationContextResolver.resolve(authentication);
        return resultStrategyFactory.getStrategy(context.authType(), context.provider())
                .handleCallback(authentication);
    }
}
