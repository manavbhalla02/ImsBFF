package com.ims.bff.orgregistration.registration.transform;

import java.time.Instant;

import org.springframework.stereotype.Component;

import com.ims.bff.common.transform.TypedTransformationStrategy;
import com.ims.bff.orgregistration.registration.context.RegistrationExecutionContext;
import com.ims.bff.orgregistration.registration.entity.SubscriptionEntity;
import com.ims.bff.orgregistration.registration.enums.RegistrationStepType;
import com.ims.bff.orgregistration.registration.enums.SubscriptionStatus;

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
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setStartAt(Instant.now());
        return subscription;
    }
}
