package com.ims.bff.authZ.orgDiscovery.exception;

public class OrganizationNotFoundForEmailException extends OrgDiscoveryException {

    public OrganizationNotFoundForEmailException(String domainName) {
        super("No organization found for domain " + domainName);
    }
}
