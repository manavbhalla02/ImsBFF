package com.ims.bff.registration.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ims.bff.registration.entity.OrganizationDomainEntity;

public interface OrganizationDomainRepository extends JpaRepository<OrganizationDomainEntity, Long> {

    boolean existsByDomainNameIgnoreCase(String domainName);
}
