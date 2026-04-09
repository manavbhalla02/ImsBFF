package com.ims.bff.registration.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrganizationRegistrationRequest(
        @NotBlank String orgName,
        @NotBlank String domainName,
        @NotNull @Positive Long subscriptionPlanId,
        @NotNull Boolean autoRenew,
        @NotNull List<@NotNull @Positive Long> featureIds) {
}
