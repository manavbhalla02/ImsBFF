package com.ims.bff.authZ.orgDiscovery.state;

import com.ims.bff.authZ.orgDiscovery.context.OrgDiscoveryStateContext;
import com.ims.bff.authZ.orgDiscovery.enums.OrgDiscoveryState;

public interface OrgDiscoveryStateHandler {

    OrgDiscoveryState supports();

    void handle(OrgDiscoveryStateContext context);
}
