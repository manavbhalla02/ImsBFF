package com.ims.bff.registration.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ims.bff.registration.context.RegistrationExecutionContext;
import com.ims.bff.registration.dto.OrganizationRegistrationRequest;
import com.ims.bff.registration.dto.OrganizationRegistrationResponse;
import com.ims.bff.registration.enums.RegistrationStepType;
import com.ims.bff.registration.factory.RegistrationExecutorFactory;

@Service
public class OrganizationRegistrationApplicationService {

    private static final List<RegistrationStepType> EXECUTION_ORDER = List.of(
            RegistrationStepType.ORGANIZATION,
            RegistrationStepType.ORGANIZATION_DOMAIN,
            RegistrationStepType.SUBSCRIPTION);

    private final RegistrationExecutorFactory executorFactory;

    public OrganizationRegistrationApplicationService(RegistrationExecutorFactory executorFactory) {
        this.executorFactory = executorFactory;
    }

    @Transactional
    public OrganizationRegistrationResponse register(OrganizationRegistrationRequest request) {
        RegistrationExecutionContext context = new RegistrationExecutionContext(request);
        EXECUTION_ORDER.forEach(step -> executorFactory.getExecutor(step).execute(context));

        return new OrganizationRegistrationResponse(
                context.getOrganization().getId(),
                context.getOrganizationDomain().getId(),
                context.getSubscription().getId(),
                "Organization registration completed successfully");
    }
}
