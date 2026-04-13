package com.ims.bff.planselection.executor;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ims.bff.common.executor.TypedExecutor;
import com.ims.bff.planselection.context.PlanSelectableFeatureContext;
import com.ims.bff.planselection.enums.PlanSelectableFeatureStepType;
import com.ims.bff.planselection.enums.FeatureGrantType;
import com.ims.bff.planselection.entity.PlanFeatureEntity;
import com.ims.bff.planselection.repository.PlanFeatureRepository;

@Component
public class SelectablePlanFeaturesFetchExecutor implements
        TypedExecutor<PlanSelectableFeatureStepType, List<PlanFeatureEntity>, PlanSelectableFeatureContext> {

    private final PlanFeatureRepository planFeatureRepository;

    public SelectablePlanFeaturesFetchExecutor(PlanFeatureRepository planFeatureRepository) {
        this.planFeatureRepository = planFeatureRepository;
    }

    @Override
    public PlanSelectableFeatureStepType supports() {
        return PlanSelectableFeatureStepType.SELECTABLE_PLAN_FEATURES;
    }

    @Override
    public List<PlanFeatureEntity> execute(PlanSelectableFeatureContext context) {
        List<PlanFeatureEntity> planFeatures =
                planFeatureRepository.findByPlanIdAndGrantTypeOrdered(
                        context.getPlanId(),
                        FeatureGrantType.SELECTABLE);
        context.setSelectablePlanFeatures(planFeatures);
        return planFeatures;
    }
}
