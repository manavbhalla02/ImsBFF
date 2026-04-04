package com.ims.bff.auth.factory;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ims.bff.auth.enums.AuthType;
import com.ims.bff.auth.strategy.result.AuthenticationResultStrategy;

@Component
public class AuthenticationResultStrategyFactory {

    private final Map<AuthType, Map<String, AuthenticationResultStrategy>> exactStrategies;
    private final Map<AuthType, AuthenticationResultStrategy> defaultStrategies;

    public AuthenticationResultStrategyFactory(List<AuthenticationResultStrategy> strategies) {
        this.exactStrategies = new EnumMap<>(AuthType.class);
        this.defaultStrategies = new EnumMap<>(AuthType.class);

        strategies.forEach(strategy -> {
            if (strategy.isDefaultStrategy()) {
                defaultStrategies.put(strategy.supportedType(), strategy);
                return;
            }

            exactStrategies.computeIfAbsent(strategy.supportedType(), ignored -> new HashMap<>())
                    .put(normalize(strategy.supportedProvider()), strategy);
        });
    }

    public AuthenticationResultStrategy getStrategy(AuthType authType, String provider) {
        AuthenticationResultStrategy exactStrategy = exactStrategies
                .getOrDefault(authType, Map.of())
                .get(normalize(provider));

        if (exactStrategy != null) {
            return exactStrategy;
        }

        AuthenticationResultStrategy defaultStrategy = defaultStrategies.get(authType);
        if (defaultStrategy != null) {
            return defaultStrategy;
        }

        throw new IllegalArgumentException(
                "No authentication result strategy registered for " + authType + " and provider " + provider);
    }

    private String normalize(String provider) {
        return provider == null ? "" : provider.toLowerCase(Locale.ROOT);
    }
}
