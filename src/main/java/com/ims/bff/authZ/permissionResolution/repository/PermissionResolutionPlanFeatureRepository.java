package com.ims.bff.authZ.permissionResolution.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ims.bff.orgRegistration.planselection.entity.PlanFeatureEntity;

public interface PermissionResolutionPlanFeatureRepository extends JpaRepository<PlanFeatureEntity, Long> {

    List<PlanFeatureEntity> findByPlanIdAndFeatureIdIn(Long planId, Collection<Long> featureIds);
}
