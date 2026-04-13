package com.ims.bff.orgRegistration.registration.dao;

import com.ims.bff.common.dao.TypedDao;
import com.ims.bff.orgRegistration.registration.entity.OrganizationEntity;
import com.ims.bff.orgRegistration.registration.enums.RegistrationStepType;
import com.ims.bff.orgRegistration.registration.repository.OrganizationRepository;

import org.springframework.stereotype.Component;

@Component
public class OrganizationJpaRegistrationDao implements TypedDao<RegistrationStepType, OrganizationEntity> {

    private final OrganizationRepository organizationRepository;

    public OrganizationJpaRegistrationDao(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Override
    public RegistrationStepType supports() {
        return RegistrationStepType.ORGANIZATION;
    }

    @Override
    public OrganizationEntity save(OrganizationEntity payload) {
        return organizationRepository.save(payload);
    }
}
