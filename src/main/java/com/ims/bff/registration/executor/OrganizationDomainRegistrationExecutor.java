package com.ims.bff.registration.executor;

import org.springframework.stereotype.Component;

import com.ims.bff.registration.context.RegistrationExecutionContext;
import com.ims.bff.registration.entity.OrganizationDomainEntity;
import com.ims.bff.registration.enums.RegistrationStepType;
import com.ims.bff.registration.factory.RegistrationDaoFactory;
import com.ims.bff.registration.factory.RegistrationTransformationFactory;

@Component
public class OrganizationDomainRegistrationExecutor extends AbstractRegistrationExecutor<OrganizationDomainEntity> {

    public OrganizationDomainRegistrationExecutor(
            RegistrationTransformationFactory transformationFactory,
            RegistrationDaoFactory daoFactory) {
        super(transformationFactory, daoFactory);
    }

    @Override
    public RegistrationStepType supports() {
        return RegistrationStepType.ORGANIZATION_DOMAIN;
    }

    @Override
    protected void updateContext(RegistrationExecutionContext context, OrganizationDomainEntity persistedPayload) {
        context.setOrganizationDomain(persistedPayload);
    }
}
