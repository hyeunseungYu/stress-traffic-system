package com.project.stress_traffic_system.event.repository;

import com.project.stress_traffic_system.event.model.CouponEventWinner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponEventWinnerRepository extends JpaRepository<CouponEventWinner, Long> {

    List<CouponEventWinner> findByUsername(String username);
}
