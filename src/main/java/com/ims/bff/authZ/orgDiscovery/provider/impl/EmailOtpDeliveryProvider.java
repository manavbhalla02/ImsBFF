package com.ims.bff.authZ.orgDiscovery.provider.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ims.bff.authZ.orgDiscovery.enums.OtpDeliveryChannel;
import com.ims.bff.authZ.orgDiscovery.provider.OtpDeliveryCommand;
import com.ims.bff.authZ.orgDiscovery.provider.OtpDeliveryProvider;

@Component
public class EmailOtpDeliveryProvider implements OtpDeliveryProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailOtpDeliveryProvider.class);

    @Override
    public OtpDeliveryChannel supports() {
        return OtpDeliveryChannel.EMAIL;
    }

    @Override
    public void sendOtp(OtpDeliveryCommand command) {
        LOGGER.info("Sending org discovery OTP to email {}", command.destination());
        LOGGER.info("Org discovery OTP for {} is {}", command.destination(), command.otpCode());
    }
}
