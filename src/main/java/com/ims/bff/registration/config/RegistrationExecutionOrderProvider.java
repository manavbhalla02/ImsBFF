package com.ims.bff.registration.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ims.bff.registration.enums.RegistrationStepType;

@Component
public class RegistrationExecutionOrderProvider {

    private static final String CONFIG_PATH = "configs/registration-execution-order.json";

    private final List<RegistrationStepType> executionOrder;

    public RegistrationExecutionOrderProvider(ObjectMapper objectMapper) {
        this.executionOrder = loadExecutionOrder(objectMapper);
    }

    public List<RegistrationStepType> getExecutionOrder() {
        return executionOrder;
    }

    private List<RegistrationStepType> loadExecutionOrder(ObjectMapper objectMapper) {
        ClassPathResource resource = new ClassPathResource(CONFIG_PATH);
        try (InputStream inputStream = resource.getInputStream()) {
            RegistrationExecutionOrderConfig config =
                    objectMapper.readValue(inputStream, RegistrationExecutionOrderConfig.class);
            if (config.steps() == null || config.steps().isEmpty()) {
                throw new IllegalStateException("Registration execution order config must contain at least one step");
            }
            return List.copyOf(config.steps());
        } catch (IOException exception) {
            throw new UncheckedIOException(
                    "Failed to read registration execution order config from " + CONFIG_PATH,
                    exception);
        }
    }
}
