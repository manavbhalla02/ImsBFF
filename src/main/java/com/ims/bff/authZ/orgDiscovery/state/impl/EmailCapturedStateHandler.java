package com.ims.bff.authZ.orgDiscovery.state.impl;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.ims.bff.authZ.orgDiscovery.context.OrgDiscoveryStateContext;
import com.ims.bff.authZ.orgDiscovery.enums.OrgDiscoveryState;
import com.ims.bff.authZ.orgDiscovery.enums.OtpDeliveryChannel;
import com.ims.bff.authZ.orgDiscovery.factory.OtpDeliveryProviderFactory;
import com.ims.bff.authZ.orgDiscovery.model.OrgDiscoverySession;
import com.ims.bff.authZ.orgDiscovery.provider.OtpCodeGenerator;
import com.ims.bff.authZ.orgDiscovery.provider.OtpDeliveryCommand;
import com.ims.bff.authZ.orgDiscovery.service.OrgDiscoveryDomainService;
import com.ims.bff.authZ.orgDiscovery.state.OrgDiscoveryStateHandler;
import com.ims.bff.authZ.orgDiscovery.store.OrgDiscoverySessionStore;

@Component
public class EmailCapturedStateHandler implements OrgDiscoveryStateHandler {

    private static final Duration OTP_TTL = Duration.ofMinutes(5);

    private final OrgDiscoveryDomainService domainService;
    private final OtpCodeGenerator otpCodeGenerator;
    private final OtpDeliveryProviderFactory otpDeliveryProviderFactory;
    private final OrgDiscoverySessionStore sessionStore;
    private final Clock clock;

    public EmailCapturedStateHandler(
            OrgDiscoveryDomainService domainService,
            OtpCodeGenerator otpCodeGenerator,
            OtpDeliveryProviderFactory otpDeliveryProviderFactory,
            OrgDiscoverySessionStore sessionStore,
            Clock clock) {
        this.domainService = domainService;
        this.otpCodeGenerator = otpCodeGenerator;
        this.otpDeliveryProviderFactory = otpDeliveryProviderFactory;
        this.sessionStore = sessionStore;
        this.clock = clock;
    }

    @Override
    public OrgDiscoveryState supports() {
        return OrgDiscoveryState.EMAIL_CAPTURED;
    }

    @Override
    public void handle(OrgDiscoveryStateContext context) {
        String emailAddress = context.getEmailAddress().trim().toLowerCase();
        String domainName = domainService.extractDomain(emailAddress);
        String otpCode = otpCodeGenerator.generateOtp();
        Instant expiresAt = Instant.now(clock).plus(OTP_TTL);

        OrgDiscoverySession session = new OrgDiscoverySession(
                UUID.randomUUID().toString(),
                emailAddress,
                domainName,
                otpCode,
                OtpDeliveryChannel.EMAIL,
                expiresAt,
                OrgDiscoveryState.OTP_SENT);

        otpDeliveryProviderFactory.getProvider(session.getChannel())
                .sendOtp(new OtpDeliveryCommand(session.getChannel(), emailAddress, otpCode));

        sessionStore.save(session);
        context.setEmailAddress(emailAddress);
        context.setSession(session);
        context.setCurrentState(OrgDiscoveryState.OTP_SENT);
    }
}
