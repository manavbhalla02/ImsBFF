package com.ims.bff.orgRegistration.planselection.repository;

import java.util.List;

import com.ims.bff.orgRegistration.planselection.entity.PlanFeatureEntity;
import com.ims.bff.orgRegistration.planselection.enums.FeatureGrantType;

public interface PlanFeatureRepositoryCustom {

    List<PlanFeatureEntity> findByPlanIdAndGrantTypeOrdered(Long planId, FeatureGrantType grantType);
}
