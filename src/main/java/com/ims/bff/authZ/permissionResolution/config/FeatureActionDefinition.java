package com.ims.bff.authZ.permissionResolution.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FeatureActionDefinition(
        String method,
        String url) {
}
