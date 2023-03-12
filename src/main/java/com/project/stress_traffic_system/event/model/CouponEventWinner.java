package com.project.stress_traffic_system.event.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name="coupon_event_winner")
@Getter
@Setter
@NoArgsConstructor
public class CouponEventWinner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "winner_id")
    private Long id;

    @Column(name = "coupon_type")
    private String couponType;

    @Column(name = "event_date")
    private Double eventDate;

    @Column(name = "username")
    private String username;

}
