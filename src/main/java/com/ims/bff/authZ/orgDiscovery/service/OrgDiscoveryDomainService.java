package com.ims.bff.authZ.orgDiscovery.service;

import org.springframework.stereotype.Service;

@Service
public class OrgDiscoveryDomainService {

    public String extractDomain(String emailAddress) {
        int atIndex = emailAddress.lastIndexOf('@');
        if (atIndex < 0 || atIndex == emailAddress.length() - 1) {
            throw new IllegalArgumentException("Invalid email address");
        }
        return emailAddress.substring(atIndex + 1).trim().toLowerCase();
    }

    public String maskEmail(String emailAddress) {
        int atIndex = emailAddress.indexOf('@');
        if (atIndex <= 1) {
            return "***" + emailAddress.substring(atIndex);
        }
        return emailAddress.substring(0, 2) + "***" + emailAddress.substring(atIndex);
    }
}
