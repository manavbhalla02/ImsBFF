package com.ims.bff.orgregistration.registration.executor;

import org.springframework.stereotype.Component;

import com.ims.bff.orgregistration.registration.context.RegistrationExecutionContext;
import com.ims.bff.orgregistration.registration.entity.OrganizationEntity;
import com.ims.bff.orgregistration.registration.enums.RegistrationStepType;
import com.ims.bff.orgregistration.registration.factory.RegistrationDaoFactory;
import com.ims.bff.orgregistration.registration.factory.RegistrationTransformationFactory;

@Component
public class OrganizationRegistrationExecutor extends AbstractRegistrationExecutor<OrganizationEntity> {

    public OrganizationRegistrationExecutor(
            RegistrationTransformationFactory transformationFactory,
            RegistrationDaoFactory daoFactory) {
        super(transformationFactory, daoFactory);
    }

    @Override
    public RegistrationStepType supports() {
        return RegistrationStepType.ORGANIZATION;
    }

    @Override
    protected void updateContext(RegistrationExecutionContext context, OrganizationEntity persistedPayload) {
        context.setOrganization(persistedPayload);
    }
}
