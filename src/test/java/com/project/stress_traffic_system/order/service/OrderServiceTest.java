package com.project.stress_traffic_system.order.service;

import com.project.stress_traffic_system.cart.model.Cart;
import com.project.stress_traffic_system.cart.model.CartItem;
import com.project.stress_traffic_system.cart.repository.CartItemRepository;
import com.project.stress_traffic_system.cart.repository.CartRepository;
import com.project.stress_traffic_system.cart.service.CartService;
import com.project.stress_traffic_system.members.entity.Members;
import com.project.stress_traffic_system.members.entity.MembersRoleEnum;
import com.project.stress_traffic_system.members.repository.MembersRepository;
import com.project.stress_traffic_system.order.model.Orders;
import com.project.stress_traffic_system.order.model.dto.OrderDto;
import com.project.stress_traffic_system.order.model.dto.OrderRequestDto;
import com.project.stress_traffic_system.order.repository.OrderRepository;
import com.project.stress_traffic_system.product.model.Product;
import com.project.stress_traffic_system.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RedissonClient;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
@DisplayName("orderService 테스트")
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MembersRepository membersRepository;
    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private RedissonClient redissonClient;

    Members member;
    Cart cart;

    Product product;
    CartItem cartItem;
    OrderRequestDto orderRequestDto;

    @BeforeEach
    void beforeEach() {
        member = new Members("user", "1234", "test@test.com", "test", MembersRoleEnum.MEMBER);
        cart = new Cart(member);
        product = new Product(1L, 30, "testName", 16000, 50,0L){

        };
        cartItem = new CartItem(cart, product);

        orderRequestDto = new OrderRequestDto();
        orderRequestDto.setProductId(1L);
        orderRequestDto.setQuantity(10);
        orderRequestDto.setDiscount(1000f);
        orderRequestDto.setDcType("none");
    }



    @Nested
    @DisplayName("성공 케이스")
    class successCase {

        @Test
        @DisplayName("단일 상품 주문")
        void orderOne() {
            //given

            //orderRepository.save()메서드가 호출되면 가로채서 save()의 0번째 인자를 리턴하도록 함.
            when(orderRepository.save(any(Orders.class))).thenAnswer(invocation -> invocation.getArgument(0));

            //회원의 장바구니를 찾을 때 내가 미리 만들어둔 cart를 리턴해주기 위해
            Map<Members, Cart> testCartMap = new HashMap<>();
            testCartMap.put(member, cart);
            OrderService orderService = new OrderService(orderRepository, productRepository, cartItemRepository, cartRepository, redissonClient) {

                //findCart라는 메서드가 사용되면 내가 지정한 cart를 바로 가져오게 하기 위해
                //그냥 원래 service에 있던 코드를 그대로 가져와도 되는데, 그러면 when으로 가로채야 하는 코드가 더 생겨서 이렇게 작성함
                @Override
                protected Cart findCart(Members members) {
                    return testCartMap.get(member);
                }


                @Override
                protected Product checkProduct(OrderRequestDto requestDto) {
                    return product;
                }

                //service에 있는 대로 그대로 복사. override를 안하면 mock객체를 보는 게 아니어서 null값을 리턴함.
                @Override
                protected void checkStock(OrderRequestDto orderRequestDto, Product product) {
                    if (orderRequestDto == null) {
                        throw new IllegalArgumentException("주문 정보가 존재하지 않습니다.");
                    }
                    if (product == null) {
                        throw new IllegalArgumentException("상품 정보가 존재하지 않습니다.");
                    }
                    if (orderRequestDto.getQuantity() > product.getStock()) {
                        throw new IllegalArgumentException("주문 가능 수량을 초과하였습니다");
                    }
                }

                @Override
                protected void checkQuantity(OrderRequestDto requestDto) {
                    if (requestDto.getQuantity() < 1) {
                        throw new IllegalArgumentException("최소 1개 이상 주문해주세요");
                    }
                }
            };

            //when
            orderService.orderOne(member, orderRequestDto);

            //then
            //재고가 차감되었는지 확인
            Assertions.assertEquals(20, product.getStock());



        }

        @Test
        @DisplayName("주문내역 리스트 가져오기")
        void getOrders() {
        }

        @Test
        @DisplayName("주문 상세내역 가져오기")
        void getOrderDetail() {


        }

        @Test
        @DisplayName("상품이 존재하는지 확인 - pessimistic lock")
        void checkProductWithPessimisticLock() {
            //given
            when(productRepository.findByIdWithPessimisticLock(orderRequestDto.getProductId())).thenReturn(product);
            OrderService orderService = new OrderService(orderRepository, productRepository, cartItemRepository, cartRepository, redissonClient);
            //when
            Product findResult = orderService.checkProductWithPessimisticLock(orderRequestDto);
            //then
            Assertions.assertEquals(product, findResult);
        }

        @Test
        @DisplayName("상품이 존재하는지 확인")
        void checkProductTest() {
            //given
            when(productRepository.findById(orderRequestDto.getProductId())).thenReturn(Optional.ofNullable(product));
            OrderService orderService = new OrderService(orderRepository, productRepository, cartItemRepository, cartRepository, redissonClient);
            //when
            Product findResult = orderService.checkProduct(orderRequestDto);
            //then
            Assertions.assertEquals(product, findResult);

        }

        @Test
        @DisplayName("checkStock")
        void checkStockTest() {
            //given
            OrderService orderService = new OrderService(orderRepository, productRepository, cartItemRepository, cartRepository, redissonClient);
            //when

            //then
            Assertions.assertDoesNotThrow(() -> orderService.checkStock(orderRequestDto, product));
        }

    }


    @Nested
    @DisplayName("실패 케이스")
    class failCase {
        @Test
        @DisplayName("checkStock - orderRequestDto가 null일 때")
        void checkStockFail1() {
            //given
            OrderService orderService = new OrderService(orderRepository, productRepository, cartItemRepository, cartRepository, redissonClient);
            OrderRequestDto orderRequestDto = null;
            //when

            //then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> orderService.checkStock(orderRequestDto, product));
            assertEquals("주문 정보가 존재하지 않습니다.", exception.getMessage());
        }

        @Test
        @DisplayName("checkStock - product가 null일 때")
        void checkStockFail2() {
            //given
            OrderService orderService = new OrderService(orderRepository, productRepository, cartItemRepository, cartRepository, redissonClient);
            Product product = null;
            //when

            //then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> orderService.checkStock(orderRequestDto, product));
            assertEquals("상품 정보가 존재하지 않습니다.", exception.getMessage());
        }

        @Test
        @DisplayName("checkStock - 주문 수량이 재고보다 많을 때")
        void checkStockFail3() {
            //given
            OrderService orderService = new OrderService(orderRepository, productRepository, cartItemRepository, cartRepository,redissonClient);
            orderRequestDto.setQuantity(100);
            //when

            //then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> orderService.checkStock(orderRequestDto, product));
            assertEquals("주문 가능 수량을 초과하였습니다", exception.getMessage());
        }
    }
}
