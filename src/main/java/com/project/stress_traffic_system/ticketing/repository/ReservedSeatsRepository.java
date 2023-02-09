package com.project.stress_traffic_system.ticketing.repository;

import com.project.stress_traffic_system.ticketing.model.ReservedSeats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservedSeatsRepository extends JpaRepository<ReservedSeats, Long> {

    List<ReservedSeats> findAllBySeatIdInAndStatus(List<Long> seats, boolean status);
}
