package com.ims.bff.authZ.orgDiscovery.service;

import org.springframework.stereotype.Service;

import com.ims.bff.authZ.orgDiscovery.context.OrgDiscoveryStateContext;
import com.ims.bff.authZ.orgDiscovery.dto.DiscoveredOrganizationResponse;
import com.ims.bff.authZ.orgDiscovery.dto.SendOtpRequest;
import com.ims.bff.authZ.orgDiscovery.dto.SendOtpResponse;
import com.ims.bff.authZ.orgDiscovery.dto.VerifyOtpRequest;
import com.ims.bff.authZ.orgDiscovery.dto.VerifyOtpResponse;
import com.ims.bff.authZ.orgDiscovery.enums.OrgDiscoveryState;
import com.ims.bff.authZ.orgDiscovery.exception.OrgDiscoverySessionNotFoundException;
import com.ims.bff.authZ.orgDiscovery.factory.OrgDiscoveryStateHandlerFactory;
import com.ims.bff.authZ.orgDiscovery.model.OrgDiscoverySession;
import com.ims.bff.authZ.orgDiscovery.store.OrgDiscoverySessionStore;

@Service
public class OrgDiscoveryApplicationService {

    private final OrgDiscoveryStateHandlerFactory stateHandlerFactory;
    private final OrgDiscoverySessionStore sessionStore;
    private final OrgDiscoveryDomainService domainService;

    public OrgDiscoveryApplicationService(
            OrgDiscoveryStateHandlerFactory stateHandlerFactory,
            OrgDiscoverySessionStore sessionStore,
            OrgDiscoveryDomainService domainService) {
        this.stateHandlerFactory = stateHandlerFactory;
        this.sessionStore = sessionStore;
        this.domainService = domainService;
    }

    public SendOtpResponse sendOtp(SendOtpRequest request) {
        OrgDiscoveryStateContext context = new OrgDiscoveryStateContext();
        context.setEmailAddress(request.getEmailAddress());
        context.setCurrentState(OrgDiscoveryState.EMAIL_CAPTURED);

        stateHandlerFactory.getHandler(context.getCurrentState()).handle(context);

        OrgDiscoverySession session = context.getSession();
        return new SendOtpResponse(
                session.getDiscoveryId(),
                session.getState(),
                session.getChannel(),
                domainService.maskEmail(session.getEmailAddress()),
                session.getOtpCode().length(),
                session.getExpiresAt());
    }

    public VerifyOtpResponse verifyOtp(VerifyOtpRequest request) {
        OrgDiscoverySession session = sessionStore.findById(request.getDiscoveryId())
                .orElseThrow(() -> new OrgDiscoverySessionNotFoundException(request.getDiscoveryId()));

        OrgDiscoveryStateContext context = new OrgDiscoveryStateContext();
        context.setSession(session);
        context.setOtp(request.getOtp());
        context.setCurrentState(session.getState());

        stateHandlerFactory.getHandler(context.getCurrentState()).handle(context);

        return new VerifyOtpResponse(
                session.getDiscoveryId(),
                context.getCurrentState(),
                new DiscoveredOrganizationResponse(
                        context.getOrganizationDomain().getOrganization().getOrganizationId(),
                        context.getOrganizationDomain().getOrganization().getOrgCode(),
                        context.getOrganizationDomain().getOrganization().getOrgName(),
                        context.getOrganizationDomain().getDomainName(),
                        session.getEmailAddress()));
    }
}
