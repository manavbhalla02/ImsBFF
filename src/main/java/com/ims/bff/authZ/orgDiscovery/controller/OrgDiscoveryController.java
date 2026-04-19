package com.ims.bff.authZ.orgDiscovery.controller;

import static com.ims.bff.authZ.orgDiscovery.AuthZOrgDiscoveryConstants.ORG_DISCOVERY_API_BASE_PATH;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ims.bff.authZ.orgDiscovery.dto.SendOtpRequest;
import com.ims.bff.authZ.orgDiscovery.dto.SendOtpResponse;
import com.ims.bff.authZ.orgDiscovery.dto.VerifyOtpRequest;
import com.ims.bff.authZ.orgDiscovery.dto.VerifyOtpResponse;
import com.ims.bff.authZ.orgDiscovery.service.OrgDiscoveryApplicationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(ORG_DISCOVERY_API_BASE_PATH)
public class OrgDiscoveryController {

    private final OrgDiscoveryApplicationService orgDiscoveryApplicationService;

    public OrgDiscoveryController(OrgDiscoveryApplicationService orgDiscoveryApplicationService) {
        this.orgDiscoveryApplicationService = orgDiscoveryApplicationService;
    }

    @PostMapping("/otp/send")
    public SendOtpResponse sendOtp(@Valid @RequestBody SendOtpRequest request) {
        return orgDiscoveryApplicationService.sendOtp(request);
    }

    @PostMapping("/otp/verify")
    public VerifyOtpResponse verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        return orgDiscoveryApplicationService.verifyOtp(request);
    }
}
