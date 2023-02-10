package com.project.stress_traffic_system.product.repository;

import com.project.stress_traffic_system.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {


}
