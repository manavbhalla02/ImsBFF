package com.ims.bff.authZ.orgDiscovery.provider;

import com.ims.bff.authZ.orgDiscovery.enums.OtpDeliveryChannel;

public interface OtpDeliveryProvider {

    OtpDeliveryChannel supports();

    void sendOtp(OtpDeliveryCommand command);
}
