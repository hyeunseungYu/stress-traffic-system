package com.project.stress_traffic_system.cart;

import com.project.stress_traffic_system.cart.model.Cart;
import com.project.stress_traffic_system.cart.model.CartItem;
import com.project.stress_traffic_system.cart.model.dto.CartResponseDto;
import com.project.stress_traffic_system.cart.repository.CartItemRepository;
import com.project.stress_traffic_system.cart.repository.CartRepository;
import com.project.stress_traffic_system.cart.service.CartService;
import com.project.stress_traffic_system.members.entity.Members;
import com.project.stress_traffic_system.members.entity.MembersRoleEnum;
import com.project.stress_traffic_system.product.model.Product;
import com.project.stress_traffic_system.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

//Mock객체를 만들어 단위테스트로 진행
@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    CartRepository cartRepository;

    @Mock
    CartItemRepository cartItemRepository;

    @Mock
    ProductRepository productRepository;

    //모든 테스트에 반복되는 변수를 전역변수로 선언
    Members member;
    Cart cart;
    Product product;
    CartItem cartItem;

    //테스트를 실행하기 전마다 전역변수에 값을 할당
    @BeforeEach
    void beforeEach(){
        member  = new Members("user", "1234", "test@test.com","test", MembersRoleEnum.MEMBER);
        cart = new Cart(member);
        product = new Product(1L,30,"testName",16000,50);
        cartItem = new CartItem(cart,product);
    }

    @Nested
    @DisplayName("서비스 Layer Test")
    class cartServiceTest {

        @DisplayName("서비스 getCartItems 메서드 테스트")
        @Test
        void getCartItems() {
            //given
            //Mock Service
            CartService cartService = new CartService(cartRepository,cartItemRepository,productRepository);

            //findAllByCart 수행시 반환할 List<CartItem>생성
            List<CartItem> cartItems = new ArrayList<>();
            for (int i=0; i<5; i++) {
                Product newProduct = new Product(1L+i,30+i,"testName"+i,16000+i,10+i);
                CartItem newCartItem = new CartItem(cart,newProduct);
                cartItems.add(newCartItem);
            }

            //cartService에 필요한 리포지토리 custom (실제 서비스와 동일하게 구현)
            when(cartRepository.findByMember(member)).thenReturn(cart);
            when(cartItemRepository.findAllByCart(cart)).thenReturn(cartItems);

            //when
            List<CartResponseDto> cartResponseDtos = cartService.getCartItems(member);

            //then
            //addToCart 메서드를 수행하면 cartItemRepository.save 가 한 번 수행됐는지 확인한다.
            assertThat(cartResponseDtos.get(1).getPrice()).isEqualTo(16000+1);
            assertThat(cartResponseDtos.get(2).getItemName()).isEqualTo("testName"+2);
            assertThat(cartResponseDtos.get(3).getImgurl()).isEqualTo(10+3);
            assertThat(cartResponseDtos.get(4).getQuantity()).isEqualTo(1);
        }

        @DisplayName("서비스 getCartItems 메서드 테스트")
        @Test
        void addToCart() {
            //given
            //Mock Service
            CartService cartService = new CartService(cartRepository,cartItemRepository,productRepository);

            //cartService에 필요한 리포지토리 custom (실제 서비스와 동일하게 구현)
            when(cartRepository.findByMember(member)).thenReturn(cart);
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            //해당 상품을 처음 등록한다고 가정
            when(cartItemRepository.findByCartAndProduct(cart,product)).thenReturn(Optional.empty());

            //when
            cartService.addToCart(member,1L);

            //then
            //addToCart 메서드를 수행하면 cartItemRepository.save 가 한 번 수행됐는지 확인한다.
            verify(cartItemRepository, times(1)).save(any(CartItem.class));
        }

        @DisplayName("서비스 getCartItems 메서드 테스트")
        @Test
        void updateQuantity() {
            //given
            //Mock Service
            CartService cartService = new CartService(cartRepository,cartItemRepository,productRepository);

            //cartService에 필요한 리포지토리 custom (실제 서비스와 동일하게 구현)
            when(cartRepository.findByMember(member)).thenReturn(cart);
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            //해당 상품이 한 개 이상 있다고 가정( cartItem은 리턴 )
            when(cartItemRepository.findByCartAndProduct(cart,product)).thenReturn(Optional.ofNullable(cartItem));

            //when

            cartService.updateQuantity(member,1L,3);

            //then
            //cartItem의 개수가 3인지 확인
            assertThat(cartItem.getQuantity()).isEqualTo(3);
        }

        @DisplayName("서비스 getCartItems 메서드 테스트")
        @Test
        void deleteProduct() {
            //given
            //Mock Service
            CartService cartService = new CartService(cartRepository,cartItemRepository,productRepository);

            //cartService에 필요한 리포지토리 custom (실제 서비스와 동일하게 구현)
            when(cartRepository.findByMember(member)).thenReturn(cart);
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));

            //when
            cartService.deleteProduct(member, product.getId());

            //then
            //deleteProduct 메서드 실행시 deleteByCartAndProduct가 한 번 수행됐는지 검증한다.
            verify(cartItemRepository, times(1)).deleteByCartAndProduct(cart, product);
        }

        @DisplayName("서비스 getCartItems 메서드 테스트")
        @Test
        void emptyCart() {
            //given
            //Mock Service
            CartService cartService = new CartService(cartRepository,cartItemRepository,productRepository);

            //cartService에 필요한 리포지토리 custom (실제 서비스와 동일하게 구현)
            when(cartRepository.findByMember(member)).thenReturn(cart);

            //when
            cartService.emptyCart(member);

            //then
            //deleteProduct 메서드 실행시 deleteByCartAndProduct가 한 번 수행됐는지 검증한다.
            verify(cartItemRepository, times(1)).deleteAllByCart(cart);
        }

    }
}