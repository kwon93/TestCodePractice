package smaple.cafekiosk.spring.domain.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import smaple.cafekiosk.spring.domain.product.Product;
import smaple.cafekiosk.spring.domain.product.ProductSellingStatus;
import smaple.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    @DisplayName("주문 생성시 상품 리스트에서 주문의 총 금액을 계산한다.")
    void calculateTotalPrice() throws Exception{
         //given
        List<Product> product = List.of(
                createProduct("001", 1000),
                createProduct("002", 2000)
        );

        LocalDateTime registeredDateTime = LocalDateTime.now();

        //when
        Order order = Order.create(product,registeredDateTime);

        //then

        assertThat(order.getTotalPrice()).isEqualTo(3000);

    }

    @Test
    @DisplayName("주문 생성시 주문 상태는 INIT이다.")
    void init() throws Exception{
        //given
        List<Product> product = List.of(
                createProduct("001", 1000),
                createProduct("002", 2000)
        );

        LocalDateTime registeredDateTime = LocalDateTime.now();

        //when
        Order order = Order.create(product,registeredDateTime);

        //then

        assertThat(order.getOrderStatus()).isEqualByComparingTo(OrderStatus.INIT); //ENUM 값 검증시에 사용

    }


    @Test
    @DisplayName("주문 생성시 주문 등록 시간을 기록해준다.")
    void registeredDateTime() throws Exception{
        //given

        LocalDateTime registeredDateTime = LocalDateTime.now();

        List<Product> product = List.of(
                createProduct("001", 1000),
                createProduct("002", 2000)
        );

        //when
        Order order = Order.create(product,registeredDateTime);

        //then

        assertThat(order.getRegisteredDateTime()).isEqualTo(registeredDateTime); //ENUM 값 검증시에 사용

    }
    private Product createProduct(String productNumber, int price ){
        return Product.builder()
                .type(ProductType.HANDMADE)
                .productNumber(productNumber)
                .price(price)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("메뉴 이름")
                .build();
    }

}