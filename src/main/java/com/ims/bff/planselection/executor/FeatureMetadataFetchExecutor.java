package com.ims.bff.planselection.executor;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ims.bff.common.executor.TypedExecutor;
import com.ims.bff.planselection.config.FeatureGraphProvider;
import com.ims.bff.planselection.context.PlanSelectableFeatureContext;
import com.ims.bff.planselection.enums.PlanSelectableFeatureStepType;
import com.ims.bff.planselection.entity.FeatureEntity;
import com.ims.bff.planselection.entity.PlanFeatureSelectionRuleEntity;
import com.ims.bff.planselection.repository.FeatureRepository;

@Component
public class FeatureMetadataFetchExecutor implements
        TypedExecutor<PlanSelectableFeatureStepType, Map<Long, FeatureEntity>, PlanSelectableFeatureContext> {

    private final FeatureRepository featureRepository;
    private final FeatureGraphProvider featureGraphProvider;

    public FeatureMetadataFetchExecutor(
            FeatureRepository featureRepository,
            FeatureGraphProvider featureGraphProvider) {
        this.featureRepository = featureRepository;
        this.featureGraphProvider = featureGraphProvider;
    }

    @Override
    public PlanSelectableFeatureStepType supports() {
        return PlanSelectableFeatureStepType.FEATURE_METADATA;
    }

    @Override
    public Map<Long, FeatureEntity> execute(PlanSelectableFeatureContext context) {
        Map<String, List<String>> adjacency = featureGraphProvider.getAdjacency();
        context.setAdjacency(adjacency);
        context.setRulesByFeatureId(context.getSelectionRules().stream()
                .collect(Collectors.toMap(
                        PlanFeatureSelectionRuleEntity::getAppliesToFeatureId,
                        Function.identity(),
                        (left, right) -> left)));

        Set<String> requiredFeatureKeys = new HashSet<>();

        Set<Long> rootFeatureIds = context.getSelectablePlanFeatures().stream()
                .map(planFeature -> planFeature.getFeatureId())
                .collect(Collectors.toSet());

        Set<Long> ruleFeatureIds = context.getSelectionRules().stream()
                .map(rule -> rule.getAppliesToFeatureId())
                .collect(Collectors.toSet());

        Set<Long> seedFeatureIds = new HashSet<>();
        seedFeatureIds.addAll(rootFeatureIds);
        seedFeatureIds.addAll(ruleFeatureIds);

        Map<Long, FeatureEntity> seedFeaturesById = featureRepository.findAllById(seedFeatureIds).stream()
                .collect(Collectors.toMap(FeatureEntity::getFeatureId, Function.identity()));

        seedFeaturesById.values().forEach(feature -> collectSubtreeKeys(feature.getFeatureKey(), adjacency, requiredFeatureKeys));

        List<FeatureEntity> features = featureRepository.findByFeatureKeyIn(requiredFeatureKeys);
        Map<Long, FeatureEntity> featuresById = features.stream()
                .collect(Collectors.toMap(FeatureEntity::getFeatureId, Function.identity()));
        Map<String, FeatureEntity> featuresByKey = features.stream()
                .collect(Collectors.toMap(FeatureEntity::getFeatureKey, Function.identity()));

        context.setFeaturesById(featuresById);
        context.setFeaturesByKey(featuresByKey);
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
