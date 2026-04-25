package com.ims.bff.authZ.permissionResolution.service;

import org.springframework.stereotype.Service;

import com.ims.bff.authZ.permissionResolution.config.PermissionResolutionExecutionOrderProvider;
import com.ims.bff.authZ.permissionResolution.context.PermissionResolutionContext;
import com.ims.bff.authZ.permissionResolution.dto.PermissionResolutionRequest;
import com.ims.bff.authZ.permissionResolution.dto.PermissionResolutionResponse;
import com.ims.bff.authZ.permissionResolution.factory.PermissionResolutionExecutorFactory;

@Service
public class PermissionResolutionApplicationService {

    private final PermissionResolutionExecutorFactory executorFactory;
    private final PermissionResolutionExecutionOrderProvider executionOrderProvider;

    public PermissionResolutionApplicationService(
            PermissionResolutionExecutorFactory executorFactory,
            PermissionResolutionExecutionOrderProvider executionOrderProvider) {
        this.executorFactory = executorFactory;
        this.executionOrderProvider = executionOrderProvider;
    }

    public PermissionResolutionResponse resolve(PermissionResolutionRequest request) {
        PermissionResolutionContext context = new PermissionResolutionContext(request);
        executionOrderProvider.getExecutionOrder()
                .forEach(step -> executorFactory.getExecutor(step).execute(context));
        return context.getResponse();
    }
}
