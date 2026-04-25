package com.ims.bff.authZ.permissionResolution.config;

import java.util.List;

import com.ims.bff.authZ.permissionResolution.enums.PermissionResolutionStepType;

public record PermissionResolutionExecutionOrderConfig(List<PermissionResolutionStepType> steps) {
}
