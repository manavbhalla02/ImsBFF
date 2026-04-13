package com.ims.bff.orgregistration.planselection.dto;

import java.util.List;

public record SelectablePlanFeaturesResponse(
        Long planId,
        List<SelectableFeatureNodeResponse> features) {
}
