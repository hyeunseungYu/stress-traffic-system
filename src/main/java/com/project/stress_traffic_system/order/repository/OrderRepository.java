package com.project.stress_traffic_system.order.repository;

import com.project.stress_traffic_system.members.entity.Members;
import com.project.stress_traffic_system.order.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    List<Orders> findAllByMembersOrderByCreatedAtAsc(Members member);
}
