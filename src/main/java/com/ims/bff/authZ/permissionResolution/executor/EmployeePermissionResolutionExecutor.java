package com.ims.bff.authZ.permissionResolution.executor;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.ims.bff.authZ.permissionResolution.context.PermissionResolutionContext;
import com.ims.bff.authZ.permissionResolution.enums.OrganizationAccessMode;
import com.ims.bff.authZ.permissionResolution.enums.PermissionResolutionStepType;
import com.ims.bff.authZ.permissionResolution.repository.PermissionResolutionReadRepository;
import com.ims.bff.common.executor.TypedExecutor;

@Component
public class EmployeePermissionResolutionExecutor implements
        TypedExecutor<PermissionResolutionStepType, Map<String, Set<String>>, PermissionResolutionContext> {

    private final PermissionResolutionReadRepository readRepository;

    public EmployeePermissionResolutionExecutor(PermissionResolutionReadRepository readRepository) {
        this.readRepository = readRepository;
    }

    @Override
    public PermissionResolutionStepType supports() {
        return PermissionResolutionStepType.EMPLOYEE_PERMISSION_RESOLUTION;
    }

    @Override
    public Map<String, Set<String>> execute(PermissionResolutionContext context) {
        Set<Long> manualPermissionGroupIds = readRepository.findManualPermissionGroupIds(context.getEmployeeId());
        Set<Long> ldapPermissionGroupIds = readRepository.findLdapPermissionGroupIds(context.getEmployeeId());
        Set<Long> effectivePermissionGroupIds = new LinkedHashSet<>();

        OrganizationAccessMode accessMode = context.getAccessPolicy().getAccessMode();
        switch (accessMode) {
            case INTERNAL -> effectivePermissionGroupIds.addAll(manualPermissionGroupIds);
            case LDAP -> effectivePermissionGroupIds.addAll(ldapPermissionGroupIds);
            case HYBRID -> {
                effectivePermissionGroupIds.addAll(manualPermissionGroupIds);
                effectivePermissionGroupIds.addAll(ldapPermissionGroupIds);
            }
        }

        if (accessMode != OrganizationAccessMode.INTERNAL) {
            Set<Long> excludedPermissionGroupIds = readRepository.findActiveExcludedPermissionGroupIds(
                    context.getEmployeeId(),
                    Instant.now());
            effectivePermissionGroupIds.removeAll(excludedPermissionGroupIds);
        }

        context.setEffectivePermissionGroupIds(effectivePermissionGroupIds);
        Map<String, Set<String>> actionsByFeatureKey = readRepository.findFeatureActionsByPermissionGroupIds(
                effectivePermissionGroupIds,
                context.getSubtreeFeatureIds());
        context.setActionsByFeatureKey(actionsByFeatureKey);
        return actionsByFeatureKey;
    }
}
