package com.ims.bff.authZ.permissionResolution.executor;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.ims.bff.authZ.permissionResolution.config.FeatureTypeDefinition;
import com.ims.bff.authZ.permissionResolution.dto.FeatureActionResponse;
import com.ims.bff.authZ.permissionResolution.context.PermissionResolutionContext;
import com.ims.bff.authZ.permissionResolution.dto.FeatureRestrictionResponse;
import com.ims.bff.authZ.permissionResolution.dto.ResolvedFeatureNodeResponse;
import com.ims.bff.authZ.permissionResolution.enums.PermissionResolutionStepType;
import com.ims.bff.common.executor.TypedExecutor;
import com.ims.bff.orgRegistration.planselection.entity.FeatureEntity;

@Component
public class FeatureTreeBuildResolutionExecutor implements
        TypedExecutor<PermissionResolutionStepType, ResolvedFeatureNodeResponse, PermissionResolutionContext> {

    @Override
    public PermissionResolutionStepType supports() {
        return PermissionResolutionStepType.FEATURE_TREE_BUILD;
    }

    @Override
    public ResolvedFeatureNodeResponse execute(PermissionResolutionContext context) {
        ResolvedFeatureNodeResponse rootFeature = buildNode(context.getRootFeatureKey(), context, true, false);
        context.setRootFeature(rootFeature);
        return rootFeature;
    }

    private ResolvedFeatureNodeResponse buildNode(
            String featureKey,
            PermissionResolutionContext context,
            boolean root,
            boolean inheritedOrganizationWide) {
        FeatureEntity feature = context.getFeaturesByKey().get(featureKey);
        if (feature == null) {
            return null;
        }

        FeatureTypeDefinition featureTypeDefinition = context.getFeatureTypesByKey().get(featureKey);
        boolean organizationWide = inheritedOrganizationWide || isOrganizationWide(featureTypeDefinition);

        List<ResolvedFeatureNodeResponse> children = new ArrayList<>();
        for (String childKey : context.getAdjacency().getOrDefault(featureKey, List.of())) {
            ResolvedFeatureNodeResponse child = buildNode(childKey, context, false, organizationWide);
            if (child != null) {
                children.add(child);
            }
        }

        boolean entitled = context.hasEntitledFeature(featureKey);
        Set<String> resolvedActions = context.getActionsByFeatureKey() != null
                ? context.getActionsByFeatureKey().getOrDefault(featureKey, Set.of())
                : Set.of();
        Set<String> actions = resolveActions(featureTypeDefinition, organizationWide, resolvedActions);

        if (!entitled) {
            return null;
        }

        if (!root && !organizationWide && actions.isEmpty() && children.isEmpty()) {
            return null;
        }

        List<FeatureRestrictionResponse> restrictions = context.getRestrictionsByFeatureKey() != null
                ? context.getRestrictionsByFeatureKey().getOrDefault(featureKey, List.of())
                : List.of();
        FeatureActionResponse action = featureTypeDefinition != null && featureTypeDefinition.action() != null
                ? new FeatureActionResponse(
                        featureTypeDefinition.action().method(),
                        featureTypeDefinition.action().url())
                : null;

        return new ResolvedFeatureNodeResponse(
                feature.getFeatureId(),
                feature.getFeatureKey(),
                featureTypeDefinition != null && featureTypeDefinition.displayName() != null
                        ? featureTypeDefinition.displayName()
                        : feature.getFeatureName(),
                featureTypeDefinition != null ? featureTypeDefinition.type() : null,
                action,
                actions.stream().sorted().toList(),
                restrictions,
                List.copyOf(children));
    }

    private boolean isOrganizationWide(FeatureTypeDefinition featureTypeDefinition) {
        return featureTypeDefinition != null && Boolean.TRUE.equals(featureTypeDefinition.organizationWide());
    }

    private Set<String> resolveActions(
            FeatureTypeDefinition featureTypeDefinition,
            boolean organizationWide,
            Set<String> resolvedActions) {
        if (!organizationWide || featureTypeDefinition == null || featureTypeDefinition.defaultActions() == null
                || featureTypeDefinition.defaultActions().isEmpty()) {
            return resolvedActions;
        }

        Set<String> effectiveActions = new LinkedHashSet<>(featureTypeDefinition.defaultActions());
        effectiveActions.addAll(resolvedActions);
        return effectiveActions;
    }
}
