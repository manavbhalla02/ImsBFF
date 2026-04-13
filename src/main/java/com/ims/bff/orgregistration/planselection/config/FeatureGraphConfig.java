package com.ims.bff.orgregistration.planselection.config;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FeatureGraphConfig(
        String graphKey,
        String graphName,
        Integer version,
        List<String> rootFeatures,
        Map<String, List<String>> adjacency) {
}
