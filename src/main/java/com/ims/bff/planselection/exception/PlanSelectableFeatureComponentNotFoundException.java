package com.ims.bff.planselection.exception;

import com.ims.bff.planselection.enums.PlanSelectableFeatureStepType;

public class PlanSelectableFeatureComponentNotFoundException extends RuntimeException {

    public PlanSelectableFeatureComponentNotFoundException(String componentType, PlanSelectableFeatureStepType stepType) {
        super(componentType + " not found for step type: " + stepType);
    }
}
