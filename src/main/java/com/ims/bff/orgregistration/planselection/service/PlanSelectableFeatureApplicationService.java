package com.ims.bff.orgregistration.planselection.service;

import org.springframework.stereotype.Service;

import com.ims.bff.orgregistration.planselection.config.PlanSelectableFeatureExecutionOrderProvider;
import com.ims.bff.orgregistration.planselection.context.PlanSelectableFeatureContext;
import com.ims.bff.orgregistration.planselection.dto.SelectablePlanFeaturesResponse;
import com.ims.bff.orgregistration.planselection.factory.PlanSelectableFeatureExecutorFactory;

@Service
public class PlanSelectableFeatureApplicationService {

    private final PlanSelectableFeatureExecutorFactory executorFactory;
    private final PlanSelectableFeatureExecutionOrderProvider executionOrderProvider;

    public PlanSelectableFeatureApplicationService(
            PlanSelectableFeatureExecutorFactory executorFactory,
            PlanSelectableFeatureExecutionOrderProvider executionOrderProvider) {
        this.executorFactory = executorFactory;
        this.executionOrderProvider = executionOrderProvider;
    }

    public SelectablePlanFeaturesResponse getSelectableFeatures(Long planId) {
        PlanSelectableFeatureContext context = new PlanSelectableFeatureContext(planId);
        executionOrderProvider.getExecutionOrder()
                .forEach(step -> executorFactory.getExecutor(step).execute(context));
        return context.getResponse();
    }
}
