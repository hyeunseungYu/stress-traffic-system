package com.project.stress_traffic_system.product.repository;

import com.project.stress_traffic_system.product.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
