package com.ims.bff.registration.transform;

import org.springframework.stereotype.Component;

import com.ims.bff.common.transform.TypedTransformationStrategy;
import com.ims.bff.registration.context.RegistrationExecutionContext;
import com.ims.bff.registration.entity.OrganizationEntity;
import com.ims.bff.registration.enums.RegistrationStepType;

@Component
public class OrganizationTransformationStrategy implements
        TypedTransformationStrategy<RegistrationStepType, OrganizationEntity, RegistrationExecutionContext> {

    @Override
    public RegistrationStepType supports() {
        return RegistrationStepType.ORGANIZATION;
    }

    @Override
    public OrganizationEntity transform(RegistrationExecutionContext context) {
        OrganizationEntity organization = new OrganizationEntity();
        organization.setOrgName(context.getRequest().orgName().trim());
        return organization;
    }
}
