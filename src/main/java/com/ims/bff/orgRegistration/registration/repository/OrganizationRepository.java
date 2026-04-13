package com.ims.bff.orgRegistration.registration.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ims.bff.orgRegistration.registration.entity.OrganizationEntity;

public interface OrganizationRepository extends JpaRepository<OrganizationEntity, Long> {
}
