package com.ims.bff.authZ.permissionResolution.executor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ims.bff.authZ.permissionResolution.context.PermissionResolutionContext;
import com.ims.bff.authZ.permissionResolution.dto.FeatureRestrictionResponse;
import com.ims.bff.authZ.permissionResolution.enums.PermissionResolutionStepType;
import com.ims.bff.authZ.permissionResolution.repository.PermissionResolutionReadRepository;
import com.ims.bff.common.executor.TypedExecutor;
import com.ims.bff.orgRegistration.planselection.entity.FeatureEntity;
import com.ims.bff.orgRegistration.planselection.entity.PlanFeatureEntity;

@Component
public class FeatureRestrictionResolutionExecutor implements
        TypedExecutor<PermissionResolutionStepType, Map<String, List<FeatureRestrictionResponse>>, PermissionResolutionContext> {

    private final PermissionResolutionReadRepository readRepository;

    public FeatureRestrictionResolutionExecutor(PermissionResolutionReadRepository readRepository) {
        this.readRepository = readRepository;
    }

    @Override
    public PermissionResolutionStepType supports() {
        return PermissionResolutionStepType.FEATURE_RESTRICTION_RESOLUTION;
    }

    @Override
    public Map<String, List<FeatureRestrictionResponse>> execute(PermissionResolutionContext context) {
        if (!context.hasPlanFeaturesInScope()) {
            context.setRestrictionsByFeatureKey(Map.of());
            return Map.of();
        }

        Map<Long, List<FeatureRestrictionResponse>> restrictionsByPlanFeatureId = readRepository.findRestrictionsByPlanFeatureIds(
                context.getPlanFeaturesInScope().stream()
                        .map(PlanFeatureEntity::getPlanFeatureId)
                        .collect(Collectors.toSet()));

        Map<String, List<FeatureRestrictionResponse>> restrictionsByFeatureKey = new HashMap<>();
        for (PlanFeatureEntity planFeature : context.getPlanFeaturesInScope()) {
            FeatureEntity feature = context.getFeaturesById().get(planFeature.getFeatureId());
            if (feature == null || !context.hasEntitledFeature(feature.getFeatureKey())) {
                continue;
            }
            List<FeatureRestrictionResponse> restrictions = restrictionsByPlanFeatureId.get(planFeature.getPlanFeatureId());
            if (restrictions != null && !restrictions.isEmpty()) {
                restrictionsByFeatureKey.put(feature.getFeatureKey(), List.copyOf(restrictions));
            }
        }

        context.setRestrictionsByFeatureKey(restrictionsByFeatureKey);
        return restrictionsByFeatureKey;
    }
}
