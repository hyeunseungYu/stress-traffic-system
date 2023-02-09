package com.project.stress_traffic_system.ticketing.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class Seats {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;
    private Long showId; //todo show - 연관관계로 바꿔주기
    private String seatNum;

    private boolean status;

    public Seats(Long showId, String seatNum) {
        this.showId = showId;
        this.seatNum = seatNum;
        this.status = false;
    }

}
