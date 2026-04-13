package com.ims.bff.orgRegistration.planselection.config;

import java.util.List;

import com.ims.bff.orgRegistration.planselection.enums.PlanSelectableFeatureStepType;

public record PlanSelectableFeatureExecutionOrderConfig(List<PlanSelectableFeatureStepType> steps) {
}
