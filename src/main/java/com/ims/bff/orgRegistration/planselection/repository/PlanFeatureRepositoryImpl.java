package com.ims.bff.orgRegistration.planselection.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.ims.bff.orgRegistration.planselection.entity.PlanFeatureEntity;
import com.ims.bff.orgRegistration.planselection.enums.FeatureGrantType;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class PlanFeatureRepositoryImpl implements PlanFeatureRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private final boolean postgres;

    public PlanFeatureRepositoryImpl(@Value("${spring.datasource.url:}") String datasourceUrl) {
        this.postgres = datasourceUrl != null && datasourceUrl.contains("postgresql");
    }

    @Override
    public List<PlanFeatureEntity> findByPlanIdAndGrantTypeOrdered(Long planId, FeatureGrantType grantType) {
        if (postgres) {
            return entityManager.createNativeQuery("""
                    select pfe.*
                    from plan_feature pfe
                    where pfe.plan_id = :planId
                      and pfe.grant_type = cast(:grantType as feature_grant_type)
                    order by pfe.plan_feature_id
                    """, PlanFeatureEntity.class)
                    .setParameter("planId", planId)
                    .setParameter("grantType", grantType.name())
                    .getResultList();
        }

        return entityManager.createQuery("""
                select p
                from PlanFeatureEntity p
                where p.planId = :planId
                  and p.grantType = :grantType
                order by p.planFeatureId
                """, PlanFeatureEntity.class)
                .setParameter("planId", planId)
                .setParameter("grantType", grantType)
                .getResultList();
    }
}
