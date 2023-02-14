package com.project.stress_traffic_system.product.repository;

import com.project.stress_traffic_system.product.model.Category;
import com.project.stress_traffic_system.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    List<Product> findAllByCategory(Category category);
}
