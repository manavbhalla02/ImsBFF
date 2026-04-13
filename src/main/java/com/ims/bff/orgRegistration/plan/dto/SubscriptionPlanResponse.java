package com.ims.bff.orgRegistration.plan.dto;

import java.math.BigDecimal;

public record SubscriptionPlanResponse(
        Long planId,
        String planName,
        String billingPeriod,
        BigDecimal price) {
}
