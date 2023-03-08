package com.project.stress_traffic_system.order.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.stress_traffic_system.order.model.dto.OrderRequestDto;
import com.project.stress_traffic_system.product.model.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@NoArgsConstructor
@Getter
@Entity
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "orders_id")
    private Orders orders;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id")
    private Product product; //주문상품

    private int quantity; //주문수량

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    private OrderItem(Product product) {
        this.product = product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    //생성메서드

    public static OrderItem createOrderItem(Product product, OrderRequestDto orderRequestDto) {

        //주문상품에 상품정보 넣기
        OrderItem orderItem = new OrderItem(product);
        orderItem.setQuantity(orderRequestDto.getQuantity());

        // 주문 들어온 만큼 재고 줄어들게끔
        product.removeStock(orderRequestDto.getQuantity());


        return orderItem;
    }
}
