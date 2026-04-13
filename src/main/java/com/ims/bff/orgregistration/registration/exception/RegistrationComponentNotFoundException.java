package com.ims.bff.orgregistration.registration.exception;

import com.ims.bff.orgregistration.registration.enums.RegistrationStepType;

public class RegistrationComponentNotFoundException extends RuntimeException {

    public RegistrationComponentNotFoundException(String componentType, RegistrationStepType stepType) {
        super(componentType + " not found for step: " + stepType);
    }
}
