package com.ims.bff.orgregistration.registration.executor;

import com.ims.bff.common.executor.TypedExecutor;
import com.ims.bff.orgregistration.registration.context.RegistrationExecutionContext;
import com.ims.bff.orgregistration.registration.factory.RegistrationDaoFactory;
import com.ims.bff.orgregistration.registration.factory.RegistrationTransformationFactory;
import com.ims.bff.orgregistration.registration.enums.RegistrationStepType;

public abstract class AbstractRegistrationExecutor<T> implements
        TypedExecutor<RegistrationStepType, T, RegistrationExecutionContext> {

    private final RegistrationTransformationFactory transformationFactory;
    private final RegistrationDaoFactory daoFactory;

    protected AbstractRegistrationExecutor(
            RegistrationTransformationFactory transformationFactory,
            RegistrationDaoFactory daoFactory) {
        this.transformationFactory = transformationFactory;
        this.daoFactory = daoFactory;
    }

    @Override
    public T execute(RegistrationExecutionContext context) {
        var stepType = supports();
        var transformationStrategy =
                transformationFactory.<T>getStrategy(stepType);
        var dao = daoFactory.<T>getDao(stepType);
        T transformedPayload = transformationStrategy.transform(context);
        T persistedPayload = dao.save(transformedPayload);
        updateContext(context, persistedPayload);
        return persistedPayload;
    }

    protected abstract void updateContext(RegistrationExecutionContext context, T persistedPayload);
}
