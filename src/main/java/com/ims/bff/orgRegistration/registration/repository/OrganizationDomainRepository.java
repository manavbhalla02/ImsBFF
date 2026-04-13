package com.ims.bff.orgRegistration.registration.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ims.bff.orgRegistration.registration.entity.OrganizationDomainEntity;

public interface OrganizationDomainRepository extends JpaRepository<OrganizationDomainEntity, Long> {

    boolean existsByDomainNameIgnoreCase(String domainName);
}
