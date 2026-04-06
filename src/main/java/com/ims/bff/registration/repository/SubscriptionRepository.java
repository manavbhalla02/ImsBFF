package com.ims.bff.registration.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ims.bff.registration.entity.SubscriptionEntity;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {
}
