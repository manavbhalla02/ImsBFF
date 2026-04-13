package com.ims.bff.registration.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ims.bff.registration.entity.OrganizationEntity;

public interface OrganizationRepository extends JpaRepository<OrganizationEntity, Long> {
}
