package com.project.stress_traffic_system.event.repository;

import com.project.stress_traffic_system.event.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
