package com.ims.bff.planselection.executor;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.ims.bff.common.executor.TypedExecutor;
import com.ims.bff.planselection.context.PlanSelectableFeatureContext;
import com.ims.bff.planselection.dto.SelectableFeatureNodeResponse;
import com.ims.bff.planselection.dto.SelectablePlanFeaturesResponse;
import com.ims.bff.planselection.entity.FeatureEntity;
import com.ims.bff.planselection.entity.PlanFeatureEntity;
import com.ims.bff.planselection.entity.PlanFeatureSelectionRuleEntity;
import com.ims.bff.planselection.enums.PlanSelectableFeatureStepType;

@Component
public class SelectablePlanFeatureResponseBuildExecutor implements
        TypedExecutor<PlanSelectableFeatureStepType, SelectablePlanFeaturesResponse, PlanSelectableFeatureContext> {

    @Override
    public PlanSelectableFeatureStepType supports() {
        return PlanSelectableFeatureStepType.RESPONSE_BUILD;
    }

    @Override
    public SelectablePlanFeaturesResponse execute(PlanSelectableFeatureContext context) {
        List<SelectableFeatureNodeResponse> rootNodes = context.getSelectablePlanFeatures().stream()
                .map(PlanFeatureEntity::getFeatureId)
                .map(context.getFeaturesById()::get)
                .filter(Objects::nonNull)
                .map(feature -> buildNode(feature.getFeatureKey(), context))
                .toList();

        SelectablePlanFeaturesResponse response = new SelectablePlanFeaturesResponse(context.getPlanId(), rootNodes);
        context.setResponse(response);
        return response;
    }

    private SelectableFeatureNodeResponse buildNode(
            String featureKey,
            PlanSelectableFeatureContext context) {
        FeatureEntity feature = context.getFeaturesByKey().get(featureKey);
        if (feature == null) {
            return null;
        }

        PlanFeatureSelectionRuleEntity rule = context.getRulesByFeatureId().get(feature.getFeatureId());
        List<SelectableFeatureNodeResponse> children = context.getAdjacency().getOrDefault(featureKey, List.of()).stream()
                .map(childKey -> buildNode(childKey, context))
                .filter(Objects::nonNull)
                .toList();

        return new SelectableFeatureNodeResponse(
                feature.getFeatureId(),
                feature.getFeatureKey(),
                feature.getFeatureName(),
                rule != null ? rule.getSelectionScope() : null,
                rule != null ? rule.getMinSelectable() : null,
                rule != null ? rule.getMaxSelectable() : null,
                rule != null ? rule.isSelectionRequired() : null,
                children);
    }
}
