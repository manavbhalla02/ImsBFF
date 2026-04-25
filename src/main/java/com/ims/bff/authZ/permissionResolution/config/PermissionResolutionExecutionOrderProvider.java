package com.ims.bff.authZ.permissionResolution.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ims.bff.authZ.permissionResolution.enums.PermissionResolutionStepType;

@Component
public class PermissionResolutionExecutionOrderProvider {

    private static final String CONFIG_PATH = "configs/permission-resolution-execution-order.json";

    private final List<PermissionResolutionStepType> executionOrder;

    public PermissionResolutionExecutionOrderProvider(ObjectMapper objectMapper) {
        this.executionOrder = loadExecutionOrder(objectMapper);
    }

    public List<PermissionResolutionStepType> getExecutionOrder() {
        return executionOrder;
    }

    private List<PermissionResolutionStepType> loadExecutionOrder(ObjectMapper objectMapper) {
        try (InputStream inputStream = new ClassPathResource(CONFIG_PATH).getInputStream()) {
            PermissionResolutionExecutionOrderConfig config =
                    objectMapper.readValue(inputStream, PermissionResolutionExecutionOrderConfig.class);
            if (config.steps() == null || config.steps().isEmpty()) {
                throw new IllegalStateException(
                        "Permission resolution execution order config must contain at least one step");
            }
            return List.copyOf(config.steps());
        } catch (IOException exception) {
            throw new UncheckedIOException(
                    "Failed to read permission resolution execution order config from " + CONFIG_PATH,
                    exception);
        }
    }
}
