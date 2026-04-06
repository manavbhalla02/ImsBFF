package com.ims.bff.registration.dto;

public record OrganizationRegistrationResponse(
        Long organizationId,
        Long organizationDomainId,
        Long subscriptionId,
        String message) {
}
