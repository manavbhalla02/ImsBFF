package com.ims.bff.planselection.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ims.bff.planselection.entity.PlanFeatureSelectionRuleEntity;

public interface PlanFeatureSelectionRuleRepository extends JpaRepository<PlanFeatureSelectionRuleEntity, Long> {

    List<PlanFeatureSelectionRuleEntity> findByPlanFeatureIdIn(Collection<Long> planFeatureIds);
}
