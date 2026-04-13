package com.ims.bff.orgRegistration.registration.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ims.bff.orgRegistration.registration.config.RegistrationExecutionOrderProvider;
import com.ims.bff.orgRegistration.registration.context.RegistrationExecutionContext;
import com.ims.bff.orgRegistration.registration.dto.OrganizationRegistrationRequest;
import com.ims.bff.orgRegistration.registration.dto.OrganizationRegistrationResponse;
import com.ims.bff.orgRegistration.registration.factory.RegistrationExecutorFactory;

@Service
public class OrganizationRegistrationApplicationService {

    private final RegistrationExecutorFactory executorFactory;
    private final RegistrationExecutionOrderProvider executionOrderProvider;

    public OrganizationRegistrationApplicationService(
            RegistrationExecutorFactory executorFactory,
            RegistrationExecutionOrderProvider executionOrderProvider) {
        this.executorFactory = executorFactory;
        this.executionOrderProvider = executionOrderProvider;
    }

    @Transactional
    public OrganizationRegistrationResponse register(OrganizationRegistrationRequest request) {
        RegistrationExecutionContext context = new RegistrationExecutionContext(request);
        executionOrderProvider.getExecutionOrder()
                .forEach(step -> executorFactory.getExecutor(step).execute(context));

        return new OrganizationRegistrationResponse(
                context.getOrganization().getOrganizationId(),
                context.getOrganizationDomain().getOrganizationDomainId(),
                context.getSubscription().getSubscriptionId(),
                "Organization registration completed successfully");
    }
}
