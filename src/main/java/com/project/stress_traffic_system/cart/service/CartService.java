package com.project.stress_traffic_system.cart.service;

import com.project.stress_traffic_system.cart.model.Cart;
import com.project.stress_traffic_system.cart.model.CartItem;
import com.project.stress_traffic_system.cart.model.dto.CartRequestDto;
import com.project.stress_traffic_system.cart.repository.CartItemRepository;
import com.project.stress_traffic_system.cart.repository.CartRepository;
import com.project.stress_traffic_system.members.entity.Members;
import com.project.stress_traffic_system.product.model.Product;
import com.project.stress_traffic_system.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    //장바구니에 상품 추가
    @Transactional
    public void addToCart(Members member, CartRequestDto requestDto) {

        //해당 회원의 장바구니를 찾아온다
        Cart cart = getCart(member);

        //상품정보를 가져온다
        Product product = getProduct(requestDto.getProductId());

        //장바구니아이템 엔티티를 저장한다
        CartItem cartItem = new CartItem(cart, product, requestDto.getQuantity());
        cartItemRepository.save(cartItem);
    }

    //장바구니의 상품 수량 변경
    @Transactional
    public void updateQuantity(Members member, Long productId, int quantity) {

        //해당 회원의 장바구니를 찾아온다
        Cart cart = getCart(member);

        //상품정보를 가져온다
        Product product = getProduct(productId);

        //수량을 업데이트한다
//        cartItemRepository.updateQuantity(cart, product, quantity);
    }

    //장바구니 상품 삭제
    @Transactional
    public void deleteProduct(Members member, Long productId) {
        //해당 회원의 장바구니를 찾아온다
        Cart cart = getCart(member);

        //상품정보를 가져온다
        Product product = getProduct(productId);

        cartItemRepository.deleteByCartAndProduct(cart, product);
    }

    //장바구니 비우기
    @Transactional
    public void emptyCart(Members member) {
        //해당 회원의 장바구니를 찾아온다
        Cart cart = getCart(member);

        cartItemRepository.deleteAllByCart(cart);
    }

    private Cart getCart(Members member) {
        return cartRepository.findByMember(member);
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 상품입니다")
        );
    }

}
