package com.ims.bff.authZ.permissionResolution.executor;

import org.springframework.stereotype.Component;

import com.ims.bff.authZ.permissionResolution.context.PermissionResolutionContext;
import com.ims.bff.authZ.permissionResolution.entity.OrganizationAccessPolicyEntity;
import com.ims.bff.authZ.permissionResolution.enums.OrganizationAccessMode;
import com.ims.bff.authZ.permissionResolution.enums.PermissionResolutionStepType;
import com.ims.bff.authZ.permissionResolution.repository.OrganizationAccessPolicyRepository;
import com.ims.bff.authZ.permissionResolution.repository.PermissionResolutionSubscriptionRepository;
import com.ims.bff.common.executor.TypedExecutor;
import com.ims.bff.orgRegistration.registration.enums.SubscriptionStatus;

@Component
public class OrgAccessContextResolutionExecutor implements
        TypedExecutor<PermissionResolutionStepType, Void, PermissionResolutionContext> {

    private final OrganizationAccessPolicyRepository organizationAccessPolicyRepository;
    private final PermissionResolutionSubscriptionRepository subscriptionRepository;

    public OrgAccessContextResolutionExecutor(
            OrganizationAccessPolicyRepository organizationAccessPolicyRepository,
            PermissionResolutionSubscriptionRepository subscriptionRepository) {
        this.organizationAccessPolicyRepository = organizationAccessPolicyRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public PermissionResolutionStepType supports() {
        return PermissionResolutionStepType.ORG_ACCESS_CONTEXT;
    }

    @Override
    public Void execute(PermissionResolutionContext context) {
        OrganizationAccessPolicyEntity accessPolicy = organizationAccessPolicyRepository.findByOrganizationId(
                context.getOrganizationId()).orElse(null);

        context.setAccessPolicy(accessPolicy);
        var subscription = subscriptionRepository.findLatestByOrganizationIdAndStatus(
                context.getOrganizationId(),
                SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new IllegalStateException(
                        "No active subscription found for organization " + context.getOrganizationId()));

        context.setActiveSubscription(subscription);
        context.setActivePlanId(subscription.getPlanId());

        if (accessPolicy == null) {
            context.setAccessPolicy(createDefaultInternalPolicy(context.getOrganizationId()));
        }
        return null;
    }

    private OrganizationAccessPolicyEntity createDefaultInternalPolicy(Long organizationId) {
        OrganizationAccessPolicyEntity policy = new OrganizationAccessPolicyEntity();
        policy.setOrganizationId(organizationId);
        policy.setAccessMode(OrganizationAccessMode.INTERNAL);
        policy.setAllowManualPermissionAssignment(true);
        policy.setAllowManualResolverAssignment(true);
        return policy;
    }
}
