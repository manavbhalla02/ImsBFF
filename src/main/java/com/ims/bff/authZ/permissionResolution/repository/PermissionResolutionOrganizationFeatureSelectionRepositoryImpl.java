package com.ims.bff.authZ.permissionResolution.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.ims.bff.orgRegistration.registration.entity.OrganizationFeatureSelectionEntity;
import com.ims.bff.orgRegistration.registration.enums.OrganizationFeatureSelectionStatus;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class PermissionResolutionOrganizationFeatureSelectionRepositoryImpl
        implements PermissionResolutionOrganizationFeatureSelectionRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private final boolean postgres;

    public PermissionResolutionOrganizationFeatureSelectionRepositoryImpl(
            @Value("${spring.datasource.url:}") String datasourceUrl) {
        this.postgres = datasourceUrl != null && datasourceUrl.contains("postgresql");
    }

    @Override
    public List<OrganizationFeatureSelectionEntity> findByOrganizationIdAndStatusAndFeatureIdIn(
            Long organizationId,
            OrganizationFeatureSelectionStatus status,
            Collection<Long> featureIds) {
        if (featureIds == null || featureIds.isEmpty()) {
            return List.of();
        }

        if (postgres) {
            return entityManager.createNativeQuery("""
                    select ofs.*
                    from organization_feature_selection ofs
                    where ofs.organization_id = :organizationId
                      and ofs.status = cast(:status as organization_feature_selection_status)
                      and ofs.feature_id in (:featureIds)
                    order by ofs.organization_feature_selection_id
                    """, OrganizationFeatureSelectionEntity.class)
                    .setParameter("organizationId", organizationId)
                    .setParameter("status", status.name())
                    .setParameter("featureIds", featureIds)
                    .getResultList();
        }

        return entityManager.createQuery("""
                select ofs
                from OrganizationFeatureSelectionEntity ofs
                where ofs.organization.organizationId = :organizationId
                  and ofs.status = :status
                  and ofs.featureId in :featureIds
                order by ofs.organizationFeatureSelectionId
                """, OrganizationFeatureSelectionEntity.class)
                .setParameter("organizationId", organizationId)
                .setParameter("status", status)
                .setParameter("featureIds", featureIds)
                .getResultList();
    }
}
