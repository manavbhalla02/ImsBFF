package com.ims.bff.registration.config;

import java.util.List;

import com.ims.bff.registration.enums.RegistrationStepType;

public record RegistrationExecutionOrderConfig(List<RegistrationStepType> steps) {
}
