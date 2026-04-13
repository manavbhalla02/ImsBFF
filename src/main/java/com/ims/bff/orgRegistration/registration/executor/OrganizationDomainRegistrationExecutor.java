package com.ims.bff.orgRegistration.registration.executor;

import org.springframework.stereotype.Component;

import com.ims.bff.orgRegistration.registration.context.RegistrationExecutionContext;
import com.ims.bff.orgRegistration.registration.entity.OrganizationDomainEntity;
import com.ims.bff.orgRegistration.registration.enums.RegistrationStepType;
import com.ims.bff.orgRegistration.registration.factory.RegistrationDaoFactory;
import com.ims.bff.orgRegistration.registration.factory.RegistrationTransformationFactory;

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
