package com.ims.bff.authZ.orgDiscovery.exception;

public class OtpExpiredException extends OrgDiscoveryException {

    public OtpExpiredException() {
        super("OTP has expired");
    }
}
