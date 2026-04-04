package com.ims.bff.auth;

public final class AuthConstants {

    public static final String AUTH_API_BASE_PATH = "/api/v1/auth";
    public static final String AUTH_PROVIDERS_PATH = AUTH_API_BASE_PATH + "/providers";
    public static final String AUTH_BASIC_LOGIN_PATH = AUTH_API_BASE_PATH + "/login/basic";
    public static final String AUTH_EXTERNAL_LOGIN_PATH = AUTH_API_BASE_PATH + "/login/{type}/{provider}";
    public static final String AUTH_USER_PATH = AUTH_API_BASE_PATH + "/user";
    public static final String OAUTH2_AUTHORIZATION_BASE_URI = "/oauth2/authorization";
    public static final String LOGIN_PAGE = "/login";
    public static final String DEFAULT_BASIC_ROLE = "USER";
    public static final String DEFAULT_PROVIDER_KEY = "*";

    public static final String PROVIDER_BASIC = "basic";
    public static final String PROVIDER_GOOGLE = "google";
    public static final String PROVIDER_GITHUB = "github";
    public static final String PROVIDER_LINKEDIN = "linkedin";
    public static final String PROVIDER_OKTA = "okta";

    private AuthConstants() {
    }
}
