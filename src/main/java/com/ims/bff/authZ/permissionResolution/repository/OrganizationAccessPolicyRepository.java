package com.ims.bff.authZ.permissionResolution.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ims.bff.authZ.permissionResolution.entity.OrganizationAccessPolicyEntity;

public interface OrganizationAccessPolicyRepository extends JpaRepository<OrganizationAccessPolicyEntity, Long> {

    Optional<OrganizationAccessPolicyEntity> findByOrganizationId(Long organizationId);
}
