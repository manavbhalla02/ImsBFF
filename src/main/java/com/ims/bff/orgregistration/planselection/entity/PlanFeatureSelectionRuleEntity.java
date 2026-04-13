package com.ims.bff.orgregistration.planselection.entity;

import java.time.Instant;

import com.ims.bff.orgregistration.planselection.enums.SelectionScope;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "plan_feature_selection_rule")
public class PlanFeatureSelectionRuleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_feature_selection_rule_id")
    private Long planFeatureSelectionRuleId;

    @Column(name = "plan_feature_id", nullable = false)
    private Long planFeatureId;

    @Column(name = "applies_to_feature_id", nullable = false)
    private Long appliesToFeatureId;

    @Enumerated(EnumType.STRING)
    @Column(name = "selection_scope", nullable = false)
    private SelectionScope selectionScope;

    @Column(name = "min_selectable", nullable = false)
    private Integer minSelectable;

    @Column(name = "max_selectable", nullable = false)
    private Integer maxSelectable;

    @Column(name = "selection_required", nullable = false)
    private boolean selectionRequired;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public Long getPlanFeatureSelectionRuleId() {
        return planFeatureSelectionRuleId;
    }

    public Long getPlanFeatureId() {
        return planFeatureId;
    }

    public void setPlanFeatureId(Long planFeatureId) {
        this.planFeatureId = planFeatureId;
    }

    public Long getAppliesToFeatureId() {
        return appliesToFeatureId;
    }

    public void setAppliesToFeatureId(Long appliesToFeatureId) {
        this.appliesToFeatureId = appliesToFeatureId;
    }

    public SelectionScope getSelectionScope() {
        return selectionScope;
    }

    public void setSelectionScope(SelectionScope selectionScope) {
        this.selectionScope = selectionScope;
    }

    public Integer getMinSelectable() {
        return minSelectable;
    }

    public void setMinSelectable(Integer minSelectable) {
        this.minSelectable = minSelectable;
    }

    public Integer getMaxSelectable() {
        return maxSelectable;
    }

    public void setMaxSelectable(Integer maxSelectable) {
        this.maxSelectable = maxSelectable;
    }

    public boolean isSelectionRequired() {
        return selectionRequired;
    }

    public void setSelectionRequired(boolean selectionRequired) {
        this.selectionRequired = selectionRequired;
    }

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
