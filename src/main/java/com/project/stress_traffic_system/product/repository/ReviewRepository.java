package com.project.stress_traffic_system.product.repository;

import com.project.stress_traffic_system.product.model.Product;
import com.project.stress_traffic_system.product.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByProduct(Product product);
}
