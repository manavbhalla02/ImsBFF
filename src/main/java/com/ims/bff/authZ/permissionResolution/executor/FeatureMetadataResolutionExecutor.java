package com.ims.bff.authZ.permissionResolution.executor;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ims.bff.authZ.permissionResolution.config.FeatureTypeProvider;
import com.ims.bff.authZ.permissionResolution.context.PermissionResolutionContext;
import com.ims.bff.authZ.permissionResolution.enums.PermissionResolutionStepType;
import com.ims.bff.common.executor.TypedExecutor;
import com.ims.bff.orgRegistration.planselection.config.FeatureGraphProvider;
import com.ims.bff.orgRegistration.planselection.entity.FeatureEntity;
import com.ims.bff.orgRegistration.planselection.repository.FeatureRepository;

@Component
public class FeatureMetadataResolutionExecutor implements
        TypedExecutor<PermissionResolutionStepType, Map<Long, FeatureEntity>, PermissionResolutionContext> {

    private final FeatureRepository featureRepository;
    private final FeatureGraphProvider featureGraphProvider;
    private final FeatureTypeProvider featureTypeProvider;

    public FeatureMetadataResolutionExecutor(
            FeatureRepository featureRepository,
            FeatureGraphProvider featureGraphProvider,
            FeatureTypeProvider featureTypeProvider) {
        this.featureRepository = featureRepository;
        this.featureGraphProvider = featureGraphProvider;
        this.featureTypeProvider = featureTypeProvider;
    }

    @Override
    public PermissionResolutionStepType supports() {
        return PermissionResolutionStepType.FEATURE_METADATA;
    }

    @Override
    public Map<Long, FeatureEntity> execute(PermissionResolutionContext context) {
        Map<String, List<String>> adjacency = featureGraphProvider.getAdjacency();
        context.setAdjacency(adjacency);
        context.setFeatureTypesByKey(featureTypeProvider.getFeatureTypes());

        Set<String> subtreeKeys = new LinkedHashSet<>();
        collectSubtreeKeys(context.getRootFeatureKey(), adjacency, subtreeKeys);
        context.setSubtreeFeatureKeys(subtreeKeys);

        List<FeatureEntity> features = featureRepository.findByFeatureKeyIn(subtreeKeys);
        Map<Long, FeatureEntity> featuresById = features.stream()
                .collect(Collectors.toMap(FeatureEntity::getFeatureId, Function.identity()));
        Map<String, FeatureEntity> featuresByKey = features.stream()
                .collect(Collectors.toMap(FeatureEntity::getFeatureKey, Function.identity()));

        if (!featuresByKey.containsKey(context.getRootFeatureKey())) {
            throw new IllegalArgumentException("Unknown root feature key " + context.getRootFeatureKey());
        }

        context.setFeaturesById(featuresById);
        context.setFeaturesByKey(featuresByKey);
        context.setSubtreeFeatureIds(features.stream()
                .map(FeatureEntity::getFeatureId)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new)));
        return featuresById;
    }

    private void collectSubtreeKeys(String featureKey, Map<String, List<String>> adjacency, Set<String> keys) {
        if (!keys.add(featureKey)) {
            return;
        }
        for (String childKey : adjacency.getOrDefault(featureKey, List.of())) {
            collectSubtreeKeys(childKey, adjacency, keys);
        }
    }
}
