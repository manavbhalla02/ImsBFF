package com.ims.bff.plan.controller;

import static com.ims.bff.plan.PlanConstants.PLAN_API_BASE_PATH;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ims.bff.plan.dto.SubscriptionPlansResponse;
import com.ims.bff.plan.service.PlanApplicationService;

@RestController
@RequestMapping(PLAN_API_BASE_PATH)
public class PlanController {

    private final PlanApplicationService planApplicationService;

    public PlanController(PlanApplicationService planApplicationService) {
        this.planApplicationService = planApplicationService;
    }

    @GetMapping
    public SubscriptionPlansResponse getSubscriptionPlans() {
        return planApplicationService.getSubscriptionPlans();
    }
}
