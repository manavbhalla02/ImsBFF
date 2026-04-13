package com.ims.bff.orgregistration.plan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ims.bff.orgregistration.plan.entity.PlanEntity;

public interface PlanRepository extends JpaRepository<PlanEntity, Long> {

    List<PlanEntity> findAllByOrderByPlanIdAsc();
}
