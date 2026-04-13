package com.ims.bff.orgregistration.registration.executor;

import org.springframework.stereotype.Component;

import com.ims.bff.orgregistration.registration.context.RegistrationExecutionContext;
import com.ims.bff.orgregistration.registration.entity.SubscriptionEntity;
import com.ims.bff.orgregistration.registration.enums.RegistrationStepType;
import com.ims.bff.orgregistration.registration.factory.RegistrationDaoFactory;
import com.ims.bff.orgregistration.registration.factory.RegistrationTransformationFactory;

@Component
public class SubscriptionRegistrationExecutor extends AbstractRegistrationExecutor<SubscriptionEntity> {

    public SubscriptionRegistrationExecutor(
            RegistrationTransformationFactory transformationFactory,
            RegistrationDaoFactory daoFactory) {
        super(transformationFactory, daoFactory);
    }

    @Override
    public RegistrationStepType supports() {
        return RegistrationStepType.SUBSCRIPTION;
    }

    @Override
    protected void updateContext(RegistrationExecutionContext context, SubscriptionEntity persistedPayload) {
        context.setSubscription(persistedPayload);
    }
}
