package com.ims.bff.authZ.orgDiscovery.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import com.ims.bff.authZ.orgDiscovery.provider.OtpCodeGenerator;

@SpringBootTest
@AutoConfigureMockMvc
@Import(OrgDiscoveryControllerTest.FixedOtpGeneratorConfig.class)
class OrgDiscoveryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldSendOtpForWorkEmailDiscovery() throws Exception {
        mockMvc.perform(post("/api/v1/authz/org-discovery/otp/send")
                        .contentType("application/json")
                        .content("""
                                {
                                  "emailAddress": "user@acme.com"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.discoveryId").isNotEmpty())
                .andExpect(jsonPath("$.state").value("OTP_SENT"))
                .andExpect(jsonPath("$.channel").value("EMAIL"))
                .andExpect(jsonPath("$.otpLength").value(6));
    }

    @Test
    void shouldVerifyOtpAndVendOutOrganization() throws Exception {
        String responseBody = mockMvc.perform(post("/api/v1/authz/org-discovery/otp/send")
                        .contentType("application/json")
                        .content("""
                                {
                                  "emailAddress": "user@acme.com"
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String discoveryId = responseBody.replaceAll(".*\"discoveryId\"\\s*:\\s*\"([^\"]+)\".*", "$1");

        mockMvc.perform(post("/api/v1/authz/org-discovery/otp/verify")
                        .contentType("application/json")
                        .content("""
                                {
                                  "discoveryId": "%s",
                                  "otp": "123456"
                                }
                                """.formatted(discoveryId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("ORGANIZATION_RESOLVED"))
                .andExpect(jsonPath("$.organization.organizationId").value(1))
                .andExpect(jsonPath("$.organization.organizationCode").value("ORG_ACME"))
                .andExpect(jsonPath("$.organization.organizationName").value("Acme Retail"))
                .andExpect(jsonPath("$.organization.domainName").value("acme.com"))
                .andExpect(jsonPath("$.organization.workEmail").value("user@acme.com"));
    }

    @Test
    void shouldRejectInvalidOtp() throws Exception {
        String responseBody = mockMvc.perform(post("/api/v1/authz/org-discovery/otp/send")
                        .contentType("application/json")
                        .content("""
                                {
                                  "emailAddress": "user@acme.com"
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String discoveryId = responseBody.replaceAll(".*\"discoveryId\"\\s*:\\s*\"([^\"]+)\".*", "$1");

        mockMvc.perform(post("/api/v1/authz/org-discovery/otp/verify")
                        .contentType("application/json")
                        .content("""
                                {
                                  "discoveryId": "%s",
                                  "otp": "654321"
                                }
                                """.formatted(discoveryId)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("OTP verification failed"));
    }

    @TestConfiguration
    static class FixedOtpGeneratorConfig {

        @Bean
        @Primary
        OtpCodeGenerator otpCodeGenerator() {
            return () -> "123456";
        }
    }
}
