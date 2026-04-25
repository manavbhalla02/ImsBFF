package com.ims.bff.authZ.permissionResolution.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.ims.bff.authZ.permissionResolution.enums.PermissionResolutionStage;

public record PermissionResolutionRequest(
        @NotNull Long organizationId,
        @NotNull Long employeeId,
        @NotNull PermissionResolutionStage stage,
        @NotBlank String rootFeatureKey) {
}
