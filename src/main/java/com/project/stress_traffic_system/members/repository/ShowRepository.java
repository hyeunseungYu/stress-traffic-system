package com.project.stress_traffic_system.members.repository;

import com.project.stress_traffic_system.reservation.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowRepository extends JpaRepository<Show, Long> {
}
