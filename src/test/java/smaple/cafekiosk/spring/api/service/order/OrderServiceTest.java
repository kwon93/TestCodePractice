package smaple.cafekiosk.spring.api.service.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import smaple.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import smaple.cafekiosk.spring.api.service.order.response.OrderResponse;
import smaple.cafekiosk.spring.domain.order.OrderRepository;
import smaple.cafekiosk.spring.domain.orderProduct.OrderProduct;
import smaple.cafekiosk.spring.domain.orderProduct.OrderProductRepository;
import smaple.cafekiosk.spring.domain.product.Product;
import smaple.cafekiosk.spring.domain.product.ProductRepository;
import smaple.cafekiosk.spring.domain.product.ProductSellingStatus;
import smaple.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;
    @AfterEach
    void tearDown() { //연관 관계로 인한 순서 주의
        orderProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("createOrder(): 주문 번호 리스트를 받아 주문을 생성한다.")
    void createOrder() throws Exception{
         //given


        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct(ProductType.HANDMADE, "001",1000);
        Product product2 = createProduct(ProductType.HANDMADE, "002",3000);
        Product product3 = createProduct(ProductType.HANDMADE, "003",5000);
        productRepository.saveAll(List.of(product1, product2, product3));


        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumber(List.of("001", "002"))
                .build();

        //when
        OrderResponse orderResponse = orderService.createOrder(request,registeredDateTime);

        //then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .contains(registeredDateTime,4000);
        assertThat(orderResponse.getProducts()).hasSize(2)
                .extracting("productNumber","price")
                .containsExactlyInAnyOrder(
                        tuple("001",1000),
                        tuple("002",3000)
                );

    }

    @Test
    @DisplayName("중복되는 상품번호 리스트로 주문을 생성 할 수 있다.")
    void createOrderWithDuplicateProductNumbers() throws Exception{
        //given


        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct(ProductType.HANDMADE, "001",1000);
        Product product2 = createProduct(ProductType.HANDMADE, "002",3000);
        Product product3 = createProduct(ProductType.HANDMADE, "003",5000);
        productRepository.saveAll(List.of(product1, product2, product3));


        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumber(List.of("001", "001"))
                .build();



        //when
        OrderResponse orderResponse = orderService.createOrder(request,registeredDateTime);

        //then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .contains(registeredDateTime,2000);
        assertThat(orderResponse.getProducts()).hasSize(2)
                .extracting("productNumber","price")
                .containsExactlyInAnyOrder(
                        tuple("001",1000),
                        tuple("001",1000)
                );

    }

    private Product createProduct(ProductType type, String productNumber,int price ){
        return Product.builder()
                .type(type)
                .productNumber(productNumber)
                .price(price)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("메뉴 이름")
                .build();
    }
}