package com.ims.bff.registration.transform;

import org.springframework.stereotype.Component;

import com.ims.bff.common.transform.TypedTransformationStrategy;
import com.ims.bff.registration.context.RegistrationExecutionContext;
import com.ims.bff.registration.entity.OrganizationDomainEntity;
import com.ims.bff.registration.enums.RegistrationStepType;

@Component
public class OrganizationDomainTransformationStrategy implements
        TypedTransformationStrategy<RegistrationStepType, OrganizationDomainEntity, RegistrationExecutionContext> {

    @Override
    public RegistrationStepType supports() {
        return RegistrationStepType.ORGANIZATION_DOMAIN;
    }

    @Override
    public OrganizationDomainEntity transform(RegistrationExecutionContext context) {
        OrganizationDomainEntity domain = new OrganizationDomainEntity();
        domain.setOrganization(context.getOrganization());
        domain.setDomainName(context.getRequest().domainName().trim().toLowerCase());
        return domain;
    }
}
