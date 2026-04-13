package com.ims.bff.orgregistration.registration.dao;

import com.ims.bff.common.dao.TypedDao;
import org.springframework.stereotype.Component;

import com.ims.bff.orgregistration.registration.entity.OrganizationEntity;
import com.ims.bff.orgregistration.registration.enums.RegistrationStepType;
import com.ims.bff.orgregistration.registration.repository.OrganizationRepository;

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
