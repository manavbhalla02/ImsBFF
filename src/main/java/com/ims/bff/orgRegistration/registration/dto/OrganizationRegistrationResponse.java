package com.ims.bff.orgRegistration.registration.dto;

public record OrganizationRegistrationResponse(
        Long organizationId,
        Long organizationDomainId,
        Long subscriptionId,
        String message) {
}
