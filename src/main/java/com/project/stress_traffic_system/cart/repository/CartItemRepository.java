package com.project.stress_traffic_system.cart.repository;
import com.project.stress_traffic_system.cart.model.Cart;
import com.project.stress_traffic_system.cart.model.CartItem;
import com.project.stress_traffic_system.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long>, CartItemRepositoryCustom {

    void deleteByCartAndProduct(Cart cart, Product product);

    void deleteAllByCart(Cart cart);
}

