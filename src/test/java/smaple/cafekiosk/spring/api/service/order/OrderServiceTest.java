package smaple.cafekiosk.spring.api.service.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import smaple.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import smaple.cafekiosk.spring.api.service.order.request.OrderCreateServiceRequest;
import smaple.cafekiosk.spring.api.service.order.response.OrderResponse;
import smaple.cafekiosk.spring.domain.order.OrderRepository;
import smaple.cafekiosk.spring.domain.orderProduct.OrderProduct;
import smaple.cafekiosk.spring.domain.orderProduct.OrderProductRepository;
import smaple.cafekiosk.spring.domain.product.Product;
import smaple.cafekiosk.spring.domain.product.ProductRepository;
import smaple.cafekiosk.spring.domain.product.ProductSellingStatus;
import smaple.cafekiosk.spring.domain.product.ProductType;
import smaple.cafekiosk.spring.domain.stock.Stock;
import smaple.cafekiosk.spring.domain.stock.StockRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
//@Transactional
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private StockRepository stockRepository;

    @AfterEach
    void tearDown() { //연관 관계로 인한 순서 주의
        orderProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        stockRepository.deleteAllInBatch();
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


        OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
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


        OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
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

    @Test
    @DisplayName("재고와 관련된 상품이 포함되어 있는 주문번호 리스트를 받아 주문을 생성한다.")
    void createOrderWithStock() throws Exception{
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct(ProductType.BOTTEL, "001",1000);
        Product product2 = createProduct(ProductType.BAKERY, "002",3000);
        Product product3 = createProduct(ProductType.HANDMADE, "003",5000);
        productRepository.saveAll(List.of(product1, product2, product3));

        Stock stock1 = Stock.create("001",2);
        Stock stock2 = Stock.create("002",2);
        stockRepository.saveAll(List.of(stock1,stock2));

        OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
                .productNumber(List.of("001","001","002","003"))
                .build();

        //when
        OrderResponse orderResponse = orderService.createOrder(request,registeredDateTime);

        //then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .contains(registeredDateTime,10000);
        assertThat(orderResponse.getProducts()).hasSize(4)
                .extracting("productNumber","price")
                .containsExactlyInAnyOrder(
                        tuple("001",1000),
                        tuple("001",1000),
                        tuple("002",3000),
                        tuple("003",5000)
                );

        List<Stock> stocks = stockRepository.findAll();
        assertThat(stocks).hasSize(2)
                .extracting("productNumber","quantity")
                .containsExactlyInAnyOrder(
                        tuple("001",0),
                        tuple("002",1)

                );

    }

    @Test
    @DisplayName("재고가 부족한 상품으로 주문을 생성하려는 경우 예외가 발생한다.")
    void createOrderWithNoStock() throws Exception{
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct(ProductType.BOTTEL, "001",1000);
        Product product2 = createProduct(ProductType.BAKERY, "002",3000);
        Product product3 = createProduct(ProductType.HANDMADE, "003",5000);
        productRepository.saveAll(List.of(product1, product2, product3));

        Stock stock1 = Stock.create("001",2);
        Stock stock2 = Stock.create("002",2);
        stock1.deductQuantity(1);
        stockRepository.saveAll(List.of(stock1,stock2));

        OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
                .productNumber(List.of("001","001","002","003"))
                .build();

        //when //then
        assertThatThrownBy(()-> orderService.createOrder(request,registeredDateTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고가 부족한 상품이 있습니다.");
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