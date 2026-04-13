package com.ims.bff.orgRegistration.planselection.dto;

import java.util.List;

public record SelectablePlanFeaturesResponse(
        Long planId,
        List<SelectableFeatureNodeResponse> features) {
}
