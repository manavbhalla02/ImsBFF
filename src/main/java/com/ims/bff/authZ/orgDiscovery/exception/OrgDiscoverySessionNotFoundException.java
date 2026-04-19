package com.ims.bff.authZ.orgDiscovery.exception;

public class OrgDiscoverySessionNotFoundException extends OrgDiscoveryException {

    public OrgDiscoverySessionNotFoundException(String discoveryId) {
        super("No org discovery session found for id " + discoveryId);
    }
}
