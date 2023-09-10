package smaple.cafekiosk.spring.api.service.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import smaple.cafekiosk.spring.IntegrationTestSupport;
import smaple.cafekiosk.spring.client.mail.MailSendClient;
import smaple.cafekiosk.spring.domain.history.mail.MailSendHistory;
import smaple.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;
import smaple.cafekiosk.spring.domain.order.Order;
import smaple.cafekiosk.spring.domain.order.OrderRepository;
import smaple.cafekiosk.spring.domain.order.OrderStatus;
import smaple.cafekiosk.spring.domain.orderProduct.OrderProductRepository;
import smaple.cafekiosk.spring.domain.product.Product;
import smaple.cafekiosk.spring.domain.product.ProductRepository;
import smaple.cafekiosk.spring.domain.product.ProductSellingStatus;
import smaple.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderStaticsServiceTest extends IntegrationTestSupport {

    @Autowired
    private OrderStaticsService orderStaticsService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MailSendHistoryRepository historyRepository;


    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        historyRepository.deleteAllInBatch();
    }


    @Test
    @DisplayName("결제완료 주문들을 조회하여 매출 통계 메일을 전송한다.")
    void sendOrderStaticsMail() throws Exception{
        //given
        LocalDateTime now = LocalDateTime.of(2023, 3, 5, 0, 0);

        Product product1 = createProduct("001", ProductType.HANDMADE, 1000);
        Product product2 = createProduct("002", ProductType.HANDMADE, 2000);
        Product product3 = createProduct("003", ProductType.HANDMADE, 3000);
        productRepository.saveAll(List.of(product1, product2, product3));
        List<Product> products = List.of(product1, product2, product3);

        //경계값 테스트 진행.
        Order order1 = createPaymentCompletedOrder(products, LocalDateTime.of(2023,3,4,23,59,59));
        Order order2 = createPaymentCompletedOrder(products, now);
        Order order3 = createPaymentCompletedOrder(products, LocalDateTime.of(2023,3,5,23,59,59));
        Order order4 = createPaymentCompletedOrder(products, LocalDateTime.of(2023,3,6,0,0));

        //stubbing
        when(mailSendClient.sendEmail(
                any(String.class),
                any(String.class),
                any(String.class),
                any(String.class)
                )).thenReturn(true);
        //when
        boolean result = orderStaticsService.sendOrderStaticsMail(LocalDate.of(2023, 3, 5), "test@test.com");

        //then
        assertThat(result).isTrue();
        List<MailSendHistory> histories = historyRepository.findAll();
        assertThat(histories).hasSize(1)
                .extracting("content")
                .contains("총 매출 합계는 12000원입니다.");
    }

    private Order createPaymentCompletedOrder(List<Product> products, LocalDateTime now) {
        Order order = Order.builder()
                .products(products)
                .orderStatus(OrderStatus.PAYMENT_COMPLETED)
                .registeredDateTime(now)
                .build();

        return orderRepository.save(order);
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