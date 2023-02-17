package com.project.stress_traffic_system.product.repository;

import com.project.stress_traffic_system.product.model.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
}
