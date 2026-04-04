package com.ims.bff.auth.factory;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ims.bff.auth.enums.AuthType;
import com.ims.bff.auth.strategy.login.AuthenticationStrategy;

@Component
public class AuthenticationStrategyFactory {

    private final Map<AuthType, AuthenticationStrategy> strategyMap;

    public AuthenticationStrategyFactory(List<AuthenticationStrategy> strategies) {
        this.strategyMap = new EnumMap<>(AuthType.class);
        strategies.forEach(strategy -> strategyMap.put(strategy.supportedType(), strategy));
    }

    public AuthenticationStrategy getStrategy(AuthType authType) {
        AuthenticationStrategy strategy = strategyMap.get(authType);
        if (strategy == null) {
            throw new IllegalArgumentException("No authentication strategy registered for " + authType);
        }
        return strategy;
    }

    public List<AuthenticationStrategy> getAllStrategies() {
        return List.copyOf(strategyMap.values());
    }
}
