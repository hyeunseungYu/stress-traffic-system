package com.project.stress_traffic_system.cart;

import com.project.stress_traffic_system.cart.model.Cart;
import com.project.stress_traffic_system.cart.model.CartItem;
import com.project.stress_traffic_system.cart.repository.CartItemRepository;
import com.project.stress_traffic_system.cart.repository.CartRepository;
import com.project.stress_traffic_system.members.entity.Members;
import com.project.stress_traffic_system.members.entity.MembersRoleEnum;
import com.project.stress_traffic_system.members.repository.MembersRepository;
import com.project.stress_traffic_system.product.model.Product;
import com.project.stress_traffic_system.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

//조회테스트는 Bean에 등록된 객체를 사용하고 생성테스트(testsave)는 Mock 객체로 사용//
@DataJpaTest /*모든 테스트가 끝난 뒤 롤백*/
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //h2 DB가 아닌 실제 DB이용
//@SpringBootTest(통합테스트)
public class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private MembersRepository membersRepository;

    @Autowired
    private ProductRepository productRepository;

    @Mock
    private CartItemRepository MockcartItemRepository;

    @Nested
    @DisplayName("findByMember 메서드 테스트")
    class findCart {

        //@Transactional //통합테스트일 경우 (메서드가 끝날 때까지 세션이 유지된다.)
        @DisplayName("cart가 멤버로 조회가 잘 되는지 테스트")
        @Test
        void findByMember() {
            //given
            Long findMemberId = 2000084L; //찾고자 하는 멤버 아이디
            Members member = membersRepository.findById(findMemberId)
                    .orElseThrow(() -> new IllegalArgumentException("Wrong MemberId"));

            //when
            Cart cart = cartRepository.findByMember(member);

            // then
            assertThat(cart.getId()).isNotZero();
            assertThat(cart.getMember()).isEqualTo(member);
        }

        //@Transactional //통합테스트일 경우 (메서드가 끝날 때까지 세션이 유지된다.)
        @DisplayName("findAllByCart 메서드 테스트")
        @Test
        void findAllByCart() {
            //given
            //찾고자 하는 멤버의 카트
            Long findMemberId = 2000084L; //찾고자 하는 멤버 아이디
            Members member = membersRepository.findById(findMemberId)
                    .orElseThrow(() -> new IllegalArgumentException("Wrong MemberId:<>"));
            Cart cart = cartRepository.findByMember(member);

            //when
            List<CartItem> cartItems = cartItemRepository.findAllByCart(cart);

            // then
            //찾은 아이템의 멤버와 카트가 맞는지 확인
            assertThat(cartItems.get(0).getCart().getMember()).isEqualTo(member);
            assertThat(cartItems.get(0).getCart()).isEqualTo(cart);
        }

        //@Transactional //통합테스트일 경우 (메서드가 끝날 때까지 세션이 유지된다.)
        @DisplayName("findByCartAndProduct 메서드 테스트")
        @Test
        void findByCartAndProduct() {
            //given
            //찾고자 하는 멤버의 카트와 상품
            Long findMemberId = 2000084L; //찾고자 하는 멤버 아이디
            Members member = membersRepository.findById(findMemberId)
                    .orElseThrow(() -> new IllegalArgumentException("Wrong MemberId:<>"));
            Cart cart = cartRepository.findByMember(member);
            Product product = productRepository.findById(1L)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다"));

            //when
            CartItem findCartItem = cartItemRepository.findByCartAndProduct(cart, product)
                    .orElseThrow(() -> new IllegalArgumentException("Wrong MemberId:<>"));

            System.out.println("findCartItem : " + findCartItem.getId());

            // then
            //카트 아이템이 상품과 카트와 일치하는지 확인
            assertThat(findCartItem.getProduct()).isEqualTo(product);
            assertThat(findCartItem.getCart()).isEqualTo(cart);
        }

        @DisplayName("cartItem이 저장이 잘 되는지 확인") //Mock객체로 진행
        @Test
        public void testSave() {
            //given
            Members member = new Members("usertest","abcde123?","서울",MembersRoleEnum.MEMBER);
            Product product = new Product(1L,30);
            Cart cart = new Cart(member);
            CartItem cartItem = new CartItem(cart, product);

            //custom한 리포지터리 ->MockcartItemRepository cartItem를 저장하면 cartItem를 반환
            when(MockcartItemRepository.save(cartItem)).thenReturn(cartItem);

            // when
            CartItem savedCartItem = MockcartItemRepository.save(cartItem);

            // then
            assertThat(savedCartItem.getCart()).isEqualTo(cartItem.getCart());
            assertThat(savedCartItem.getProduct()).isEqualTo(cartItem.getProduct());
        }
    }
}