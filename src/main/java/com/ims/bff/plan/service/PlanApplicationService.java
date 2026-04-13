package com.ims.bff.plan.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ims.bff.plan.dto.SubscriptionPlanResponse;
import com.ims.bff.plan.dto.SubscriptionPlansResponse;
import com.ims.bff.plan.repository.PlanRepository;

@Service
public class PlanApplicationService {

    private final PlanRepository planRepository;

    public PlanApplicationService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    public SubscriptionPlansResponse getSubscriptionPlans() {
        List<SubscriptionPlanResponse> plans = planRepository.findAllByOrderByPlanIdAsc().stream()
                .map(plan -> new SubscriptionPlanResponse(
                        plan.getPlanId(),
                        plan.getPlanName(),
                        plan.getBillingPeriod().name(),
                        plan.getBasePrice()))
                .toList();
        return new SubscriptionPlansResponse(plans);
    }
}
