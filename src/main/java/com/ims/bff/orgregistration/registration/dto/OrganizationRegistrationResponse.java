package com.ims.bff.orgregistration.registration.dto;

public record OrganizationRegistrationResponse(
        Long organizationId,
        Long organizationDomainId,
        Long subscriptionId,
        String message) {
}
