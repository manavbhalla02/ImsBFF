package com.ims.bff.registration.context;

import com.ims.bff.registration.dto.OrganizationRegistrationRequest;
import com.ims.bff.registration.entity.OrganizationDomainEntity;
import com.ims.bff.registration.entity.OrganizationEntity;
import com.ims.bff.registration.entity.SubscriptionEntity;

public class RegistrationExecutionContext {

    private final OrganizationRegistrationRequest request;
    private OrganizationEntity organization;
    private OrganizationDomainEntity organizationDomain;
    private SubscriptionEntity subscription;

    public RegistrationExecutionContext(OrganizationRegistrationRequest request) {
        this.request = request;
    }

    public OrganizationRegistrationRequest getRequest() {
        return request;
    }

    public OrganizationEntity getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationEntity organization) {
        this.organization = organization;
    }

    public OrganizationDomainEntity getOrganizationDomain() {
        return organizationDomain;
    }

    public void setOrganizationDomain(OrganizationDomainEntity organizationDomain) {
        this.organizationDomain = organizationDomain;
    }

    public SubscriptionEntity getSubscription() {
        return subscription;
    }

    public void setSubscription(SubscriptionEntity subscription) {
        this.subscription = subscription;
    }
}
