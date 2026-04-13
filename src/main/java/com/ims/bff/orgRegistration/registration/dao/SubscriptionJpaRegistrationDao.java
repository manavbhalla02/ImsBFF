package com.ims.bff.orgRegistration.registration.dao;

import com.ims.bff.common.dao.TypedDao;
import com.ims.bff.orgRegistration.registration.entity.SubscriptionEntity;
import com.ims.bff.orgRegistration.registration.enums.RegistrationStepType;
import com.ims.bff.orgRegistration.registration.repository.SubscriptionRepository;

import org.springframework.stereotype.Component;

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
