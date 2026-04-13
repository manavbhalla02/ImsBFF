package com.ims.bff.orgregistration.planselection.context;

import java.util.List;
import java.util.Map;

import com.ims.bff.orgregistration.planselection.dto.SelectablePlanFeaturesResponse;
import com.ims.bff.orgregistration.planselection.entity.FeatureEntity;
import com.ims.bff.orgregistration.planselection.entity.PlanFeatureEntity;
import com.ims.bff.orgregistration.planselection.entity.PlanFeatureSelectionRuleEntity;

public class PlanSelectableFeatureContext {

    private final Long planId;
    private List<PlanFeatureEntity> selectablePlanFeatures;
    private List<PlanFeatureSelectionRuleEntity> selectionRules;
    private Map<Long, PlanFeatureSelectionRuleEntity> rulesByFeatureId;
    private Map<String, List<String>> adjacency;
    private Map<Long, FeatureEntity> featuresById;
    private Map<String, FeatureEntity> featuresByKey;
    private SelectablePlanFeaturesResponse response;

    public PlanSelectableFeatureContext(Long planId) {
        this.planId = planId;
    }

    public Long getPlanId() {
        return planId;
    }

    public List<PlanFeatureEntity> getSelectablePlanFeatures() {
        return selectablePlanFeatures;
    }

    public void setSelectablePlanFeatures(List<PlanFeatureEntity> selectablePlanFeatures) {
        this.selectablePlanFeatures = selectablePlanFeatures;
    }

    public List<PlanFeatureSelectionRuleEntity> getSelectionRules() {
        return selectionRules;
    }

    public void setSelectionRules(List<PlanFeatureSelectionRuleEntity> selectionRules) {
        this.selectionRules = selectionRules;
    }

    public Map<Long, PlanFeatureSelectionRuleEntity> getRulesByFeatureId() {
        return rulesByFeatureId;
    }

    public void setRulesByFeatureId(Map<Long, PlanFeatureSelectionRuleEntity> rulesByFeatureId) {
        this.rulesByFeatureId = rulesByFeatureId;
    }

    public Map<String, List<String>> getAdjacency() {
        return adjacency;
    }

    public void setAdjacency(Map<String, List<String>> adjacency) {
        this.adjacency = adjacency;
    }

    public Map<Long, FeatureEntity> getFeaturesById() {
        return featuresById;
    }

    public void setFeaturesById(Map<Long, FeatureEntity> featuresById) {
        this.featuresById = featuresById;
    }

    public Map<String, FeatureEntity> getFeaturesByKey() {
        return featuresByKey;
    }

    public void setFeaturesByKey(Map<String, FeatureEntity> featuresByKey) {
        this.featuresByKey = featuresByKey;
    }

    public SelectablePlanFeaturesResponse getResponse() {
        return response;
    }

    public void setResponse(SelectablePlanFeaturesResponse response) {
        this.response = response;
    }
}
