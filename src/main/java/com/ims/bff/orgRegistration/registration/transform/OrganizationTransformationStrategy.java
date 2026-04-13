package com.ims.bff.orgRegistration.registration.transform;

import java.security.SecureRandom;
import java.util.Locale;

import org.springframework.stereotype.Component;

import com.ims.bff.common.transform.TypedTransformationStrategy;
import com.ims.bff.orgRegistration.registration.context.RegistrationExecutionContext;
import com.ims.bff.orgRegistration.registration.entity.OrganizationEntity;
import com.ims.bff.orgRegistration.registration.enums.RegistrationStepType;

@Component
public class OrganizationTransformationStrategy implements
        TypedTransformationStrategy<RegistrationStepType, OrganizationEntity, RegistrationExecutionContext> {

    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int ORG_CODE_LENGTH = 8;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Override
    public RegistrationStepType supports() {
        return RegistrationStepType.ORGANIZATION;
    }

    @Override
    public OrganizationEntity transform(RegistrationExecutionContext context) {
        OrganizationEntity organization = new OrganizationEntity();
        String orgName = context.getRequest().orgName().trim();
        organization.setOrgName(orgName);
        organization.setOrgCode(generateOrgCode(orgName));
        return organization;
    }

    private String generateOrgCode(String orgName) {
        String normalizedName = orgName.replaceAll("[^A-Za-z0-9]", "").toUpperCase(Locale.ROOT);
        StringBuilder orgCode = new StringBuilder(ORG_CODE_LENGTH);

        if (!normalizedName.isBlank()) {
            orgCode.append(normalizedName, 0, Math.min(3, normalizedName.length()));
        }

        while (orgCode.length() < ORG_CODE_LENGTH) {
            int randomIndex = SECURE_RANDOM.nextInt(ALPHANUMERIC.length());
            orgCode.append(ALPHANUMERIC.charAt(randomIndex));
        }

        return orgCode.substring(0, ORG_CODE_LENGTH);
    }
}
