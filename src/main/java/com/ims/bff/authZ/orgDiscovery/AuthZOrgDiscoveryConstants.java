package com.ims.bff.authZ.orgDiscovery;

public final class AuthZOrgDiscoveryConstants {

    public static final String ORG_DISCOVERY_API_BASE_PATH = "/api/v1/authz/org-discovery";
    public static final String SEND_OTP_PATH = ORG_DISCOVERY_API_BASE_PATH + "/otp/send";
    public static final String VERIFY_OTP_PATH = ORG_DISCOVERY_API_BASE_PATH + "/otp/verify";

    private AuthZOrgDiscoveryConstants() {
    }
}
