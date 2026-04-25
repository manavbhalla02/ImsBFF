package com.ims.bff.authZ.permissionResolution.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ims.bff.orgRegistration.registration.entity.OrganizationFeatureSelectionEntity;

public interface PermissionResolutionOrganizationFeatureSelectionRepository
        extends JpaRepository<OrganizationFeatureSelectionEntity, Long>,
        PermissionResolutionOrganizationFeatureSelectionRepositoryCustom {
}
