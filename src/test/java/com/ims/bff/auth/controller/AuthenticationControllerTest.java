package com.ims.bff.auth.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
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
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldExposeSupportedAuthenticationProviders() throws Exception {
        mockMvc.perform(get("/api/v1/auth/providers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.providers.length()").value(3))
                .andExpect(jsonPath("$.providers[0].type").exists());
    }

    @Test
    void shouldAuthenticateUsingBasicLogin() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login/basic")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "ims-user",
                                  "password": "ims-password"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticationType").value("BASIC"))
                .andExpect(jsonPath("$.provider").value("basic"))
                .andExpect(jsonPath("$.username").value("ims-user"));
    }

    @Test
    void shouldRedirectExternalProviderRequestsToOauthAuthorizationEndpoint() throws Exception {
        mockMvc.perform(get("/api/v1/auth/login/oauth2/google"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "/oauth2/authorization/google"));
    }

    @Test
    void shouldResolveCurrentBasicUserThroughBasicStrategy() throws Exception {
        mockMvc.perform(get("/api/v1/auth/user")
                        .with(httpBasic("ims-user", "ims-password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticationType").value("BASIC"))
                .andExpect(jsonPath("$.provider").value("basic"))
                .andExpect(jsonPath("$.username").value("ims-user"));
    }

    @Test
    void shouldAllowCorsPreflightForProvidersEndpoint() throws Exception {
        mockMvc.perform(options("/api/v1/auth/providers")
                        .header("Origin", "http://localhost:5173")
                        .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5173"));
    }
}
