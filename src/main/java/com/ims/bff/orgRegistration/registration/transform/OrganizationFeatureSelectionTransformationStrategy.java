package com.ims.bff.orgRegistration.registration.transform;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ims.bff.common.transform.TypedTransformationStrategy;
import com.ims.bff.orgRegistration.registration.context.RegistrationExecutionContext;
import com.ims.bff.orgRegistration.registration.entity.OrganizationFeatureSelectionEntity;
import com.ims.bff.orgRegistration.registration.enums.OrganizationFeatureSelectionStatus;
import com.ims.bff.orgRegistration.registration.enums.RegistrationStepType;

@Component
public class OrganizationFeatureSelectionTransformationStrategy implements
        TypedTransformationStrategy<RegistrationStepType, List<OrganizationFeatureSelectionEntity>, RegistrationExecutionContext> {

    @Override
    public RegistrationStepType supports() {
        return RegistrationStepType.ORGANIZATION_FEATURE_SELECTION;
    }

    @Override
    public List<OrganizationFeatureSelectionEntity> transform(RegistrationExecutionContext context) {
        return context.getRequest().featureIds().stream()
                .distinct()
                .map(featureId -> {
                    OrganizationFeatureSelectionEntity selection = new OrganizationFeatureSelectionEntity();
                    selection.setOrganization(context.getOrganization());
                    selection.setFeatureId(featureId);
                    selection.setStatus(OrganizationFeatureSelectionStatus.ACTIVE);
                    return selection;
                })
                .toList();
    }
}
