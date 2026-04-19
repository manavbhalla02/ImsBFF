package com.ims.bff.authZ.orgDiscovery.dto;

public class DiscoveredOrganizationResponse {

    private final Long organizationId;
    private final String organizationCode;
    private final String organizationName;
    private final String domainName;
    private final String workEmail;

    public DiscoveredOrganizationResponse(
            Long organizationId,
            String organizationCode,
            String organizationName,
            String domainName,
            String workEmail) {
        this.organizationId = organizationId;
        this.organizationCode = organizationCode;
        this.organizationName = organizationName;
        this.domainName = domainName;
        this.workEmail = workEmail;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getDomainName() {
        return domainName;
    }

    public String getWorkEmail() {
        return workEmail;
    }
}
