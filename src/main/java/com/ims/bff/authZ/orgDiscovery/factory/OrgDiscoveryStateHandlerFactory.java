package com.ims.bff.authZ.orgDiscovery.factory;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ims.bff.authZ.orgDiscovery.enums.OrgDiscoveryState;
import com.ims.bff.authZ.orgDiscovery.exception.OrgDiscoveryException;
import com.ims.bff.authZ.orgDiscovery.state.OrgDiscoveryStateHandler;

@Component
public class OrgDiscoveryStateHandlerFactory {

    private final Map<OrgDiscoveryState, OrgDiscoveryStateHandler> handlersByState;

    public OrgDiscoveryStateHandlerFactory(List<OrgDiscoveryStateHandler> handlers) {
        this.handlersByState = handlers.stream()
                .collect(Collectors.toMap(OrgDiscoveryStateHandler::supports, Function.identity()));
    }

    public OrgDiscoveryStateHandler getHandler(OrgDiscoveryState state) {
        OrgDiscoveryStateHandler handler = handlersByState.get(state);
        if (handler == null) {
            throw new OrgDiscoveryException("No org discovery state handler configured for state " + state);
        }
        return handler;
    }
}
