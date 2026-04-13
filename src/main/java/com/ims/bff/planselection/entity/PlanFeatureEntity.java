package com.ims.bff.planselection.entity;

import java.time.Instant;

import com.ims.bff.planselection.enums.FeatureGrantType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "plan_feature")
public class PlanFeatureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_feature_id")
    private Long planFeatureId;

    @Column(name = "plan_id", nullable = false)
    private Long planId;

    @Column(name = "feature_id", nullable = false)
    private Long featureId;

    @Enumerated(EnumType.STRING)
    @Column(name = "grant_type", nullable = false)
    private FeatureGrantType grantType;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public Long getPlanFeatureId() {
        return planFeatureId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Long getFeatureId() {
        return featureId;
    }

    public void setFeatureId(Long featureId) {
        this.featureId = featureId;
    }

    public FeatureGrantType getGrantType() {
        return grantType;
    }

    public void setGrantType(FeatureGrantType grantType) {
        this.grantType = grantType;
    }

    @PrePersist
    void onCreate() {
        this.createdAt = Instant.now();
    }
}
