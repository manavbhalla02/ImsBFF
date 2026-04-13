package com.ims.bff.registration.dao;

import com.ims.bff.common.dao.TypedDao;
import org.springframework.stereotype.Component;

import com.ims.bff.registration.entity.OrganizationDomainEntity;
import com.ims.bff.registration.enums.RegistrationStepType;
import com.ims.bff.registration.exception.DuplicateDomainException;
import com.ims.bff.registration.repository.OrganizationDomainRepository;

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
