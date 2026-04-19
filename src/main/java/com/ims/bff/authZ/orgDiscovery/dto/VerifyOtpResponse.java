package com.ims.bff.authZ.orgDiscovery.dto;

import com.ims.bff.authZ.orgDiscovery.enums.OrgDiscoveryState;

public class VerifyOtpResponse {

    private final String discoveryId;
    private final OrgDiscoveryState state;
    private final DiscoveredOrganizationResponse organization;

    public VerifyOtpResponse(String discoveryId, OrgDiscoveryState state, DiscoveredOrganizationResponse organization) {
        this.discoveryId = discoveryId;
        this.state = state;
        this.organization = organization;
    }

    public String getDiscoveryId() {
        return discoveryId;
    }

    public OrgDiscoveryState getState() {
        return state;
    }

    public DiscoveredOrganizationResponse getOrganization() {
        return organization;
    }
}
