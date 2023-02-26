//package com.project.stress_traffic_system.order.service;
//
//import com.project.stress_traffic_system.cart.model.Cart;
//import com.project.stress_traffic_system.cart.model.CartItem;
//import com.project.stress_traffic_system.cart.repository.CartItemRepository;
//import com.project.stress_traffic_system.cart.repository.CartRepository;
//import com.project.stress_traffic_system.cart.service.CartService;
//import com.project.stress_traffic_system.members.entity.Members;
//import com.project.stress_traffic_system.members.entity.MembersRoleEnum;
//import com.project.stress_traffic_system.members.repository.MembersRepository;
//import com.project.stress_traffic_system.order.model.dto.OrderDto;
//import com.project.stress_traffic_system.order.model.dto.OrderRequestDto;
//import com.project.stress_traffic_system.order.repository.OrderRepository;
//import com.project.stress_traffic_system.product.model.Product;
//import com.project.stress_traffic_system.product.repository.ProductRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//@Slf4j
//@DisplayName("orderService 테스트")
//class OrderServiceTest {
//
//    @Mock
//    private OrderRepository orderRepository;
//
//    @Mock
//    private CartRepository cartRepository;
//
//    @Mock
//    private ProductRepository productRepository;
//
//    @Mock
//    private MembersRepository membersRepository;
//    @Mock
//    private CartItemRepository cartItemRepository;
//
//    Members member;
//    Cart cart;
//
//    Product product;
//    CartItem cartItem;
//    OrderRequestDto orderRequestDto;
//
//    @BeforeEach
//    void beforeEach() {
//        member = new Members("user", "1234", "test@test.com", "test", MembersRoleEnum.MEMBER);
//        cart = new Cart(member);
//        product = new Product(1L, 30, "testName", 16000, 50);
//        cartItem = new CartItem(cart, product);
//
//        orderRequestDto = new OrderRequestDto();
//        orderRequestDto.setProductId(1L);
//        orderRequestDto.setQuantity(10);
//        orderRequestDto.setDiscount(1000f);
//        orderRequestDto.setDcType("none");
//    }
//
//
//    @Nested
//    @DisplayName("성공 케이스")
//    class successCase {
//        @Test
//        @DisplayName("checkProduct")
//        void checkProductTest() {
//            //given
//            when(productRepository.findByIdWithPessimisticLock(orderRequestDto.getProductId())).thenReturn(product);
//            OrderService orderService = new OrderService(orderRepository, productRepository, cartItemRepository, cartRepository);
//            //when
//            Product findResult = orderService.checkProduct(orderRequestDto);
//            //then
//            Assertions.assertEquals(product, findResult);
//
//        }
//
//        @Test
//        @DisplayName("checkStock")
//        void checkStockTest() {
//            //given
//            OrderService orderService = new OrderService(orderRepository, productRepository, cartItemRepository, cartRepository);
//            //when
//
//            //then
//            Assertions.assertDoesNotThrow(() -> orderService.checkStock(orderRequestDto, product));
//        }
//
//    }
//
//
//    @Nested
//    @DisplayName("실패 케이스")
//    class failCase {
//        @Test
//        @DisplayName("checkStock - orderRequestDto가 null일 때")
//        void checkStockFail1() {
//            //given
//            OrderService orderService = new OrderService(orderRepository, productRepository, cartItemRepository, cartRepository);
//            OrderRequestDto orderRequestDto = null;
//            //when
//
//            //then
//            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> orderService.checkStock(orderRequestDto, product));
//            assertEquals("주문 정보가 존재하지 않습니다.", exception.getMessage());
//        }
//
//        @Test
//        @DisplayName("checkStock - product가 null일 때")
//        void checkStockFail2() {
//            //given
//            OrderService orderService = new OrderService(orderRepository, productRepository, cartItemRepository, cartRepository);
//            Product product = null;
//            //when
//
//            //then
//            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> orderService.checkStock(orderRequestDto, product));
//            assertEquals("상품 정보가 존재하지 않습니다.", exception.getMessage());
//        }
//
//        @Test
//        @DisplayName("checkStock - 주문 수량이 재고보다 많을 때")
//        void checkStockFail3() {
//            //given
//            OrderService orderService = new OrderService(orderRepository, productRepository, cartItemRepository, cartRepository);
//            orderRequestDto.setQuantity(100);
//            //when
//
//            //then
//            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> orderService.checkStock(orderRequestDto, product));
//            assertEquals("주문 가능 수량을 초과하였습니다", exception.getMessage());
//        }
//    }
//}
