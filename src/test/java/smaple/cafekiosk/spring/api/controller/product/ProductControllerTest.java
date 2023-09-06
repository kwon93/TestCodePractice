package smaple.cafekiosk.spring.api.controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.ModelResultMatchers;
import smaple.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import smaple.cafekiosk.spring.api.service.product.ProductService;
import smaple.cafekiosk.spring.domain.product.ProductSellingStatus;
import smaple.cafekiosk.spring.domain.product.ProductType;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static smaple.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static smaple.cafekiosk.spring.domain.product.ProductType.*;


/**
 * Presentation Layer 계층 테스트시 중요 사항
 * 클라이언트의 요청이 정확히 유효하게 들어왔는지 검증하기
 */

@WebMvcTest(controllers = ProductController.class) //컨트롤러관련 bean들만 띄워주는 테스트
class ProductControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean //mockito (Bean을 Mocking)
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;



    @Test
    @DisplayName("신규상품을 생성한다.")
    void createProduct() throws Exception{
        //given
        final String url = "/api/v1/product/new";
        ProductCreateRequest request
                = ProductCreateRequest.builder()
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("아메리카노")
                .price(4000)
                .build();

        //when //then
        mockMvc.perform(
                        post(url)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("신규 상품을 등록할 때 상품 타입은 필수 값이여야한다.")
    void createProductWithoutType() throws Exception{
        //given
        final String url = "/api/v1/product/new";
        ProductCreateRequest request
                = ProductCreateRequest.builder()
                .sellingStatus(SELLING)
                .name("아메리카노")
                .price(4000)
                .build();

        //when //then
        mockMvc.perform(
                        post(url)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

}