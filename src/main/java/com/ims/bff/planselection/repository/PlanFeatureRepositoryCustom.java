package com.ims.bff.planselection.repository;

import java.util.List;

import com.ims.bff.planselection.entity.PlanFeatureEntity;
import com.ims.bff.planselection.enums.FeatureGrantType;

public interface PlanFeatureRepositoryCustom {

    List<PlanFeatureEntity> findByPlanIdAndGrantTypeOrdered(Long planId, FeatureGrantType grantType);
}
