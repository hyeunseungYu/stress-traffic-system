package com.project.stress_traffic_system.order.model;

import com.project.stress_traffic_system.members.entity.Members;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "orders_id")
    private Long id;

    private int totalPrice; //총 주문가격

    private int totalQuantity; // 총 주문수량

    @OneToMany(mappedBy = "orders", cascade = ALL)
    private List<OrderItem> orderItems = new ArrayList<>(); //주문한 책 리스트

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private Members members;

    @CreatedDate
    private LocalDateTime createdAt; //주문일자

    private String status; //주문상태


    //주문상품 객체에 주문상품 추가하고, 주문정보도 추가하는 메서드
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrders(this);
    }

    private void setMembers(Members members) {
        this.members = members;
    }

    private void setStatus(String status) {
        this.status = status;
    }

    //생성 메서드
    public static Orders createOrder(Members member, List<OrderItem> orderItems) {
        Orders order = new Orders();

        //회원정보, 주문상태 저장
        order.setMembers(member);
        order.setStatus("order");

        for (OrderItem orderItem : orderItems) {
            //총 주문금액, 총 수량 저장
            order.totalPrice += orderItem.getProduct().getPrice() * orderItem.getQuantity();
            order.totalQuantity += orderItem.getQuantity();
            order.addOrderItem(orderItem);
        }
        return order;
    }
}
