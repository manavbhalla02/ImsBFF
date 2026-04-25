package com.ims.bff.authZ.permissionResolution.executor;

import org.springframework.stereotype.Component;

import com.ims.bff.authZ.permissionResolution.context.PermissionResolutionContext;
import com.ims.bff.authZ.permissionResolution.dto.PermissionResolutionResponse;
import com.ims.bff.authZ.permissionResolution.enums.PermissionResolutionStepType;
import com.ims.bff.common.executor.TypedExecutor;

@Component
public class PermissionResolutionResponseBuildExecutor implements
        TypedExecutor<PermissionResolutionStepType, PermissionResolutionResponse, PermissionResolutionContext> {

    @Override
    public PermissionResolutionStepType supports() {
        return PermissionResolutionStepType.RESPONSE_BUILD;
    }

    @Override
    public PermissionResolutionResponse execute(PermissionResolutionContext context) {
        PermissionResolutionResponse response = new PermissionResolutionResponse(
                context.getOrganizationId(),
                context.getEmployeeId(),
                context.getRequest().stage(),
                context.getRootFeature());
        context.setResponse(response);
        return response;
    }
}
