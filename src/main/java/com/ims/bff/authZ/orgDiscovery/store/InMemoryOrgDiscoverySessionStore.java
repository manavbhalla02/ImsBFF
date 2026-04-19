package com.ims.bff.authZ.orgDiscovery.store;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;

import com.ims.bff.authZ.orgDiscovery.model.OrgDiscoverySession;

@Component
public class InMemoryOrgDiscoverySessionStore implements OrgDiscoverySessionStore {

    private final ConcurrentMap<String, OrgDiscoverySession> sessions = new ConcurrentHashMap<>();

    @Override
    public void save(OrgDiscoverySession session) {
        sessions.put(session.getDiscoveryId(), session);
    }

    @Override
    public Optional<OrgDiscoverySession> findById(String discoveryId) {
        return Optional.ofNullable(sessions.get(discoveryId));
    }
}
