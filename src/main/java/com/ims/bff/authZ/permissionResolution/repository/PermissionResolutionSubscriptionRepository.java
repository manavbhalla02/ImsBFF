package com.ims.bff.authZ.permissionResolution.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ims.bff.orgRegistration.registration.entity.SubscriptionEntity;

public interface PermissionResolutionSubscriptionRepository
        extends JpaRepository<SubscriptionEntity, Long>, PermissionResolutionSubscriptionRepositoryCustom {
}
