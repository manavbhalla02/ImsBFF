package com.ims.bff.orgregistration.registration.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ims.bff.orgregistration.registration.entity.OrganizationEntity;

public interface OrganizationRepository extends JpaRepository<OrganizationEntity, Long> {
}
