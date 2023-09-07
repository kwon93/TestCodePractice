package smaple.cafekiosk.spring.api.controller.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import smaple.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import smaple.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import smaple.cafekiosk.spring.api.service.order.OrderService;
import smaple.cafekiosk.spring.domain.order.Order;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static smaple.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static smaple.cafekiosk.spring.domain.product.ProductType.HANDMADE;

@WebMvcTest(controllers = Ordercontroller.class)
class OrdercontrollerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("신규 주문을 요청이 왔을때 요청에 성공적으로 응답할 수 있어야 한다.")
    void createOder() throws Exception{
        //given
        final String url = "/api/v1/orders/new";
        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumber(List.of("001", "002"))
                .build();


        //when //then
        mockMvc.perform(
                        post(url)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @Test
    @DisplayName("상품의번호가 요청에 존재하지않으면 요청 응답에 실패해야한다.")
    void createOderWithoutProductNumber() throws Exception{
        //given
        final String url = "/api/v1/orders/new";
        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumber(List.of())
                .build();
        //when //then
        mockMvc.perform(
                        post(url)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품의 번호를 입력해주세요."))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}