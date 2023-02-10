package com.project.stress_traffic_system.ticketing.repository;

import com.project.stress_traffic_system.ticketing.model.Seats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatsRepository extends JpaRepository<Seats, Long>, CustomSeatsRepository {

    List<Seats> findAllByShowId(Long showId);

    List<Seats> findAllByShowIdAndSeatIdIn(Long showId, List<Long> seats);
}
