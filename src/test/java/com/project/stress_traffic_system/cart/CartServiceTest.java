//package com.project.stress_traffic_system.cart;
//
//import com.project.stress_traffic_system.cart.model.Cart;
//import com.project.stress_traffic_system.cart.model.CartItem;
//import com.project.stress_traffic_system.cart.model.dto.CartResponseDto;
//import com.project.stress_traffic_system.cart.repository.CartItemRepository;
//import com.project.stress_traffic_system.cart.repository.CartRepository;
//import com.project.stress_traffic_system.cart.service.CartService;
//import com.project.stress_traffic_system.members.entity.Members;
//import com.project.stress_traffic_system.members.repository.MembersRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
///*@Transactional을 사용하여 실제 DB에 영향을 주지 않는다*/
//@Slf4j
//@SpringBootTest
//public class CartServiceTest {
//
//    @Autowired
//    private CartRepository cartRepository;
//
//    @Autowired
//    private CartItemRepository cartItemRepository;
//
//    @Autowired
//    private MembersRepository membersRepository;
//
//    @Autowired
//    private CartService cartService;
//
//    @Nested
//    @DisplayName("서비스 Layer Test")
//    class cartServiceTest {
//
//        @Transactional //메서드가 실행될 동안만 세션 유지
//        @DisplayName("서비스 getCartItems 메서드 테스트")
//        @Test
//        void getCartItems() {
//            //given
//            Long findMemberId = 2000084L; //사용할 멤버 아이디
//            Long ProductId = 10L; //저장할 상품 id
//            Members member = membersRepository.findById(findMemberId)
//                    .orElseThrow(() -> new IllegalArgumentException("Wrong MemberId"));
//            cartService.addToCart(member, ProductId); //장바구니 추가
//
//            //when
//            //해당 멤버의 장바구니 리스트를 가져온다.
//            List<CartResponseDto> cartResponseDtos = cartService.getCartItems(member);
//
//            //then
//            //가장 최근에 추가한 아이템을 조회
//            assertThat(cartResponseDtos.get(cartResponseDtos.size()-1).getItemName()).isEqualTo("Monika Will");
//            assertThat(cartResponseDtos.get(cartResponseDtos.size()-1).getImgurl()).isEqualTo(2);
//            assertThat(cartResponseDtos.get(cartResponseDtos.size()-1).getPrice()).isEqualTo(32209);
//        }
//
//        @Transactional //메서드가 실행될 동안만 세션 유지
//        @DisplayName("서비스 addToCart 메서드 테스트")
//        @Test
//        void addToCart() {
//            //given
//            Long findMemberId = 2000084L; //사용할 멤버 아이디
//            Members member = membersRepository.findById(findMemberId)
//                    .orElseThrow(() -> new IllegalArgumentException("Wrong MemberId"));
//            Cart cart = cartRepository.findByMember(member);
//            Long ProductId = 1000L; //추가할 상품 id
//
//            //when
//            cartService.addToCart(member, ProductId);
//            //추가한 상품 찾기
//            CartItem findCartItem = cartItemRepository.findByProductId(ProductId)
//                    .orElseThrow(() -> new IllegalArgumentException("Wrong MemberId"));
//
//            assertThat(findCartItem.getProduct().getId()).isEqualTo(ProductId);
//            assertThat(findCartItem.getCart()).isEqualTo(cart);
//            assertThat(findCartItem.getCart().getMember()).isEqualTo(member);
//        }
//
//        @Transactional //메서드가 실행될 동안만 세션 유지
//        @DisplayName("서비스 updateQuantity 메서드 테스트")
//        @Test
//        void updateQuantity() {
//            //given
//            Long findMemberId = 2000084L; //사용할 멤버 아이디
//            Members member = membersRepository.findById(findMemberId)
//                    .orElseThrow(() -> new IllegalArgumentException("Wrong MemberId"));
//            Long ProductId = 1L; //수량을 변경할 상품 id
//            int UpdateQuantity = 5; //변경할 수량 사이즈
//
//            CartItem CartItem = cartItemRepository.findByProductId(ProductId)
//                    .orElseThrow(() -> new IllegalArgumentException("Wrong MemberId"));
//            log.info("변경 전 상품 수량: " + CartItem.getQuantity());
//
//            //when
//            cartService.updateQuantity(member, ProductId, UpdateQuantity);
//
//            //then
//            //수량 변경한 상품 찾기
//            CartItem findCartItem = cartItemRepository.findByProductId(ProductId)
//                    .orElseThrow(() -> new IllegalArgumentException("Wrong MemberId"));
//            log.info("변경 후 상품 수량: " + findCartItem.getQuantity());
//
//            assertThat(findCartItem.getQuantity()).isEqualTo(UpdateQuantity);
//        }
//
//        @Transactional
//        @DisplayName("서비스 deleteProduct 메서드 테스트")
//        @Test
//        void deleteProduct() {
//            //given
//            Long findMemberId = 2000084L; //사용할 멤버 아이디
//            Members member = membersRepository.findById(findMemberId)
//                    .orElseThrow(() -> new IllegalArgumentException("Wrong MemberId"));
//            Long ProductId = 1L; //삭제할 상품 id
//
//            //when
//            cartService.deleteProduct(member, ProductId);
//
//            //then
//            //삭제한 상품 찾기
//            Optional<CartItem> findCartItem = cartItemRepository.findByProductId(ProductId);
//
//            assertThat(findCartItem).isEmpty(); //해당 상품이 삭제가 됐는지 확인
//        }
//
//        @Transactional
//        @DisplayName("서비스 emptyCart 메서드 테스트")
//        @Test
//        void emptyCart() {
//            //given
//            Long findMemberId = 2000084L; //사용할 멤버 아이디
//            Members member = membersRepository.findById(findMemberId)
//                    .orElseThrow(() -> new IllegalArgumentException("Wrong MemberId"));
//            Cart cart = cartRepository.findByMember(member); //해당 멤버의 카트
//
//            //when
//            cartService.emptyCart(member);
//
//            //then
//            //삭제한 카트 찾기
//            List<CartItem> cartItem = cartItemRepository.findAllByCart(cart);
//
//            assertThat(cartItem).isEmpty(); //카트가 삭제됐는지 확인
//        }
//
//    }
//}