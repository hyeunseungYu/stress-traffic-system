package com.project.stress_traffic_system.ticketing.repository;


import java.util.List;

public interface CustomSeatsRepository {
    List<String> findByReservationId(Long reservationId);

    void updateStatus(List<Long> seats);
}
