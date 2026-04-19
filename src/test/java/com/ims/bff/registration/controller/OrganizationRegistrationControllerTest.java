package com.ims.bff.registration.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class OrganizationRegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRegisterOrganizationWithDomainAndSubscription() throws Exception {
        mockMvc.perform(post("/api/v1/registrations/organizations")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "orgName": "Acme Corp",
                                  "domainName": "acme-registration.com",
                                  "subscriptionPlanId": 1,
                                  "autoRenew": true,
                                  "featureIds": [9, 10]
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.organizationId").isNumber())
                .andExpect(jsonPath("$.organizationDomainId").isNumber())
                .andExpect(jsonPath("$.subscriptionId").isNumber())
                .andExpect(jsonPath("$.message").value("Organization registration completed successfully"));
    }

    @Test
    void shouldRejectDuplicateDomainRegistration() throws Exception {
        String payload = """
                {
                  "orgName": "Acme Corp",
                  "domainName": "duplicate.com",
                  "subscriptionPlanId": 1,
                  "autoRenew": true,
                  "featureIds": [9, 10]
                }
                """;

        mockMvc.perform(post("/api/v1/registrations/organizations")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/registrations/organizations")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "orgName": "Another Corp",
                                  "domainName": "duplicate.com",
                                  "subscriptionPlanId": 2,
                                  "autoRenew": false,
                                  "featureIds": [9, 10]
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Domain is already registered: duplicate.com"));
    }

    @Test
    void shouldRejectRegistrationWhenFeatureIdsAreMissing() throws Exception {
        mockMvc.perform(post("/api/v1/registrations/organizations")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "orgName": "Acme Corp",
                                  "domainName": "acme-missing-features.com",
                                  "subscriptionPlanId": 1,
                                  "autoRenew": true
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
}
