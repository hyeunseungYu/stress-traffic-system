package com.project.stress_traffic_system.event.model;

import com.project.stress_traffic_system.members.entity.Members;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@EntityListeners(AuditingEntityListener.class)
@Getter
@Entity
public class Winners {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "winners_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "event_id")
    private Event event;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private Members member;

    private int num;

    @CreatedDate
    private LocalDateTime createdAt;

    public Winners(Event event, Members member, int num) {
        this.event = event;
        this.member = member;
        this.num = num;
    }
}
