package com.ims.bff.authZ.orgDiscovery.model;

import java.time.Instant;

import com.ims.bff.authZ.orgDiscovery.enums.OrgDiscoveryState;
import com.ims.bff.authZ.orgDiscovery.enums.OtpDeliveryChannel;

public class OrgDiscoverySession {

    private final String discoveryId;
    private final String emailAddress;
    private final String domainName;
    private final String otpCode;
    private final OtpDeliveryChannel channel;
    private final Instant expiresAt;
    private OrgDiscoveryState state;

    public OrgDiscoverySession(
            String discoveryId,
            String emailAddress,
            String domainName,
            String otpCode,
            OtpDeliveryChannel channel,
            Instant expiresAt,
            OrgDiscoveryState state) {
        this.discoveryId = discoveryId;
        this.emailAddress = emailAddress;
        this.domainName = domainName;
        this.otpCode = otpCode;
        this.channel = channel;
        this.expiresAt = expiresAt;
        this.state = state;
    }

    public String getDiscoveryId() {
        return discoveryId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getDomainName() {
        return domainName;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public OtpDeliveryChannel getChannel() {
        return channel;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public OrgDiscoveryState getState() {
        return state;
    }

    public void setState(OrgDiscoveryState state) {
        this.state = state;
    }
}
