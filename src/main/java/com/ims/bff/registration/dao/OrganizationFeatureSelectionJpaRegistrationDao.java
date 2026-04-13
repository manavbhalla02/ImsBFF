package com.ims.bff.registration.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ims.bff.common.dao.TypedDao;
import com.ims.bff.registration.entity.OrganizationFeatureSelectionEntity;
import com.ims.bff.registration.enums.RegistrationStepType;
import com.ims.bff.registration.repository.OrganizationFeatureSelectionRepository;

@Component
public class OrganizationFeatureSelectionJpaRegistrationDao implements
        TypedDao<RegistrationStepType, List<OrganizationFeatureSelectionEntity>> {

    private final OrganizationFeatureSelectionRepository organizationFeatureSelectionRepository;

    public OrganizationFeatureSelectionJpaRegistrationDao(
            OrganizationFeatureSelectionRepository organizationFeatureSelectionRepository) {
        this.organizationFeatureSelectionRepository = organizationFeatureSelectionRepository;
    }

    @Override
    public RegistrationStepType supports() {
        return RegistrationStepType.ORGANIZATION_FEATURE_SELECTION;
    }

    @Override
    public List<OrganizationFeatureSelectionEntity> save(List<OrganizationFeatureSelectionEntity> payload) {
        return organizationFeatureSelectionRepository.saveAll(payload);
    }
}
