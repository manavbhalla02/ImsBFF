package com.ims.bff.authZ.orgDiscovery.store;

import java.util.Optional;

import com.ims.bff.authZ.orgDiscovery.model.OrgDiscoverySession;

public interface OrgDiscoverySessionStore {

    void save(OrgDiscoverySession session);

    Optional<OrgDiscoverySession> findById(String discoveryId);
}
