package com.ims.bff.registration.dao;

import com.ims.bff.common.dao.TypedDao;
import org.springframework.stereotype.Component;

import com.ims.bff.registration.entity.SubscriptionEntity;
import com.ims.bff.registration.enums.RegistrationStepType;
import com.ims.bff.registration.repository.SubscriptionRepository;

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
