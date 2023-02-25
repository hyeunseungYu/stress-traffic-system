package com.project.stress_traffic_system.cart.model;

import com.project.stress_traffic_system.cart.model.Cart;
import com.project.stress_traffic_system.product.model.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class CartItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;

    @OneToOne
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    private Integer quantity;

    @CreatedDate
    private LocalDateTime createdAt;

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public CartItem(Cart cart, Product product) {
        this.cart = cart;
        this.product = product;
        quantity = 1;
    }
}
