package com.ims.bff.registration.transform;

import org.springframework.stereotype.Component;

import com.ims.bff.common.transform.TypedTransformationStrategy;
import com.ims.bff.registration.context.RegistrationExecutionContext;
import com.ims.bff.registration.entity.SubscriptionEntity;
import com.ims.bff.registration.enums.RegistrationStepType;

@Component
public class SubscriptionTransformationStrategy implements
        TypedTransformationStrategy<RegistrationStepType, SubscriptionEntity, RegistrationExecutionContext> {

    @Override
    public RegistrationStepType supports() {
        return RegistrationStepType.SUBSCRIPTION;
    }

    @Override
    public SubscriptionEntity transform(RegistrationExecutionContext context) {
        SubscriptionEntity subscription = new SubscriptionEntity();
        subscription.setOrganization(context.getOrganization());
        subscription.setPlanId(context.getRequest().subscriptionPlanId());
        subscription.setAutoRenew(context.getRequest().autoRenew());
        subscription.setStatus("ACTIVE");
        return subscription;
    }
}
