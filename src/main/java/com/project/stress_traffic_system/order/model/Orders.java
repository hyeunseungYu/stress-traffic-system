package com.project.stress_traffic_system.order.model;

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
public class Orders {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Members members;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private Integer quantity;
    private Integer totalPrice;

    @CreatedDate
    private LocalDateTime createdAt;

    public Orders(Members members, Product product, Integer quantity) {
        this.members = members;
        this.product = product;
        this.quantity = quantity;
        totalPrice = product.getPrice() * quantity;
    }
}
