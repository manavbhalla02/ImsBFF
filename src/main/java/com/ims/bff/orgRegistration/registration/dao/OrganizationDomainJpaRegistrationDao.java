package com.ims.bff.orgRegistration.registration.dao;

import com.ims.bff.common.dao.TypedDao;
import com.ims.bff.orgRegistration.registration.entity.OrganizationDomainEntity;
import com.ims.bff.orgRegistration.registration.enums.RegistrationStepType;
import com.ims.bff.orgRegistration.registration.exception.DuplicateDomainException;
import com.ims.bff.orgRegistration.registration.repository.OrganizationDomainRepository;

import org.springframework.stereotype.Component;

@Component
public class OrganizationDomainJpaRegistrationDao implements TypedDao<RegistrationStepType, OrganizationDomainEntity> {

    private final OrganizationDomainRepository organizationDomainRepository;

    public OrganizationDomainJpaRegistrationDao(OrganizationDomainRepository organizationDomainRepository) {
        this.organizationDomainRepository = organizationDomainRepository;
    }

    @Override
    public RegistrationStepType supports() {
        return RegistrationStepType.ORGANIZATION_DOMAIN;
    }

    @Override
    public OrganizationDomainEntity save(OrganizationDomainEntity payload) {
        if (organizationDomainRepository.existsByDomainNameIgnoreCase(payload.getDomainName())) {
            throw new DuplicateDomainException(payload.getDomainName());
        }
        return organizationDomainRepository.save(payload);
    }
}
