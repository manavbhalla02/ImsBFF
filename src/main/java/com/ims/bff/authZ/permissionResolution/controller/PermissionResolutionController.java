package com.ims.bff.authZ.permissionResolution.controller;

import static com.ims.bff.authZ.permissionResolution.AuthZPermissionResolutionConstants.PERMISSION_RESOLUTION_API_BASE_PATH;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ims.bff.authZ.permissionResolution.dto.PermissionResolutionRequest;
import com.ims.bff.authZ.permissionResolution.dto.PermissionResolutionResponse;
import com.ims.bff.authZ.permissionResolution.service.PermissionResolutionApplicationService;

@RestController
@RequestMapping(PERMISSION_RESOLUTION_API_BASE_PATH)
public class PermissionResolutionController {

    private final PermissionResolutionApplicationService permissionResolutionApplicationService;

    public PermissionResolutionController(
            PermissionResolutionApplicationService permissionResolutionApplicationService) {
        this.permissionResolutionApplicationService = permissionResolutionApplicationService;
    }

    @PostMapping("/resolve")
    public PermissionResolutionResponse resolve(@Valid @RequestBody PermissionResolutionRequest request) {
        return permissionResolutionApplicationService.resolve(request);
    }
}
