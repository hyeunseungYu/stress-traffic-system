package com.project.stress_traffic_system.event.model;

import com.project.stress_traffic_system.members.entity.Members;
import com.project.stress_traffic_system.product.model.Product;
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
public class Event {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Product product;

    public Event(Product product) {
        this.product = product;
    }
}
