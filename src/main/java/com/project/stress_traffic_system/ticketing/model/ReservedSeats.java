package com.project.stress_traffic_system.ticketing.model;

import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
public class ReservedSeats {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long seatId;
    private Long reservationId; //todo 연관관계
    private boolean status;

    public ReservedSeats(Long seatId, Long reservationId) {
        this.seatId = seatId;
        this.reservationId = reservationId;
        status = true;
    }
}
