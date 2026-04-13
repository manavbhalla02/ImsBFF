package com.ims.bff.orgRegistration.registration.factory;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ims.bff.common.executor.TypedExecutor;
import com.ims.bff.common.factory.AbstractComponentFactory;
import com.ims.bff.orgRegistration.registration.context.RegistrationExecutionContext;
import com.ims.bff.orgRegistration.registration.enums.RegistrationStepType;
import com.ims.bff.orgRegistration.registration.exception.RegistrationComponentNotFoundException;

@Component
public class RegistrationExecutorFactory extends AbstractComponentFactory<
        RegistrationStepType,
        TypedExecutor<RegistrationStepType, ?, RegistrationExecutionContext>> {

    public RegistrationExecutorFactory(List<TypedExecutor<RegistrationStepType, ?, RegistrationExecutionContext>> executors) {
        super(executors);
    }

    @SuppressWarnings("unchecked")
    public <T> TypedExecutor<RegistrationStepType, T, RegistrationExecutionContext> getExecutor(RegistrationStepType stepType) {
        return (TypedExecutor<RegistrationStepType, T, RegistrationExecutionContext>) getComponent(
                stepType,
                new RegistrationComponentNotFoundException("Executor", stepType));
    }
}
