package com.ims.bff.authZ.permissionResolution.dto;

public record FeatureActionResponse(
        String method,
        String url) {
}
