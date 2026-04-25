package com.ims.bff.authZ.permissionResolution.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import org.hibernate.annotations.ColumnTransformer;

import com.ims.bff.authZ.permissionResolution.enums.OrganizationAccessMode;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
@Table(name = "organization_access_policy")
public class OrganizationAccessPolicyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "organization_access_policy_id")
    private Long organizationAccessPolicyId;

    @Column(name = "organization_id", nullable = false, unique = true)
    private Long organizationId;

    @Enumerated(EnumType.STRING)
    @ColumnTransformer(write = "cast(? as organization_access_mode)")
    @Column(name = "access_mode", nullable = false, columnDefinition = "organization_access_mode")
    private OrganizationAccessMode accessMode;

    @Column(name = "allow_manual_permission_assignment", nullable = false)
    private boolean allowManualPermissionAssignment;

    @Column(name = "allow_manual_resolver_assignment", nullable = false)
    private boolean allowManualResolverAssignment;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public Long getOrganizationAccessPolicyId() {
        return organizationAccessPolicyId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public OrganizationAccessMode getAccessMode() {
        return accessMode;
    }

    public void setAccessMode(OrganizationAccessMode accessMode) {
        this.accessMode = accessMode;
    }

    public boolean isAllowManualPermissionAssignment() {
        return allowManualPermissionAssignment;
    }

    public void setAllowManualPermissionAssignment(boolean allowManualPermissionAssignment) {
        this.allowManualPermissionAssignment = allowManualPermissionAssignment;
    }

    public boolean isAllowManualResolverAssignment() {
        return allowManualResolverAssignment;
    }

    public void setAllowManualResolverAssignment(boolean allowManualResolverAssignment) {
        this.allowManualResolverAssignment = allowManualResolverAssignment;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
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
