package com.ims.bff.authZ.permissionResolution.context;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ims.bff.authZ.permissionResolution.config.FeatureTypeDefinition;
import com.ims.bff.authZ.permissionResolution.dto.FeatureRestrictionResponse;
import com.ims.bff.authZ.permissionResolution.dto.PermissionResolutionRequest;
import com.ims.bff.authZ.permissionResolution.dto.PermissionResolutionResponse;
import com.ims.bff.authZ.permissionResolution.dto.ResolvedFeatureNodeResponse;
import com.ims.bff.authZ.permissionResolution.entity.OrganizationAccessPolicyEntity;
import com.ims.bff.orgRegistration.planselection.entity.FeatureEntity;
import com.ims.bff.orgRegistration.planselection.entity.PlanFeatureEntity;
import com.ims.bff.orgRegistration.registration.entity.OrganizationFeatureSelectionEntity;
import com.ims.bff.orgRegistration.registration.entity.SubscriptionEntity;

public class PermissionResolutionContext {

    private final PermissionResolutionRequest request;
    private OrganizationAccessPolicyEntity accessPolicy;
    private SubscriptionEntity activeSubscription;
    private Long activePlanId;
    private Map<String, List<String>> adjacency;
    private Map<String, FeatureTypeDefinition> featureTypesByKey;
    private Set<String> subtreeFeatureKeys;
    private Set<Long> subtreeFeatureIds;
    private Map<Long, FeatureEntity> featuresById;
    private Map<String, FeatureEntity> featuresByKey;
    private List<PlanFeatureEntity> planFeaturesInScope;
    private List<OrganizationFeatureSelectionEntity> activeSelectionsInScope;
    private Set<String> entitledFeatureKeys;
    private Set<Long> effectivePermissionGroupIds;
    private Map<String, Set<String>> actionsByFeatureKey;
    private Map<String, List<FeatureRestrictionResponse>> restrictionsByFeatureKey;
    private ResolvedFeatureNodeResponse rootFeature;
    private PermissionResolutionResponse response;

    public PermissionResolutionContext(PermissionResolutionRequest request) {
        this.request = request;
    }

    public PermissionResolutionRequest getRequest() {
        return request;
    }

    public Long getOrganizationId() {
        return request.organizationId();
    }

    public Long getEmployeeId() {
        return request.employeeId();
    }

    public String getRootFeatureKey() {
        return request.rootFeatureKey();
    }

    public OrganizationAccessPolicyEntity getAccessPolicy() {
        return accessPolicy;
    }

    public void setAccessPolicy(OrganizationAccessPolicyEntity accessPolicy) {
        this.accessPolicy = accessPolicy;
    }

    public SubscriptionEntity getActiveSubscription() {
        return activeSubscription;
    }

    public void setActiveSubscription(SubscriptionEntity activeSubscription) {
        this.activeSubscription = activeSubscription;
    }

    public Long getActivePlanId() {
        return activePlanId;
    }

    public void setActivePlanId(Long activePlanId) {
        this.activePlanId = activePlanId;
    }

    public Map<String, List<String>> getAdjacency() {
        return adjacency;
    }

    public void setAdjacency(Map<String, List<String>> adjacency) {
        this.adjacency = adjacency;
    }

    public Map<String, FeatureTypeDefinition> getFeatureTypesByKey() {
        return featureTypesByKey;
    }

    public void setFeatureTypesByKey(Map<String, FeatureTypeDefinition> featureTypesByKey) {
        this.featureTypesByKey = featureTypesByKey;
    }

    public Set<String> getSubtreeFeatureKeys() {
        return subtreeFeatureKeys;
    }

    public void setSubtreeFeatureKeys(Set<String> subtreeFeatureKeys) {
        this.subtreeFeatureKeys = subtreeFeatureKeys;
    }

    public Set<Long> getSubtreeFeatureIds() {
        return subtreeFeatureIds;
    }

    public void setSubtreeFeatureIds(Set<Long> subtreeFeatureIds) {
        this.subtreeFeatureIds = subtreeFeatureIds;
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

    public List<PlanFeatureEntity> getPlanFeaturesInScope() {
        return planFeaturesInScope;
    }

    public void setPlanFeaturesInScope(List<PlanFeatureEntity> planFeaturesInScope) {
        this.planFeaturesInScope = planFeaturesInScope;
    }

    public List<OrganizationFeatureSelectionEntity> getActiveSelectionsInScope() {
        return activeSelectionsInScope;
    }

    public void setActiveSelectionsInScope(List<OrganizationFeatureSelectionEntity> activeSelectionsInScope) {
        this.activeSelectionsInScope = activeSelectionsInScope;
    }

    public Set<String> getEntitledFeatureKeys() {
        return entitledFeatureKeys;
    }

    public void setEntitledFeatureKeys(Set<String> entitledFeatureKeys) {
        this.entitledFeatureKeys = entitledFeatureKeys;
    }

    public Set<Long> getEffectivePermissionGroupIds() {
        return effectivePermissionGroupIds;
    }

    public void setEffectivePermissionGroupIds(Set<Long> effectivePermissionGroupIds) {
        this.effectivePermissionGroupIds = effectivePermissionGroupIds;
    }

    public Map<String, Set<String>> getActionsByFeatureKey() {
        return actionsByFeatureKey;
    }

    public void setActionsByFeatureKey(Map<String, Set<String>> actionsByFeatureKey) {
        this.actionsByFeatureKey = actionsByFeatureKey;
    }

    public Map<String, List<FeatureRestrictionResponse>> getRestrictionsByFeatureKey() {
        return restrictionsByFeatureKey;
    }

    public void setRestrictionsByFeatureKey(Map<String, List<FeatureRestrictionResponse>> restrictionsByFeatureKey) {
        this.restrictionsByFeatureKey = restrictionsByFeatureKey;
    }

    public ResolvedFeatureNodeResponse getRootFeature() {
        return rootFeature;
    }

    public void setRootFeature(ResolvedFeatureNodeResponse rootFeature) {
        this.rootFeature = rootFeature;
    }

    public PermissionResolutionResponse getResponse() {
        return response;
    }

    public void setResponse(PermissionResolutionResponse response) {
        this.response = response;
    }

    public boolean hasSubtreeFeatureKeys() {
        return subtreeFeatureKeys != null && !subtreeFeatureKeys.isEmpty();
    }

    public boolean hasPlanFeaturesInScope() {
        return planFeaturesInScope != null && !planFeaturesInScope.isEmpty();
    }

    public boolean hasSubtreeFeatureIds() {
        return subtreeFeatureIds != null && !subtreeFeatureIds.isEmpty();
    }

    public boolean hasActionsForFeature(String featureKey) {
        return actionsByFeatureKey != null
                && actionsByFeatureKey.containsKey(featureKey)
                && !actionsByFeatureKey.get(featureKey).isEmpty();
    }

    public boolean hasEntitledFeature(String featureKey) {
        return entitledFeatureKeys != null && entitledFeatureKeys.contains(featureKey);
    }

    public boolean hasSelectionsInScope() {
        return activeSelectionsInScope != null && !activeSelectionsInScope.isEmpty();
    }

    public boolean hasPlanFeatureIds(Collection<Long> planFeatureIds) {
        return planFeatureIds != null && !planFeatureIds.isEmpty();
    }
}
