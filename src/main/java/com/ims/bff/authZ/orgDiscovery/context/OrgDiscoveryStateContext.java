package com.ims.bff.authZ.orgDiscovery.context;

import com.ims.bff.authZ.orgDiscovery.enums.OrgDiscoveryState;
import com.ims.bff.authZ.orgDiscovery.model.OrgDiscoverySession;
import com.ims.bff.orgRegistration.registration.entity.OrganizationDomainEntity;

public class OrgDiscoveryStateContext {

    private String emailAddress;
    private String otp;
    private OrgDiscoveryState currentState;
    private OrgDiscoverySession session;
    private OrganizationDomainEntity organizationDomain;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public OrgDiscoveryState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(OrgDiscoveryState currentState) {
        this.currentState = currentState;
    }

    public OrgDiscoverySession getSession() {
        return session;
    }

    public void setSession(OrgDiscoverySession session) {
        this.session = session;
    }

    public OrganizationDomainEntity getOrganizationDomain() {
        return organizationDomain;
    }

    public void setOrganizationDomain(OrganizationDomainEntity organizationDomain) {
        this.organizationDomain = organizationDomain;
    }
}
