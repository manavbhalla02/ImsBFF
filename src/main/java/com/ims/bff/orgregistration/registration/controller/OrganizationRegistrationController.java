package com.ims.bff.orgregistration.registration.controller;

import static com.ims.bff.orgregistration.registration.RegistrationConstants.ORGANIZATION_REGISTRATION_PATH;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ims.bff.orgregistration.registration.dto.OrganizationRegistrationRequest;
import com.ims.bff.orgregistration.registration.dto.OrganizationRegistrationResponse;
import com.ims.bff.orgregistration.registration.service.OrganizationRegistrationApplicationService;

import jakarta.validation.Valid;

@RestController
public class OrganizationRegistrationController {

    private final OrganizationRegistrationApplicationService registrationService;

    public OrganizationRegistrationController(OrganizationRegistrationApplicationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping(ORGANIZATION_REGISTRATION_PATH)
    public ResponseEntity<OrganizationRegistrationResponse> registerOrganization(
            @Valid @RequestBody OrganizationRegistrationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(registrationService.register(request));
    }
}
