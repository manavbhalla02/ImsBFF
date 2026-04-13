package com.ims.bff.orgregistration.plan.dto;

import java.math.BigDecimal;

public record SubscriptionPlanResponse(
        Long planId,
        String planName,
        String billingPeriod,
        BigDecimal price) {
}
