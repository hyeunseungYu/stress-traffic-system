package com.project.stress_traffic_system.cart.service;

import com.project.stress_traffic_system.cart.model.Cart;
import com.project.stress_traffic_system.cart.model.CartItem;
import com.project.stress_traffic_system.cart.repository.CartItemRepository;
import com.project.stress_traffic_system.cart.repository.CartRepository;
import com.project.stress_traffic_system.cart.service.CartService;
import com.project.stress_traffic_system.members.entity.Members;
import com.project.stress_traffic_system.members.entity.MembersRoleEnum;
import com.project.stress_traffic_system.members.repository.MembersRepository;
import com.project.stress_traffic_system.product.model.Product;
import com.project.stress_traffic_system.product.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CartServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private MembersRepository membersRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartService cartService;

     Members member;
     Cart cart;
     Product product;
     CartItem cartItem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        member = new Members();
        member.setAddress("test");
        member.setPassword("test");
        member.setRole(MembersRoleEnum.MEMBER);
        member.setUsername("hyeunseung");

        cart = new Cart(member);
        cart.setId(1L);

        product = new Product();



        cartItem = new CartItem(cart, product);
    }

    @Test
    @DisplayName("정상적으로 작동하는 경우 - 장바구니에 상품이 없는 경우")
    public void testAddToCartWhenProductIsNotInCart() {
        // given
        when(membersRepository.save(member)).thenReturn(member);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(product));
        when(cartItemRepository.findByCartAndProduct(cart, product)).thenReturn(Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

        // when
        cartService.addToCart(member, 1L);

        // then
        //addToCart에서 저장된 cartItem의 Cart와 Product가 when을 통해서 넣어준 cart,product가 같은지를 본다.
        Assertions.assertEquals(cart,cartItem.getCart());
        Assertions.assertEquals(product,cartItem.getProduct());
    }
}