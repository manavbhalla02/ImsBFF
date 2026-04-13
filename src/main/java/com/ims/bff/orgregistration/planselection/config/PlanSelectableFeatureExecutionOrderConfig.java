package com.ims.bff.orgregistration.planselection.config;

import java.util.List;

import com.ims.bff.orgregistration.planselection.enums.PlanSelectableFeatureStepType;

public record PlanSelectableFeatureExecutionOrderConfig(List<PlanSelectableFeatureStepType> steps) {
}
