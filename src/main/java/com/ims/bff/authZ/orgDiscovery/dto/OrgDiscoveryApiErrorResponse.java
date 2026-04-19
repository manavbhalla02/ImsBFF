package com.ims.bff.authZ.orgDiscovery.dto;

import java.time.Instant;

public class OrgDiscoveryApiErrorResponse {

    private final Instant timestamp = Instant.now();
    private final String message;

    public OrgDiscoveryApiErrorResponse(String message) {
        this.message = message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
