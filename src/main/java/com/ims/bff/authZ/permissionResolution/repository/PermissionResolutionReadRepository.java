package com.ims.bff.authZ.permissionResolution.repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.ims.bff.authZ.permissionResolution.dto.FeatureRestrictionResponse;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class PermissionResolutionReadRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Set<Long> findManualPermissionGroupIds(Long employeeId) {
        List<?> rows = entityManager.createNativeQuery("""
                select epg.permission_group_id
                from employee_permission_group epg
                where epg.employee_id = :employeeId
                """)
                .setParameter("employeeId", employeeId)
                .getResultList();
        return toLongSet(rows);
    }

    public Set<Long> findLdapPermissionGroupIds(Long employeeId) {
        List<?> rows = entityManager.createNativeQuery("""
                select distinct lgpg.permission_group_id
                from employee_ldap_identity eli
                join ldap_user_group lug
                  on lug.ldap_user_id = eli.ldap_user_id
                join ldap_group_permission_group lgpg
                  on lgpg.ldap_group_id = lug.ldap_group_id
                where eli.employee_id = :employeeId
                """)
                .setParameter("employeeId", employeeId)
                .getResultList();
        return toLongSet(rows);
    }

    public Set<Long> findActiveExcludedPermissionGroupIds(Long employeeId, Instant now) {
        List<?> rows = entityManager.createNativeQuery("""
                select epge.permission_group_id
                from employee_permission_group_exclusion epge
                where epge.employee_id = :employeeId
                  and epge.status = 'ACTIVE'
                  and (epge.expires_at is null or epge.expires_at > :now)
                """)
                .setParameter("employeeId", employeeId)
                .setParameter("now", Timestamp.from(now))
                .getResultList();
        return toLongSet(rows);
    }

    public Map<String, Set<String>> findFeatureActionsByPermissionGroupIds(
            Collection<Long> permissionGroupIds,
            Collection<Long> featureIds) {
        if (permissionGroupIds == null || permissionGroupIds.isEmpty() || featureIds == null || featureIds.isEmpty()) {
            return Map.of();
        }

        @SuppressWarnings("unchecked")
        List<Object[]> rows = entityManager.createNativeQuery("""
                select f.feature_key, fp.permission_key
                from permission_group_feature_permission pgfp
                join feature_permission fp
                  on fp.feature_permission_id = pgfp.feature_permission_id
                join feature f
                  on f.feature_id = fp.feature_id
                where pgfp.permission_group_id in (:permissionGroupIds)
                  and fp.feature_id in (:featureIds)
                  and fp.is_active = true
                """)
                .setParameter("permissionGroupIds", permissionGroupIds)
                .setParameter("featureIds", featureIds)
                .getResultList();

        Map<String, Set<String>> actionsByFeatureKey = new HashMap<>();
        for (Object[] row : rows) {
            String featureKey = row[0] != null ? row[0].toString() : null;
            String permissionKey = row[1] != null ? row[1].toString() : null;
            if (featureKey == null || permissionKey == null) {
                continue;
            }
            actionsByFeatureKey.computeIfAbsent(featureKey, ignored -> new LinkedHashSet<>()).add(permissionKey);
        }
        return actionsByFeatureKey;
    }

    public Map<Long, List<FeatureRestrictionResponse>> findRestrictionsByPlanFeatureIds(Collection<Long> planFeatureIds) {
        if (planFeatureIds == null || planFeatureIds.isEmpty()) {
            return Map.of();
        }

        @SuppressWarnings("unchecked")
        List<Object[]> rows = entityManager.createNativeQuery("""
                select pfr.plan_feature_id,
                       pfr.restriction_key,
                       pfr.restriction_type,
                       pfr.restriction_operator,
                       pfr.restriction_value,
                       pfr.restriction_unit
                from plan_feature_restriction pfr
                where pfr.plan_feature_id in (:planFeatureIds)
                order by pfr.plan_feature_restriction_id
                """)
                .setParameter("planFeatureIds", planFeatureIds)
                .getResultList();

        Map<Long, List<FeatureRestrictionResponse>> restrictionsByPlanFeatureId = new HashMap<>();
        for (Object[] row : rows) {
            Long planFeatureId = row[0] != null ? ((Number) row[0]).longValue() : null;
            if (planFeatureId == null) {
                continue;
            }
            FeatureRestrictionResponse restriction = new FeatureRestrictionResponse(
                    row[1] != null ? row[1].toString() : null,
                    row[2] != null ? row[2].toString() : null,
                    row[3] != null ? row[3].toString() : null,
                    row[4] != null ? row[4].toString() : null,
                    row[5] != null ? row[5].toString() : null);
            restrictionsByPlanFeatureId.computeIfAbsent(planFeatureId, ignored -> new ArrayList<>()).add(restriction);
        }
        return restrictionsByPlanFeatureId;
    }

    private Set<Long> toLongSet(List<?> rows) {
        Set<Long> values = new LinkedHashSet<>();
        for (Object row : rows) {
            if (row instanceof Number number) {
                values.add(number.longValue());
            }
        }
        return values;
    }
}
