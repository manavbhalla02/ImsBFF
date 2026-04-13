package com.ims.bff.orgregistration.registration.dao;

import com.ims.bff.common.dao.TypedDao;
import org.springframework.stereotype.Component;

import com.ims.bff.orgregistration.registration.entity.SubscriptionEntity;
import com.ims.bff.orgregistration.registration.enums.RegistrationStepType;
import com.ims.bff.orgregistration.registration.repository.SubscriptionRepository;

@Component
public class SubscriptionJpaRegistrationDao implements TypedDao<RegistrationStepType, SubscriptionEntity> {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionJpaRegistrationDao(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public RegistrationStepType supports() {
        return RegistrationStepType.SUBSCRIPTION;
    }

    @Override
    public SubscriptionEntity save(SubscriptionEntity payload) {
        return subscriptionRepository.save(payload);
    }
}
