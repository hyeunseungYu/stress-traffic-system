package com.project.stress_traffic_system.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.stress_traffic_system.cart.controller.CartController;
import com.project.stress_traffic_system.cart.model.dto.CartResponseDto;
import com.project.stress_traffic_system.cart.service.CartService;
import com.project.stress_traffic_system.config.WebSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*전체적인 flow를 확인하는 통합테스트가 아닌 직접 서비스를 만드는 단위테스트로 진행*/
//Spring Security와의 의존성을 제거 ->컨트롤러의 테스트만 하기 위함
@WebMvcTest(controllers = CartController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class, // 추가
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfig.class)})
public class CartControllerTest2 {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CartService cartService;

    @Test
    @DisplayName("장바구니 상품목록 조회")
    public void getCartItems() throws Exception{
        //given
        //getCartItems 메서드의 return 값
        List<CartResponseDto> cartResponseDtos = new ArrayList<>();
        for (int i=1; i<=5; i++) {
            CartResponseDto cartResponseDto = CartResponseDto.builder()
                    .itemName("Great Expectation"+i)
                    .imgurl(10+i)
                    .price(16000+i)
                    .quantity(3+i)
                    .build();
            cartResponseDtos.add(cartResponseDto);
        }

        // json ArrayList의 첫번째 원소의 경로설정
        String itemName = "$.[?(@.itemName == '%s')]"; //장바구니 아이템 이름
        String imgurl = "$.[?(@.imgurl == '%s')]"; // 아이템 이미지
        String price = "$.[?(@.price == '%s')]"; // 아이템 가격
        String quantity = "$.[?(@.quantity == '%s')]"; //아이템 주문 수량

        //getCartItems에 인자값이 들어오면 cartResponseDtos을 리턴
        when(cartService.getCartItems(any())).thenReturn(cartResponseDtos);

        // when & then
        mockMvc.perform(get("/api/products/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath(itemName,"Great Expectation1").exists())
                .andExpect(jsonPath(imgurl,11).exists())
                .andExpect(jsonPath(price,16001).exists())
                .andExpect(jsonPath(quantity,4).exists());
    }

    @Test
    @DisplayName("장바구니에 상품 추가")
    public void addToCart() throws Exception{

        // when & then
        mockMvc.perform(post("/api/products/cart/1"))
                .andExpect(status().is2xxSuccessful()); //201
    }

    @Test
    @DisplayName("장바구니 상품 수량 변경")
    public void updateQuantity() throws Exception{
        //given
        String productId = "1";
        String quantity = "5";

        // when & then
        mockMvc.perform(patch("/api/products/"+productId+"/cart-update/"+quantity))
                .andExpect(status().is2xxSuccessful()); //201
    }

    @Test
    @DisplayName("장바구니 단일 상품 삭제")
    public void deleteProduct() throws Exception {
        //given
        String productId = "1";

        // when & then
        mockMvc.perform(delete("/api/products/"+productId+"/cart-delete"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("장바구니 비우기")
    public void emptyCart() throws Exception {
        // when & then
        mockMvc.perform(delete("/api/products/cart-empty"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("success"));
    }

}
