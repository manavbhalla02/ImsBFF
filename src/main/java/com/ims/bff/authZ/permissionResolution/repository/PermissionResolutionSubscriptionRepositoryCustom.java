package com.ims.bff.authZ.permissionResolution.repository;

import java.util.Optional;

import com.ims.bff.orgRegistration.registration.entity.SubscriptionEntity;
import com.ims.bff.orgRegistration.registration.enums.SubscriptionStatus;

public interface PermissionResolutionSubscriptionRepositoryCustom {

    Optional<SubscriptionEntity> findLatestByOrganizationIdAndStatus(Long organizationId, SubscriptionStatus status);
}
