package com.ims.bff.authZ.permissionResolution.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class FeatureTypeProvider {

    private static final String CONFIG_PATH = "configs/feature-type-config.json";

    private final Map<String, FeatureTypeDefinition> featureTypes;

    public FeatureTypeProvider(ObjectMapper objectMapper) {
        FeatureTypeConfig config = loadConfig(objectMapper);
        if (config.featureTypes() == null || config.featureTypes().isEmpty()) {
            throw new IllegalStateException("Feature type config must contain at least one featureTypes entry");
        }
        this.featureTypes = Map.copyOf(config.featureTypes());
    }

    public Map<String, FeatureTypeDefinition> getFeatureTypes() {
        return featureTypes;
    }

    private FeatureTypeConfig loadConfig(ObjectMapper objectMapper) {
        try (InputStream inputStream = new ClassPathResource(CONFIG_PATH).getInputStream()) {
            return objectMapper.readValue(inputStream, FeatureTypeConfig.class);
        } catch (IOException exception) {
            throw new UncheckedIOException("Failed to read feature type config from " + CONFIG_PATH, exception);
        }
    }
}
