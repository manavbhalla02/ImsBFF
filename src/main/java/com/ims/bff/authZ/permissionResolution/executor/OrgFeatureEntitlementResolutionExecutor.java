package com.ims.bff.authZ.permissionResolution.executor;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.ims.bff.authZ.permissionResolution.context.PermissionResolutionContext;
import com.ims.bff.authZ.permissionResolution.enums.PermissionResolutionStepType;
import com.ims.bff.authZ.permissionResolution.repository.PermissionResolutionOrganizationFeatureSelectionRepository;
import com.ims.bff.authZ.permissionResolution.repository.PermissionResolutionPlanFeatureRepository;
import com.ims.bff.common.executor.TypedExecutor;
import com.ims.bff.orgRegistration.planselection.entity.FeatureEntity;
import com.ims.bff.orgRegistration.planselection.entity.PlanFeatureEntity;
import com.ims.bff.orgRegistration.planselection.enums.FeatureGrantType;
import com.ims.bff.orgRegistration.registration.entity.OrganizationFeatureSelectionEntity;
import com.ims.bff.orgRegistration.registration.enums.OrganizationFeatureSelectionStatus;

@Component
public class OrgFeatureEntitlementResolutionExecutor implements
        TypedExecutor<PermissionResolutionStepType, Set<String>, PermissionResolutionContext> {

    private final PermissionResolutionPlanFeatureRepository planFeatureRepository;
    private final PermissionResolutionOrganizationFeatureSelectionRepository organizationFeatureSelectionRepository;

    public OrgFeatureEntitlementResolutionExecutor(
            PermissionResolutionPlanFeatureRepository planFeatureRepository,
            PermissionResolutionOrganizationFeatureSelectionRepository organizationFeatureSelectionRepository) {
        this.planFeatureRepository = planFeatureRepository;
        this.organizationFeatureSelectionRepository = organizationFeatureSelectionRepository;
    }

    @Override
    public PermissionResolutionStepType supports() {
        return PermissionResolutionStepType.ORG_FEATURE_ENTITLEMENT;
    }

    @Override
    public Set<String> execute(PermissionResolutionContext context) {
        List<PlanFeatureEntity> planFeatures = planFeatureRepository.findByPlanIdAndFeatureIdIn(
                context.getActivePlanId(),
                context.getSubtreeFeatureIds());
        context.setPlanFeaturesInScope(planFeatures);

        List<OrganizationFeatureSelectionEntity> activeSelections = organizationFeatureSelectionRepository
                .findByOrganizationIdAndStatusAndFeatureIdIn(
                        context.getOrganizationId(),
                        OrganizationFeatureSelectionStatus.ACTIVE,
                        context.getSubtreeFeatureIds());
        context.setActiveSelectionsInScope(activeSelections);

        Set<String> entitledFeatureKeys = new LinkedHashSet<>();
        Set<String> selectableAnchorKeys = new LinkedHashSet<>();

        for (PlanFeatureEntity planFeature : planFeatures) {
            FeatureEntity feature = context.getFeaturesById().get(planFeature.getFeatureId());
            if (feature == null) {
                continue;
            }
            if (planFeature.getGrantType() == FeatureGrantType.DIRECT) {
                collectSubtreeKeys(feature.getFeatureKey(), context.getAdjacency(), entitledFeatureKeys);
            } else if (planFeature.getGrantType() == FeatureGrantType.SELECTABLE) {
                selectableAnchorKeys.add(feature.getFeatureKey());
            }
        }

        Map<String, String> parentByChild = buildParentLookup(context.getAdjacency());
        for (OrganizationFeatureSelectionEntity selection : activeSelections) {
            FeatureEntity selectedFeature = context.getFeaturesById().get(selection.getFeatureId());
            if (selectedFeature == null) {
                continue;
            }
            String currentKey = selectedFeature.getFeatureKey();
            while (currentKey != null && context.getSubtreeFeatureKeys().contains(currentKey)) {
                entitledFeatureKeys.add(currentKey);
                if (selectableAnchorKeys.contains(currentKey)) {
                    break;
                }
                currentKey = parentByChild.get(currentKey);
            }
        }

        if (entitledFeatureKeys.isEmpty()) {
            throw new IllegalStateException(
                    "Organization " + context.getOrganizationId()
                            + " is not entitled to root feature " + context.getRootFeatureKey());
        }

        context.setEntitledFeatureKeys(entitledFeatureKeys);
        return entitledFeatureKeys;
    }

    private void collectSubtreeKeys(
            String featureKey,
            Map<String, List<String>> adjacency,
            Set<String> entitledFeatureKeys) {
        if (!entitledFeatureKeys.add(featureKey)) {
            return;
        }
        for (String childKey : adjacency.getOrDefault(featureKey, List.of())) {
            collectSubtreeKeys(childKey, adjacency, entitledFeatureKeys);
        }
    }

    private Map<String, String> buildParentLookup(Map<String, List<String>> adjacency) {
        Map<String, String> parentByChild = new HashMap<>();
        adjacency.forEach((parentKey, children) -> children.stream()
                .filter(Objects::nonNull)
                .forEach(childKey -> parentByChild.putIfAbsent(childKey, parentKey)));
        return parentByChild;
    }
}
