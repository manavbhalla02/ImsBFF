package com.ims.bff.orgregistration.planselection.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ims.bff.orgregistration.planselection.entity.PlanFeatureEntity;

public interface PlanFeatureRepository extends JpaRepository<PlanFeatureEntity, Long>, PlanFeatureRepositoryCustom {

    List<PlanFeatureEntity> findByPlanFeatureIdIn(Collection<Long> planFeatureIds);
}
