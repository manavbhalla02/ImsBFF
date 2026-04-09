package com.ims.bff.registration.executor;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ims.bff.registration.context.RegistrationExecutionContext;
import com.ims.bff.registration.entity.OrganizationFeatureSelectionEntity;
import com.ims.bff.registration.enums.RegistrationStepType;
import com.ims.bff.registration.factory.RegistrationDaoFactory;
import com.ims.bff.registration.factory.RegistrationTransformationFactory;

@Component
public class OrganizationFeatureSelectionRegistrationExecutor extends
        AbstractRegistrationExecutor<List<OrganizationFeatureSelectionEntity>> {

    public OrganizationFeatureSelectionRegistrationExecutor(
            RegistrationTransformationFactory transformationFactory,
            RegistrationDaoFactory daoFactory) {
        super(transformationFactory, daoFactory);
    }

    @Override
    public RegistrationStepType supports() {
        return RegistrationStepType.ORGANIZATION_FEATURE_SELECTION;
    }

    @Override
    protected void updateContext(
            RegistrationExecutionContext context,
            List<OrganizationFeatureSelectionEntity> persistedPayload) {
        context.setOrganizationFeatureSelections(persistedPayload);
    }
}
