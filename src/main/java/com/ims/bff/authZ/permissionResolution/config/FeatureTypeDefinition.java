package com.ims.bff.authZ.permissionResolution.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FeatureTypeDefinition(
        String type,
        String displayName,
        Boolean organizationWide,
        List<String> defaultActions,
        FeatureActionDefinition action) {
}
