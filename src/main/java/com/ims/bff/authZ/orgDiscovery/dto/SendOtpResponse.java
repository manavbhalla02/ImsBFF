package com.ims.bff.authZ.orgDiscovery.dto;

import java.time.Instant;

import com.ims.bff.authZ.orgDiscovery.enums.OrgDiscoveryState;
import com.ims.bff.authZ.orgDiscovery.enums.OtpDeliveryChannel;

public class SendOtpResponse {

    private final String discoveryId;
    private final OrgDiscoveryState state;
    private final OtpDeliveryChannel channel;
    private final String maskedDestination;
    private final int otpLength;
    private final Instant expiresAt;

    public SendOtpResponse(
            String discoveryId,
            OrgDiscoveryState state,
            OtpDeliveryChannel channel,
            String maskedDestination,
            int otpLength,
            Instant expiresAt) {
        this.discoveryId = discoveryId;
        this.state = state;
        this.channel = channel;
        this.maskedDestination = maskedDestination;
        this.otpLength = otpLength;
        this.expiresAt = expiresAt;
    }

    public String getDiscoveryId() {
        return discoveryId;
    }

    public OrgDiscoveryState getState() {
        return state;
    }

    public OtpDeliveryChannel getChannel() {
        return channel;
    }

    public String getMaskedDestination() {
        return maskedDestination;
    }

    public int getOtpLength() {
        return otpLength;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }
}
