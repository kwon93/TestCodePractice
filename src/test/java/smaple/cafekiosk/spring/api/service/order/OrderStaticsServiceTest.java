package smaple.cafekiosk.spring.api.service.order;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import smaple.cafekiosk.spring.domain.order.Order;
import smaple.cafekiosk.spring.domain.order.OrderRepository;
import smaple.cafekiosk.spring.domain.product.Product;
import smaple.cafekiosk.spring.domain.product.ProductRepository;
import smaple.cafekiosk.spring.domain.product.ProductSellingStatus;
import smaple.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderStaticsServiceTest {

    @Autowired
    private OrderStaticsService orderStaticsService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
    }


    @Test
    @DisplayName("결제완료 주문들을 조회하여 매출 통계 메일을 전송한다.")
    void sendOrderStaticsMail() throws Exception{
        //given
        LocalDateTime now = LocalDateTime.of(2023, 9, 7, 9, 48);

        Product product1 = createProduct("001", ProductType.HANDMADE, 1000);
        Product product2 = createProduct("002", ProductType.HANDMADE, 2000);
        Product product3 = createProduct("003", ProductType.HANDMADE, 3000);
        productRepository.saveAll(List.of(product1, product2, product3));

        Order order1 = Order.create(List.of(product1, product2, product3), now);
        orderRepository.save(order1);
        //when

        //then
    }

    private Product createProduct(String productNumber,
                                  ProductType type,
                                  int productPrice) {
        return Product.builder()
                .productNumber(productNumber)
                .type(type)
                .price(productPrice)
                .build();
    }
}