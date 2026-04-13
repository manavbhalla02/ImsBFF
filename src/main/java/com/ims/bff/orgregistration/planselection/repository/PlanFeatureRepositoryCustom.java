package com.ims.bff.orgregistration.planselection.repository;

import java.util.List;

import com.ims.bff.orgregistration.planselection.entity.PlanFeatureEntity;
import com.ims.bff.orgregistration.planselection.enums.FeatureGrantType;

public interface PlanFeatureRepositoryCustom {

    List<PlanFeatureEntity> findByPlanIdAndGrantTypeOrdered(Long planId, FeatureGrantType grantType);
}
