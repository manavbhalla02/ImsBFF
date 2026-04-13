package com.ims.bff.orgRegistration.planselection.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ims.bff.orgRegistration.planselection.enums.PlanSelectableFeatureStepType;

@Component
public class PlanSelectableFeatureExecutionOrderProvider {

    private static final String CONFIG_PATH = "configs/plan-selectable-feature-execution-order.json";

    private final List<PlanSelectableFeatureStepType> executionOrder;

    public PlanSelectableFeatureExecutionOrderProvider(ObjectMapper objectMapper) {
        this.executionOrder = loadExecutionOrder(objectMapper);
    }

    public List<PlanSelectableFeatureStepType> getExecutionOrder() {
        return executionOrder;
    }

    private List<PlanSelectableFeatureStepType> loadExecutionOrder(ObjectMapper objectMapper) {
        try (InputStream inputStream = new ClassPathResource(CONFIG_PATH).getInputStream()) {
            PlanSelectableFeatureExecutionOrderConfig config =
                    objectMapper.readValue(inputStream, PlanSelectableFeatureExecutionOrderConfig.class);
            if (config.steps() == null || config.steps().isEmpty()) {
                throw new IllegalStateException("Plan selectable feature execution order config must contain at least one step");
            }
            return List.copyOf(config.steps());
        } catch (IOException exception) {
            throw new UncheckedIOException(
                    "Failed to read plan selectable feature execution order config from " + CONFIG_PATH,
                    exception);
        }
    }
}
