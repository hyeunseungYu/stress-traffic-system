package com.project.stress_traffic_system.cart.repository;
import com.project.stress_traffic_system.cart.model.Cart;
import com.project.stress_traffic_system.product.model.Product;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;

import static com.project.stress_traffic_system.cart.model.QCartItem.cartItem;


public class CartRepositoryImpl implements CartItemRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    private CartRepositoryImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public void updateQuantity(Cart cart, Product product, int quantity) {
        queryFactory.update(cartItem)
                .set(cartItem.quantity, quantity)
                .where(cartItem.cart.eq(cart), cartItem.product.eq(product))
                .execute();
    }
}
