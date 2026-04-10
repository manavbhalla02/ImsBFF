package com.ims.bff.planselection.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ims.bff.planselection.entity.FeatureEntity;

public interface FeatureRepository extends JpaRepository<FeatureEntity, Long> {

    List<FeatureEntity> findByFeatureKeyIn(Collection<String> featureKeys);
}
