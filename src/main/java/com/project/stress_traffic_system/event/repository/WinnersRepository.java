package com.project.stress_traffic_system.event.repository;

import com.project.stress_traffic_system.event.model.Winners;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WinnersRepository extends JpaRepository<Winners, Long>, WinnersRepositoryCustom {

}
