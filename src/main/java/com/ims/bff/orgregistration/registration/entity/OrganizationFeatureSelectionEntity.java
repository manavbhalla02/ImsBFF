package com.ims.bff.orgregistration.registration.entity;

import java.time.Instant;

import com.ims.bff.orgregistration.registration.enums.OrganizationFeatureSelectionStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "organization_feature_selection")
public class OrganizationFeatureSelectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "organization_feature_selection_id")
    private Long organizationFeatureSelectionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    private OrganizationEntity organization;

    @Column(name = "feature_id", nullable = false)
    private Long featureId;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "organization_feature_selection_status")
    private OrganizationFeatureSelectionStatus status;

    @Column(name = "selected_at", nullable = false, updatable = false)
    private Instant selectedAt;

    public Long getOrganizationFeatureSelectionId() {
        return organizationFeatureSelectionId;
    }

    public OrganizationEntity getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationEntity organization) {
        this.organization = organization;
    }

    public Long getFeatureId() {
        return featureId;
    }

    public void setFeatureId(Long featureId) {
        this.featureId = featureId;
    }

    public OrganizationFeatureSelectionStatus getStatus() {
        return status;
    }

    public void setStatus(OrganizationFeatureSelectionStatus status) {
        this.status = status;
    }

    public Instant getSelectedAt() {
        return selectedAt;
    }

    @PrePersist
    void onCreate() {
        this.selectedAt = Instant.now();
    }
}
