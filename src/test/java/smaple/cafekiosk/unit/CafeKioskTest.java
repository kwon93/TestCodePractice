package smaple.cafekiosk.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import smaple.cafekiosk.unit.beverage.Americano;
import smaple.cafekiosk.unit.orders.Order;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class CafeKioskTest {

    //수동적 테스트
    @Test
    @DisplayName("음료의 개수를 더하기에 성공한다")
    void test1() throws Exception{
        //given
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano(),1);

        System.out.println("키오스크에 담긴 음료의 개수 : "+cafeKiosk.getBeverages());
        System.out.println("키오스크에 담긴 음료의 개수 : "+cafeKiosk.getBeverages().get(0).getName());
        //when

        //then
    }

    /**
     * 가게 운영시간안에만 성공하게되는 테스트가 된다.
     * @throws Exception (가게운영시간이 아닐시 예외 처리)
     */
    @Test
    @DisplayName("createOrder(): 주문에 성공하기")
    void test2() throws Exception{
         //given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        cafeKiosk.add(americano, 1);
        //when
        Order order = cafeKiosk.createOrder();

        //then

        assertThat(order.getBeverages()).hasSize(1);
        assertThat(order.getBeverages().get(0)).isEqualTo(americano);
    }

    /**
     * currentTime을 매개변수로 받아 테스트를 진행한다.
     * 운영시간에 의존하지않고 테스트 가능.
     * @throws Exception
     */
    @Test
    @DisplayName("createOrder(): 주문에 성공하기")
    void test3() throws Exception{
        //given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        cafeKiosk.add(americano, 1);
        //when
        Order order = cafeKiosk.createOrder(LocalDateTime.of(2023,8,30,19,00));

        //then

        assertThat(order.getBeverages()).hasSize(1);
        assertThat(order.getBeverages().get(0)).isEqualTo(americano);
    }

    @Test
    @DisplayName("createOrder(): 영업시간 외 주문에는 예외처리에 성공하기")
    void test4() throws Exception{
        //given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        cafeKiosk.add(americano, 1);

        //then
        assertThatThrownBy(()->
                cafeKiosk.createOrder(LocalDateTime.of(2023,8,30,23,00)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 시간이 아닙니다. 관리자에게 문의해주세요.");
    }



}