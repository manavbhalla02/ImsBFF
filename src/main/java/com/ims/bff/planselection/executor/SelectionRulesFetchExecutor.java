package com.ims.bff.planselection.executor;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ims.bff.common.executor.TypedExecutor;
import com.ims.bff.planselection.context.PlanSelectableFeatureContext;
import com.ims.bff.planselection.enums.PlanSelectableFeatureStepType;
import com.ims.bff.planselection.entity.PlanFeatureEntity;
import com.ims.bff.planselection.entity.PlanFeatureSelectionRuleEntity;
import com.ims.bff.planselection.repository.PlanFeatureSelectionRuleRepository;

@Component
public class SelectionRulesFetchExecutor implements
        TypedExecutor<PlanSelectableFeatureStepType, List<PlanFeatureSelectionRuleEntity>, PlanSelectableFeatureContext> {

    private final PlanFeatureSelectionRuleRepository planFeatureSelectionRuleRepository;

    public SelectionRulesFetchExecutor(PlanFeatureSelectionRuleRepository planFeatureSelectionRuleRepository) {
        this.planFeatureSelectionRuleRepository = planFeatureSelectionRuleRepository;
    }

    @Override
    public PlanSelectableFeatureStepType supports() {
        return PlanSelectableFeatureStepType.SELECTION_RULES;
    }

    @Override
    public List<PlanFeatureSelectionRuleEntity> execute(PlanSelectableFeatureContext context) {
        List<Long> planFeatureIds = context.getSelectablePlanFeatures().stream()
                .map(PlanFeatureEntity::getPlanFeatureId)
                .toList();
        List<PlanFeatureSelectionRuleEntity> selectionRules = planFeatureIds.isEmpty()
                ? List.of()
                : planFeatureSelectionRuleRepository.findByPlanFeatureIdIn(planFeatureIds);
        context.setSelectionRules(selectionRules);
        return selectionRules;
    }
}
