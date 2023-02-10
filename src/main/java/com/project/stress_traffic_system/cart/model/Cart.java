package com.project.stress_traffic_system.cart.model;

import com.project.stress_traffic_system.members.entity.Members;
import com.project.stress_traffic_system.product.model.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Cart {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Members member;

    public Cart(Members member) {
        this.member = member;
    }
}
