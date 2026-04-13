package com.ims.bff.orgregistration.planselection.dto;

import java.util.List;

public record SelectableFeatureNodeResponse(
        Long featureId,
        String featureKey,
        String featureName,
        String selectionScope,
        Integer minSelectable,
        Integer maxSelectable,
        Boolean selectionRequired,
        List<SelectableFeatureNodeResponse> children) {
}
