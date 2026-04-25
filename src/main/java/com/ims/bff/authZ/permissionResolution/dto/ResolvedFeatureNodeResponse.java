package com.ims.bff.authZ.permissionResolution.dto;

import java.util.List;

public record ResolvedFeatureNodeResponse(
        Long featureId,
        String featureKey,
        String displayName,
        String type,
        FeatureActionResponse action,
        List<String> actions,
        List<FeatureRestrictionResponse> restrictions,
        List<ResolvedFeatureNodeResponse> children) {
}
