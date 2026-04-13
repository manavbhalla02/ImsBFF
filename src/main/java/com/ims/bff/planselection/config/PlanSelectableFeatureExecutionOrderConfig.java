package com.ims.bff.planselection.config;

import java.util.List;

import com.ims.bff.planselection.enums.PlanSelectableFeatureStepType;

public record PlanSelectableFeatureExecutionOrderConfig(List<PlanSelectableFeatureStepType> steps) {
}
