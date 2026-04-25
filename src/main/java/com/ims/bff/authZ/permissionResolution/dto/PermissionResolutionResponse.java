package com.ims.bff.authZ.permissionResolution.dto;

import com.ims.bff.authZ.permissionResolution.enums.PermissionResolutionStage;

public record PermissionResolutionResponse(
        Long organizationId,
        Long employeeId,
        PermissionResolutionStage stage,
        ResolvedFeatureNodeResponse rootFeature) {
}
