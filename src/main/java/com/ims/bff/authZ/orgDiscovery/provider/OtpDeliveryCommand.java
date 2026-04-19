package com.ims.bff.authZ.orgDiscovery.provider;

import com.ims.bff.authZ.orgDiscovery.enums.OtpDeliveryChannel;

public record OtpDeliveryCommand(
        OtpDeliveryChannel channel,
        String destination,
        String otpCode) {
}
