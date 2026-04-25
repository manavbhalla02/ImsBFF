package com.ims.bff.authZ.permissionResolution.config;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FeatureTypeConfig(
        String configKey,
        String configName,
        Integer version,
        Map<String, FeatureTypeDefinition> featureTypes) {
}
