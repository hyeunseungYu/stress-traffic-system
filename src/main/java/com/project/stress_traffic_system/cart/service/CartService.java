package com.project.stress_traffic_system.cart.service;

import com.project.stress_traffic_system.cart.model.Cart;
import com.project.stress_traffic_system.cart.model.CartItem;
import com.project.stress_traffic_system.cart.model.dto.CartResponseDto;
import com.project.stress_traffic_system.cart.repository.CartItemRepository;
import com.project.stress_traffic_system.cart.repository.CartRepository;
import com.project.stress_traffic_system.members.entity.Members;
import com.project.stress_traffic_system.product.model.Product;
import com.project.stress_traffic_system.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;


    //장바구니에 담긴 상품 목록 가져오기
    public List<CartResponseDto> getCartItems(Members member) {

        //회원의 장바구니 가져오기
        Cart cart = getCart(member);

        //장바구니에 담긴 상품목록 가져오기
        List<CartItem> cartItems = cartItemRepository.findAllByCart(cart);

        return cartItems.stream().map(cartItem ->
                        CartResponseDto.builder()
                                .itemName(cartItem.getProduct().getName())
                                .imgurl(cartItem.getProduct().getImgurl())
                                .price(cartItem.getProduct().getDiscount(cartItem.getProduct().getPrice(), cartItem.getProduct().getDiscount()))
                                .quantity(cartItem.getQuantity())
                                .build())
                .collect(Collectors.toList());
    }

    //장바구니에 상품 추가
    @Transactional
    public void addToCart(Members member, Long productId) {

        //해당 회원의 장바구니를 찾아온다
        Cart cart = getCart(member);

        //상품정보를 가져온다
        Product product = getProduct(productId);

        Optional<CartItem> findCartItem = cartItemRepository.findByCartAndProduct(cart, product);

        //장바구니에 상품이 없으면 장바구니아이템 엔티티를 저장한다
        if (findCartItem.isEmpty()) {
            log.info("장바구니에 아이템이 없음 -> cartItemRepository.save 실행");
            CartItem cartItem = new CartItem(cart, product);
            cartItemRepository.save(cartItem);
            return;
        }

        //장바구니에 이미 담긴 상품이라면 수량을 +1 해준다
        findCartItem.get().setQuantity(findCartItem.get().getQuantity() + 1);
    }

    //장바구니의 상품 수량 변경
    @Transactional
    public void updateQuantity(Members member, Long productId, int quantity) {

        //해당 회원의 장바구니를 찾아온다
        Cart cart = getCart(member);

        //상품정보를 가져온다
        Product product = getProduct(productId);

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product).orElseThrow(
                () -> new IllegalArgumentException("장바구니에 해당 상품이 없습니다")
        );

        //save를 호출하지 않아도 자동으로 변경 감지하여 flush 된다
        cartItem.setQuantity(quantity);
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
