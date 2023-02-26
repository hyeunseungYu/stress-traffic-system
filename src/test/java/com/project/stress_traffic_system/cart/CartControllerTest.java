//
//package com.project.stress_traffic_system.cart;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.project.stress_traffic_system.jwt.JwtUtil;
//import com.project.stress_traffic_system.members.entity.Members;
//import com.project.stress_traffic_system.members.entity.MembersRoleEnum;
//import com.project.stress_traffic_system.members.service.MembersService;
//import com.project.stress_traffic_system.security.UserDetailsImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
///*@Transactional을 사용하여 실제 DB에 영향을 주지 않는다(rollback)*/
//@SpringBootTest
//@AutoConfigureMockMvc
//public class CartControllerTest {
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//    @Autowired
//    JwtUtil jwtUtil;
//
//    @MockBean
//    MembersService memberService;
//
//    UserDetailsImpl userDetails;
//
//    @BeforeEach
//    void setAuthUser() {
//        //zser28 유저에게 인가
//        Authentication authentication = jwtUtil.createAuthentication("zser28");
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        //인증,인가 및 장바구니 조회를 할 유저정보
//        String username = "zser28";
//        String password = "abcd1234?";
//        String address = "동두천";
//
//        Members member  = new Members(username, password, address, MembersRoleEnum.MEMBER);
//
//        userDetails = UserDetailsImpl
//                .builder()
//                .members(member)
//                .username(username)
//                .password(password)
//                .build();
//    }
//
//    @Test
//    @Transactional
//    @DisplayName("장바구니 상품목록 조회")
//    public void getCartItems() throws Exception{
//        //given
//        // 응답받는 json list의 경로설정
//        String itemName = "$.[?(@.itemName == '%s')]"; //장바구니 아이템 이름
//        String imgurl = "$.[?(@.imgurl == '%s')]"; // 아이템 이미지
//        String price = "$.[?(@.price == '%s')]"; // 아이템 가격
//        String quantity = "$.[?(@.quantity == '%s')]"; //아이템 주문 수량
//
//        // when & then
//        mockMvc.perform(get("/api/products/cart")
//                        .content(objectMapper.writeValueAsString(userDetails))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath(itemName,"Thomasena Lubowitz").exists())
//                .andExpect(jsonPath(imgurl,3).exists())
//                .andExpect(jsonPath(price,26168).exists()) //30% DC된 가격
//                .andExpect(jsonPath(quantity,1).exists());
//    }
//
//    @Test
//    @Transactional
//    @DisplayName("장바구니에 상품 추가")
//    public void addToCart() throws Exception {
//        //given
//        String productId = "1";
//
//        // when & then
//        mockMvc.perform(post("/api/products/cart/"+productId)
//                        .content(objectMapper.writeValueAsString(userDetails))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(content().string("success"));
//    }
//
//    @Test
//    @Transactional
//    @DisplayName("장바구니에 상품 수량 변경")
//    public void updateQuantity() throws Exception {
//        //given
//        //해당 유저의 1번 상품의 개수를 5로 수정
//        String productId = "1";
//        String updateNum = "5";
//
//        // when & then
//        mockMvc.perform(patch("/api/products/"+productId+"/cart-update/"+updateNum)
//                        .content(objectMapper.writeValueAsString(userDetails))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(content().string("success"));
//    }
//
//    @Test
//    @Transactional
//    @DisplayName("장바구니 단일 상품 삭제")
//    public void deleteProduct() throws Exception {
//        //given
//        //1번 상품 삭제
//        String productId = "1";
//
//        // when & then
//        mockMvc.perform(delete("/api/products/"+productId+"/cart-delete")
//                        .content(objectMapper.writeValueAsString(userDetails))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(content().string("success"));
//    }
//
//    @Test
//    @Transactional
//    @DisplayName("장바구니 비우기")
//    public void emptyCart() throws Exception {
//
//        // when & then
//        mockMvc.perform(delete("/api/products/cart-empty")
//                        .content(objectMapper.writeValueAsString(userDetails))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(content().string("success"));
//    }
//
//}
