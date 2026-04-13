package com.ims.bff.orgRegistration.registration.config;

import java.util.List;

import com.ims.bff.orgRegistration.registration.enums.RegistrationStepType;

public record RegistrationExecutionOrderConfig(List<RegistrationStepType> steps) {
}
