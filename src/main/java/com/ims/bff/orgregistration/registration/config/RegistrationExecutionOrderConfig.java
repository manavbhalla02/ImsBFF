package com.ims.bff.orgregistration.registration.config;

import java.util.List;

import com.ims.bff.orgregistration.registration.enums.RegistrationStepType;

public record RegistrationExecutionOrderConfig(List<RegistrationStepType> steps) {
}
