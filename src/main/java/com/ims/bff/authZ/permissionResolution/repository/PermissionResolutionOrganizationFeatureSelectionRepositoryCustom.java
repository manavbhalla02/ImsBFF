package com.ims.bff.authZ.permissionResolution.repository;

import java.util.Collection;
import java.util.List;

import com.ims.bff.orgRegistration.registration.entity.OrganizationFeatureSelectionEntity;
import com.ims.bff.orgRegistration.registration.enums.OrganizationFeatureSelectionStatus;

public interface PermissionResolutionOrganizationFeatureSelectionRepositoryCustom {

    List<OrganizationFeatureSelectionEntity> findByOrganizationIdAndStatusAndFeatureIdIn(
            Long organizationId,
            OrganizationFeatureSelectionStatus status,
            Collection<Long> featureIds);
}
