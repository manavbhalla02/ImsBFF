package com.ims.bff.authZ.permissionResolution.dto;

public record FeatureRestrictionResponse(
        String restrictionKey,
        String restrictionType,
        String operator,
        String value,
        String unit) {
}
