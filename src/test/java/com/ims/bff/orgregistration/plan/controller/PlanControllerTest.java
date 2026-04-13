package com.ims.bff.orgregistration.plan.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class PlanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldExposeSubscriptionPlansForRegistrationFlow() throws Exception {
        mockMvc.perform(get("/api/v1/plans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plans.length()").value(3))
                .andExpect(jsonPath("$.plans[0].planId").value(1))
                .andExpect(jsonPath("$.plans[0].planName").value("Basic"))
                .andExpect(jsonPath("$.plans[0].billingPeriod").value("MONTHLY"))
                .andExpect(jsonPath("$.plans[0].price").value(0.0));
    }

    @Test
    void shouldExposeSelectableFeaturesForPlan() throws Exception {
        mockMvc.perform(get("/api/v1/plan-selections/1/selectable-features"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.planId").value(1))
                .andExpect(jsonPath("$.features.length()").value(1))
                .andExpect(jsonPath("$.features[0].featureKey").value("AUTH"))
                .andExpect(jsonPath("$.features[0].selectionScope").value("DIRECT_CHILDREN"))
                .andExpect(jsonPath("$.features[0].minSelectable").value(1))
                .andExpect(jsonPath("$.features[0].maxSelectable").value(1))
                .andExpect(jsonPath("$.features[0].selectionRequired").value(true))
                .andExpect(jsonPath("$.features[0].children.length()").value(3))
                .andExpect(jsonPath("$.features[0].children[1].featureKey").value("AUTH_TYPE_OAUTH"))
                .andExpect(jsonPath("$.features[0].children[1].maxSelectable").value(1))
                .andExpect(jsonPath("$.features[0].children[1].children[0].featureKey").value("AUTH_PROVIDER_GOOGLE"));
    }
}
