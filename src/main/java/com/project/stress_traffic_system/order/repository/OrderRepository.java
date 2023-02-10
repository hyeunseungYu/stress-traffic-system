package com.project.stress_traffic_system.order.repository;

import com.project.stress_traffic_system.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
