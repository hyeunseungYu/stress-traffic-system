package com.project.stress_traffic_system.order.service;

import com.project.stress_traffic_system.cart.model.Cart;
import com.project.stress_traffic_system.cart.model.CartItem;
import com.project.stress_traffic_system.cart.repository.CartItemRepository;
import com.project.stress_traffic_system.cart.repository.CartRepository;
import com.project.stress_traffic_system.members.entity.Members;
import com.project.stress_traffic_system.order.model.OrderItem;
import com.project.stress_traffic_system.order.model.Orders;
import com.project.stress_traffic_system.order.model.dto.OrderDetailDto;
import com.project.stress_traffic_system.order.model.dto.OrderDto;
import com.project.stress_traffic_system.order.model.dto.OrderListDto;
import com.project.stress_traffic_system.order.model.dto.OrderRequestDto;
import com.project.stress_traffic_system.order.repository.OrderRepository;
import com.project.stress_traffic_system.product.model.Product;
import com.project.stress_traffic_system.product.model.dto.ProductResponseDto;
import com.project.stress_traffic_system.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final RedissonClient redissonClient;

    private final RedisCacheManager redisCacheManager;

    //단일 상품 주문하기
    @Transactional
    public OrderDto orderOne(Members member, OrderRequestDto requestDto) {


        Cart cart = findCart(member);   //회원의 장바구니 가져오기
        Product product = checkProduct(requestDto); //상품정보
        checkStock(requestDto, product); //재고가 있는지 확인
        checkQuantity(requestDto);  //주문수량이 유효한지 확인

        //주문수량만큼 상품 테이블에 총 주문수량 증가시킨다
        product.setOrderCount(product.getOrderCount() + requestDto.getQuantity());

        //주문상품 객체 만들기(생성메서드)
        //주문 수량만큼 재고 차감
        OrderItem orderItem = OrderItem.createOrderItem(product, requestDto);
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);

        //주문 객체 생성해서 회원정보와 주문상품 정보 저장
        Orders order = Orders.createOrder(member, orderItems);

        //주문정보 저장
        Orders savedOrders = orderRepository.save(order);

        deleteCartItem(cart, product); //장바구니에서 주문상품 삭제한다


        //주문내역 반환 (주문번호와, 주문일자)
        return OrderDto.builder()
                .orderId(savedOrders.getId())
                .orderDate(savedOrders.getCreatedAt())
                .resultStock(product.getStock())
                .build();
    }

    //단일 상품 주문하기 - pessimistic lock
    @Transactional
    public OrderDto orderOneWithPessimisticLock(Members member, OrderRequestDto requestDto) {


        Cart cart = findCart(member);   //회원의 장바구니 가져오기
        Product product = checkProductWithPessimisticLock(requestDto); //상품정보
        checkStock(requestDto, product); //재고가 있는지 확인
        checkQuantity(requestDto);  //주문수량이 유효한지 확인

        //주문수량만큼 상품 테이블에 총 주문수량 증가시킨다
        product.setOrderCount(product.getOrderCount() + requestDto.getQuantity());

        //주문상품 객체 만들기(생성메서드)
        //주문 수량만큼 재고 차감
        OrderItem orderItem = OrderItem.createOrderItem(product, requestDto);
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);

        //주문 객체 생성해서 회원정보와 주문상품 정보 저장
        Orders order = Orders.createOrder(member, orderItems);

        //주문정보 저장
        Orders savedOrders = orderRepository.save(order);

        deleteCartItem(cart, product); //장바구니에서 주문상품 삭제한다


        //주문내역 반환 (주문번호와, 주문일자)
        return OrderDto.builder()
                .orderId(savedOrders.getId())
                .orderDate(savedOrders.getCreatedAt())
                .resultStock(product.getStock())
                .build();
    }

    //여러 상품 주문하기
    @Transactional
    public OrderDto orderMany(Members member, List<OrderRequestDto> requestDtoList) {

        Cart cart = findCart(member); //회원의 장바구니 가져오기

        List<OrderItem> orderItems = new ArrayList<>(); //주문상품 목록
        List<Integer> resultStock = new ArrayList<>(); //차감된 재고 수량을 담을 목록

        //주문상품 리스트에 주문상품 목록을 담아준다
        for (OrderRequestDto orderRequestDto : requestDtoList) {

            Product product = checkProduct(orderRequestDto); //상품정보 가져오기
            checkStock(orderRequestDto, product);   //재고확인하기
            checkQuantity(orderRequestDto); //주문수량 유효한지 확인하기

            //주문수량만큼 상품 테이블에 총 주문수량 증가시킨다
            product.setOrderCount(product.getOrderCount() + orderRequestDto.getQuantity());

            //주문상품 객체 생성해서 list에 추가
            //주문 수량만큼 재고 차감
            OrderItem orderItem = OrderItem.createOrderItem(product, orderRequestDto);
            orderItems.add(orderItem);

//            //주문 수량만큼 재고 차감.
//            product.removeStock(orderRequestDto.getQuantity());
            resultStock.add(product.getStock());

            deleteCartItem(cart, product); //장바구니에서 주문상품 삭제한다
        }

        //주문 객체 생성해서 회원정보와 주문상품 정보 저장
        Orders order = Orders.createOrder(member, orderItems);

        Orders savedOrder = orderRepository.save(order);

        //주문내역 반환(주문번호와 주문일자)
        return OrderDto.builder()
                .orderId(savedOrder.getId())
                .orderDate(savedOrder.getCreatedAt())
                .resultStockList(resultStock)
                .build();
    }

    //여러 상품 주문하기 - pessimistic lock
    @Transactional
    public OrderDto orderManyWithPessimisticLock(Members member, List<OrderRequestDto> requestDtoList) {

        Cart cart = findCart(member); //회원의 장바구니 가져오기

        List<OrderItem> orderItems = new ArrayList<>(); //주문상품 목록
        List<Integer> resultStock = new ArrayList<>(); //차감된 재고 수량을 담을 목록

        //주문상품 리스트에 주문상품 목록을 담아준다
        for (OrderRequestDto orderRequestDto : requestDtoList) {

            Product product = checkProductWithPessimisticLock(orderRequestDto); //상품정보 가져오기
            checkStock(orderRequestDto, product);   //재고확인하기
            checkQuantity(orderRequestDto); //주문수량 유효한지 확인하기

            //주문수량만큼 상품 테이블에 총 주문수량 증가시킨다
            product.setOrderCount(product.getOrderCount() + orderRequestDto.getQuantity());

            //주문상품 객체 생성해서 list에 추가
            //주문 수량만큼 재고 차감
            OrderItem orderItem = OrderItem.createOrderItem(product, orderRequestDto);
            orderItems.add(orderItem);

//            //주문 수량만큼 재고 차감.
//            product.removeStock(orderRequestDto.getQuantity());
            resultStock.add(product.getStock());

            deleteCartItem(cart, product); //장바구니에서 주문상품 삭제한다
        }

        //주문 객체 생성해서 회원정보와 주문상품 정보 저장
        Orders order = Orders.createOrder(member, orderItems);

        Orders savedOrder = orderRepository.save(order);

        //주문내역 반환(주문번호와 주문일자)
        return OrderDto.builder()
                .orderId(savedOrder.getId())
                .orderDate(savedOrder.getCreatedAt())
                .resultStockList(resultStock)
                .build();
    }

    @Transactional
    public OrderDto orderManyWithRedissonLock(Members member, List<OrderRequestDto> requestDtoList) {

        Cart cart = findCart(member); //회원의 장바구니 가져오기

        List<OrderItem> orderItems = new ArrayList<>(); //주문상품 목록
        List<Integer> resultStock = new ArrayList<>(); //차감된 재고 수량을 담을 목록


        //주문상품 리스트에 주문상품 목록을 담아준다
        for (OrderRequestDto orderRequestDto : requestDtoList) {

            RLock lock = redissonClient.getLock(orderRequestDto.getProductId().toString()); //lock 생성

            try {
                boolean available = lock.tryLock(5, 1, TimeUnit.SECONDS);
                if (!available) {
                    log.info("lock 획득 실패");
                }

                Product product = checkProductWithPessimisticLock(orderRequestDto); //상품정보 가져오기
                checkStock(orderRequestDto, product);   //재고확인하기
                checkQuantity(orderRequestDto); //주문수량 유효한지 확인하기

                //주문수량만큼 상품 테이블에 총 주문수량 증가시킨다
                product.setOrderCount(product.getOrderCount() + orderRequestDto.getQuantity());

                //주문상품 객체 생성해서 list에 추가
                //주문 수량만큼 재고 차감
                OrderItem orderItem = OrderItem.createOrderItem(product, orderRequestDto);
                orderItems.add(orderItem);

                resultStock.add(product.getStock());

                deleteCartItem(cart, product); //장바구니에서 주문상품 삭제한다

            } catch (InterruptedException e) {
                throw new RuntimeException(e);

            } finally {
                if (lock != null && lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }

        //주문 객체 생성해서 회원정보와 주문상품 정보 저장
        Orders order = Orders.createOrder(member, orderItems);

        Orders savedOrder = orderRepository.save(order);

        //주문내역 반환(주문번호와 주문일자)
        return OrderDto.builder()
                .orderId(savedOrder.getId())
                .orderDate(savedOrder.getCreatedAt())
                .resultStockList(resultStock)
                .build();
    }

    //주문내역 리스트 가져오기
    @Transactional(readOnly = true)
    public List<OrderListDto> getOrders(Members member) {
        List<Orders> orderList = orderRepository.findAllByMembersOrderByCreatedAtAsc(member);
        log.info("orderList 사이즈는 = {}", orderList.size());

        return orderList.stream().map(
                orders -> OrderListDto.builder()
                        .orderId(orders.getId())
                        .orderDate(orders.getCreatedAt())
                        .itemName(getItemName(orders))
                        .totalQuantity(orders.getTotalQuantity())
                        .totalPrice(orders.getTotalPrice())
                        .build()
        ).collect(Collectors.toList());
    }

    //주문 상세내역 가져오기
    @Transactional(readOnly = true)
    public List<OrderDetailDto> getOrderDetail(Members member, Long orderId) {
        Orders order = orderRepository.findById(orderId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 주문입니다")
        );

        if (!order.getMembers().getId().equals(member.getId())) {
            throw new IllegalArgumentException("본인의 주문내역만 볼 수 있습니다");
        }

        List<OrderDetailDto> response = new ArrayList<>();

        //주문내역 안의 각각의 주문 상품들 모두 dto로 가져오기
        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            response.add(OrderDetailDto.builder()
                    .orderId(order.getId())
                    .name(orderItem.getProduct().getName())
                    .quantity(orderItem.getQuantity())
                    .price(orderItem.getProduct().getPrice())
                    .totalPrice(order.getTotalPrice())
                    .orderDate(order.getCreatedAt())
                    .status(order.getStatus())
                    .build());
        }
        return response;
    }

    //주문상품 이름 반환하기
    private String getItemName(Orders orders) {
        if (orders.getOrderItems().size() == 1) {
            return orders.getOrderItems().get(0).getProduct().getName();
        }

        //상품 갯수가 여러개이면 "ㅇㅇ 외" 로 표시
        return orders.getOrderItems().get(0).getProduct().getName() + " 외";
    }

    //주문수량이 적정한지
    protected void checkQuantity(OrderRequestDto requestDto) {
        if (requestDto.getQuantity() < 1) {
            throw new IllegalArgumentException("최소 1개 이상 주문해주세요");
        }
    }

    //상품이 존재하는지 확인
    protected Product checkProduct(OrderRequestDto requestDto) {
        return productRepository.findById(requestDto.getProductId()).orElseThrow(
                () -> new IllegalArgumentException("상품이 존재하지 않습니다")
        );
    }

    //상품이 존재하는지 확인 - pessimisticLock
    public Product checkProductWithPessimisticLock(OrderRequestDto requestDto) {
        return productRepository.findByIdWithPessimisticLock(requestDto.getProductId());
    }

    //재고수량 확인하기
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

    //회원 장바구니 가져오기
    protected Cart findCart(Members member) {
        return cartRepository.findByMember(member);
    }

    //장바구니에서 주문한 아이템 삭제하기
    private void deleteCartItem(Cart cart, Product product) {
        Optional<CartItem> cartItem = cartItemRepository.findByCartAndProduct(cart, product);

        //장바구니에서 주문상품이 존재하면 장바구니에서 삭제해버린다
        if (cartItem.isPresent()) {
            cartItemRepository.deleteByCartAndProduct(cart, product);
        }
    }

    //스레드 테스트를 위한 메서드
    @Transactional
    public void decrease(Long id, int quantity) {
        Product product = productRepository.findByIdWithPessimisticLock(id);
        product.removeStock(quantity);
        productRepository.save(product);

    }

    //==============Look-aside 테스트를 위한 메서드==============
    @Transactional(readOnly = true)
    //cacheable을 사용하면 밑에 productCache.put을 안 써도 됩니다. 다만 우리의 캐싱 전략에서 언제 캐싱이 되는지 좀 더 명시적으로 아는 게 필요하다고 판단해서 사용하지 않았습니다.
//    @Cacheable("productId")
    public ProductResponseDto findProductInCache(Long productId) {
        Cache productCache = redisCacheManager.getCache("productId"); //getCache는 캐시 이름을 기준으로 캐시를 가져옴
        Cache.ValueWrapper valueWrapper = productCache.get(String.valueOf(productId)); //가져온 캐시에서 키(key)를 기준으로 캐시를 찾고, 그것의 value를 가져오기 위해 사용

        //cache hit의 경우
        if (valueWrapper != null) {
            //value Wrapper는 래퍼 클래스니까 밸류를 get으로 가져와야 함.
            return (ProductResponseDto) valueWrapper.get();
        }else {
            //cache miss의 경우
            //db에서 데이터를 가져와서 반환
            //그리고 캐시에 저장
            Product product = productRepository.findById(productId).orElseThrow(
                    () -> new IllegalArgumentException("해당 상품이 존재하지 않음")
            );

            ProductResponseDto build = ProductResponseDto.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .build();

            productCache.put(String.valueOf(productId), build);
            return build;
        }
    }

    //==============Write-through 테스트를 위한 메서드==============
    @Transactional
    public ProductResponseDto saveProductInCache(ProductResponseDto request) {
        Product product = new Product(request);

        ProductResponseDto build = ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .build();

        productRepository.save(product);

        //캐시 객체 생성
        //캐시가 없으면 productId라는 이름으로 캐시를 생성하고, 존재하면 productId에 이어서 추가하기 위해 getCache로 객체를 만들어줌.
        Cache productCache = redisCacheManager.getCache("productId");

        productCache.put(String.valueOf(product.getId()), build);

        return build;
    }



}
