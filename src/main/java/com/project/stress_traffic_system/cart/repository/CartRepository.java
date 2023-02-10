package com.project.stress_traffic_system.cart.repository;

import com.project.stress_traffic_system.cart.model.Cart;
import com.project.stress_traffic_system.members.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByMember(Members member);
}
