package com.ims.bff.planselection.factory;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ims.bff.common.executor.TypedExecutor;
import com.ims.bff.common.factory.AbstractComponentFactory;
import com.ims.bff.planselection.context.PlanSelectableFeatureContext;
import com.ims.bff.planselection.enums.PlanSelectableFeatureStepType;
import com.ims.bff.planselection.exception.PlanSelectableFeatureComponentNotFoundException;

@Component
public class PlanSelectableFeatureExecutorFactory extends AbstractComponentFactory<
        PlanSelectableFeatureStepType,
        TypedExecutor<PlanSelectableFeatureStepType, ?, PlanSelectableFeatureContext>> {

    public PlanSelectableFeatureExecutorFactory(
            List<TypedExecutor<PlanSelectableFeatureStepType, ?, PlanSelectableFeatureContext>> executors) {
        super(executors);
    }

    @SuppressWarnings("unchecked")
    public <T> TypedExecutor<PlanSelectableFeatureStepType, T, PlanSelectableFeatureContext> getExecutor(
            PlanSelectableFeatureStepType stepType) {
        return (TypedExecutor<PlanSelectableFeatureStepType, T, PlanSelectableFeatureContext>) getComponent(
                stepType,
                new PlanSelectableFeatureComponentNotFoundException("Executor", stepType));
    }
}
