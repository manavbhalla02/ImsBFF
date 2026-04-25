package com.ims.bff.authZ.permissionResolution.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.ims.bff.orgRegistration.registration.entity.SubscriptionEntity;
import com.ims.bff.orgRegistration.registration.enums.SubscriptionStatus;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class PermissionResolutionSubscriptionRepositoryImpl implements PermissionResolutionSubscriptionRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private final boolean postgres;

    public PermissionResolutionSubscriptionRepositoryImpl(@Value("${spring.datasource.url:}") String datasourceUrl) {
        this.postgres = datasourceUrl != null && datasourceUrl.contains("postgresql");
    }

    @Override
    public Optional<SubscriptionEntity> findLatestByOrganizationIdAndStatus(Long organizationId, SubscriptionStatus status) {
        List<SubscriptionEntity> subscriptions;
        if (postgres) {
            subscriptions = entityManager.createNativeQuery("""
                    select s.*
                    from subscription s
                    where s.organization_id = :organizationId
                      and s.status = cast(:status as subscription_status)
                    order by s.start_at desc
                    limit 1
                    """, SubscriptionEntity.class)
                    .setParameter("organizationId", organizationId)
                    .setParameter("status", status.name())
                    .getResultList();
        } else {
            subscriptions = entityManager.createQuery("""
                    select s
                    from SubscriptionEntity s
                    where s.organization.organizationId = :organizationId
                      and s.status = :status
                    order by s.startAt desc
                    """, SubscriptionEntity.class)
                    .setParameter("organizationId", organizationId)
                    .setParameter("status", status)
                    .setMaxResults(1)
                    .getResultList();
        }

        return subscriptions.stream().findFirst();
    }
}
