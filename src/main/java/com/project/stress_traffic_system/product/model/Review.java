package com.project.stress_traffic_system.product.model;

import com.project.stress_traffic_system.members.entity.Members;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Review {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id")
    private Product product;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private Members member;

    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    public Review(Product product, Members member, String content) {
        this.product = product;
        this.member = member;
        this.content = content;
    }
}
