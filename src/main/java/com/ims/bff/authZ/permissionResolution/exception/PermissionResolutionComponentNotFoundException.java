package com.ims.bff.authZ.permissionResolution.exception;

import com.ims.bff.authZ.permissionResolution.enums.PermissionResolutionStepType;

public class PermissionResolutionComponentNotFoundException extends RuntimeException {

    public PermissionResolutionComponentNotFoundException(String componentType, PermissionResolutionStepType stepType) {
        super(componentType + " not found for permission resolution step " + stepType);
    }
}
