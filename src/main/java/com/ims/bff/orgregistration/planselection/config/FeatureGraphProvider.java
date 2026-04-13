package com.ims.bff.orgregistration.planselection.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class FeatureGraphProvider {

    private static final String CONFIG_PATH = "configs/feature-graph.json";

    private final FeatureGraphConfig featureGraphConfig;

    public FeatureGraphProvider(ObjectMapper objectMapper) {
        this.featureGraphConfig = loadConfig(objectMapper);
    }

    public Map<String, List<String>> getAdjacency() {
        return featureGraphConfig.adjacency();
    }

    private FeatureGraphConfig loadConfig(ObjectMapper objectMapper) {
        try (InputStream inputStream = new ClassPathResource(CONFIG_PATH).getInputStream()) {
            return objectMapper.readValue(inputStream, FeatureGraphConfig.class);
        } catch (IOException exception) {
            throw new UncheckedIOException("Failed to read feature graph config from " + CONFIG_PATH, exception);
        }
    }
}
