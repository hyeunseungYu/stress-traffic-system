package com.project.stress_traffic_system.reservation.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Reservation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    private Long showId; //todo show 연관관계
    private Long memberId; // todo member 연관관계
    @CreatedDate
    private LocalDateTime createdAt;
    private boolean status;

    @ManyToOne
    private Show show;

    public Reservation(Show show, Long memberId) {
        this.show = show;
        this.memberId = memberId;
        status = true;
    }
}
