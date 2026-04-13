package com.ims.bff.orgregistration.registration.factory;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ims.bff.common.factory.AbstractComponentFactory;
import com.ims.bff.common.transform.TypedTransformationStrategy;
import com.ims.bff.orgregistration.registration.context.RegistrationExecutionContext;
import com.ims.bff.orgregistration.registration.enums.RegistrationStepType;
import com.ims.bff.orgregistration.registration.exception.RegistrationComponentNotFoundException;

@Component
public class RegistrationTransformationFactory extends AbstractComponentFactory<
        RegistrationStepType,
        TypedTransformationStrategy<RegistrationStepType, ?, RegistrationExecutionContext>> {

    public RegistrationTransformationFactory(List<TypedTransformationStrategy<RegistrationStepType, ?, RegistrationExecutionContext>> strategies) {
        super(strategies);
    }

    @SuppressWarnings("unchecked")
    public <T> TypedTransformationStrategy<RegistrationStepType, T, RegistrationExecutionContext> getStrategy(RegistrationStepType stepType) {
        return (TypedTransformationStrategy<RegistrationStepType, T, RegistrationExecutionContext>) getComponent(
                stepType,
                new RegistrationComponentNotFoundException("Transformation strategy", stepType));
    }
}
