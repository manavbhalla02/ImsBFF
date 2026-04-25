package com.ims.bff.authZ.permissionResolution.factory;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ims.bff.authZ.permissionResolution.context.PermissionResolutionContext;
import com.ims.bff.authZ.permissionResolution.enums.PermissionResolutionStepType;
import com.ims.bff.authZ.permissionResolution.exception.PermissionResolutionComponentNotFoundException;
import com.ims.bff.common.executor.TypedExecutor;
import com.ims.bff.common.factory.AbstractComponentFactory;

@Component
public class PermissionResolutionExecutorFactory extends AbstractComponentFactory<
        PermissionResolutionStepType,
        TypedExecutor<PermissionResolutionStepType, ?, PermissionResolutionContext>> {

    public PermissionResolutionExecutorFactory(
            List<TypedExecutor<PermissionResolutionStepType, ?, PermissionResolutionContext>> executors) {
        super(executors);
    }

    @SuppressWarnings("unchecked")
    public <T> TypedExecutor<PermissionResolutionStepType, T, PermissionResolutionContext> getExecutor(
            PermissionResolutionStepType stepType) {
        return (TypedExecutor<PermissionResolutionStepType, T, PermissionResolutionContext>) getComponent(
                stepType,
                new PermissionResolutionComponentNotFoundException("Executor", stepType));
    }
}
