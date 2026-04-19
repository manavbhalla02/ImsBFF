package com.ims.bff.authZ.orgDiscovery.state.impl;

import java.time.Clock;
import java.time.Instant;

import org.springframework.stereotype.Component;

import com.ims.bff.authZ.orgDiscovery.context.OrgDiscoveryStateContext;
import com.ims.bff.authZ.orgDiscovery.enums.OrgDiscoveryState;
import com.ims.bff.authZ.orgDiscovery.exception.OrganizationNotFoundForEmailException;
import com.ims.bff.authZ.orgDiscovery.exception.OtpExpiredException;
import com.ims.bff.authZ.orgDiscovery.exception.OtpVerificationFailedException;
import com.ims.bff.authZ.orgDiscovery.state.OrgDiscoveryStateHandler;
import com.ims.bff.authZ.orgDiscovery.store.OrgDiscoverySessionStore;
import com.ims.bff.orgRegistration.registration.entity.OrganizationDomainEntity;
import com.ims.bff.orgRegistration.registration.repository.OrganizationDomainRepository;

@Component
public class OtpSentStateHandler implements OrgDiscoveryStateHandler {

    private final OrganizationDomainRepository organizationDomainRepository;
    private final OrgDiscoverySessionStore sessionStore;
    private final Clock clock;

    public OtpSentStateHandler(
            OrganizationDomainRepository organizationDomainRepository,
            OrgDiscoverySessionStore sessionStore,
            Clock clock) {
        this.organizationDomainRepository = organizationDomainRepository;
        this.sessionStore = sessionStore;
        this.clock = clock;
    }

    @Override
    public OrgDiscoveryState supports() {
        return OrgDiscoveryState.OTP_SENT;
    }

    @Override
    public void handle(OrgDiscoveryStateContext context) {
        if (context.getSession().getExpiresAt().isBefore(Instant.now(clock))) {
            context.getSession().setState(OrgDiscoveryState.EXPIRED);
            sessionStore.save(context.getSession());
            throw new OtpExpiredException();
        }

        if (!context.getSession().getOtpCode().equals(context.getOtp())) {
            context.getSession().setState(OrgDiscoveryState.FAILED);
            sessionStore.save(context.getSession());
            throw new OtpVerificationFailedException();
        }

        OrganizationDomainEntity organizationDomain = organizationDomainRepository
                .findByDomainNameIgnoreCase(context.getSession().getDomainName())
                .orElseThrow(() -> new OrganizationNotFoundForEmailException(context.getSession().getDomainName()));

        context.getSession().setState(OrgDiscoveryState.ORGANIZATION_RESOLVED);
        sessionStore.save(context.getSession());
        context.setOrganizationDomain(organizationDomain);
        context.setCurrentState(OrgDiscoveryState.ORGANIZATION_RESOLVED);
    }
}
