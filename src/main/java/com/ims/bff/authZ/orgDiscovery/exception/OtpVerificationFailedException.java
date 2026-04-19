package com.ims.bff.authZ.orgDiscovery.exception;

public class OtpVerificationFailedException extends OrgDiscoveryException {

    public OtpVerificationFailedException() {
        super("OTP verification failed");
    }
}
