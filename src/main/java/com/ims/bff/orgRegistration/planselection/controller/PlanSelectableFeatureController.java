package com.ims.bff.orgRegistration.planselection.controller;

import static com.ims.bff.orgRegistration.planselection.PlanSelectionConstants.PLAN_SELECTION_API_BASE_PATH;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ims.bff.orgRegistration.planselection.dto.SelectablePlanFeaturesResponse;
import com.ims.bff.orgRegistration.planselection.service.PlanSelectableFeatureApplicationService;

@RestController
@RequestMapping(PLAN_SELECTION_API_BASE_PATH)
public class PlanSelectableFeatureController {

    private final PlanSelectableFeatureApplicationService planSelectableFeatureApplicationService;

    public PlanSelectableFeatureController(PlanSelectableFeatureApplicationService planSelectableFeatureApplicationService) {
        this.planSelectableFeatureApplicationService = planSelectableFeatureApplicationService;
    }

    @GetMapping("/{planId}/selectable-features")
    public SelectablePlanFeaturesResponse getSelectableFeatures(@PathVariable Long planId) {
        return planSelectableFeatureApplicationService.getSelectableFeatures(planId);
    }
}
