package com.project.stress_traffic_system.order.model;

import com.project.stress_traffic_system.members.entity.Members;
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
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_id")
    private Long id;

    private int totalPrice; //총 주문가격

    private int totalQuantity; // 총 주문수량

    @OneToMany
    private List<OrderItem> orderItems; //주문한 책 리스트

    @ManyToOne(fetch = FetchType.LAZY)
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
    public Orders createOrder(Members member, List<OrderItem> orderItems) {

        //주문자 정보 저장
        setMembers(member);

        for (OrderItem orderItem : orderItems) {
            //orderItems 리스트에 주문상품 하나씩 저장
            addOrderItem(orderItem);

            //totalPrice에 총 주문금액 저장
            this.totalPrice += orderItem.getProduct().getPrice() * orderItem.getQuantity();
            this.totalQuantity += orderItem.getQuantity();
        }

        //주문상태는 주문완료로 설정
        setStatus("order");
        return this;
    }

}
