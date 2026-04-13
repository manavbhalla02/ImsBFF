package com.ims.bff.orgregistration.registration.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ims.bff.orgregistration.registration.entity.SubscriptionEntity;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {
}
