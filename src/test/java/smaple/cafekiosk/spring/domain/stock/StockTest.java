package smaple.cafekiosk.spring.domain.stock;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class StockTest {

    @Test
    @DisplayName("재고의 수량이 외부에서 제공된 수량보다 적은지 확인에 성공하기")
    void isQuantityLessThan() throws Exception{
        //given
        Stock stock =  Stock.create("001",1);
        int quantity = 2; // 경계값 테스트

        //when
        boolean result = stock.isQuantityLessThan(quantity);
        
        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("재고를 주어진 개수만큼 차감할 수 있다.")
    void deductQuantity1() throws Exception{
         //given
        Stock stock = Stock.create("001", 3);
        int quantity = 3;
        //when
        stock.deductQuantity(quantity);
         //then
        assertThat(stock.getQuantity()).isZero();

    }

    @Test
    @DisplayName("재고보다 많은수의 수량으로 차감시도하는 경우 예외가 발생한다. ")
    void deductQuantity2() throws Exception{
         //given
        Stock stock = Stock.create("001", 1);
        int quantity = 2;
        //when //then
        assertThatThrownBy(()->stock.deductQuantity(quantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("차감할 수량의 재고가 없습니다.");
        assertThat(stock.getQuantity()).isEqualTo(1);
    }



    @TestFactory
    @DisplayName("재고차감 시나리오")
    Collection<DynamicTest> stockDeductionDynamicTest() throws Exception {
        //given 공통 환경(자원) 설정
        Stock stock = Stock.create("001", 1);

        return List.of(
                DynamicTest.dynamicTest("재고를 주어진 개수만큼 차감할 수 있다.",()->{
                    //given
                    int quantity = 1;
                    //when
                    stock.deductQuantity(quantity);
                    //then
                    assertThat(stock.getQuantity()).isZero();
                }),

                DynamicTest.dynamicTest("재고보다 많은 수의 수량으로 차감 시도를 하는경우 예외가 발생한다.",()-> {
                    //given
                    int quantity = 1;
                    //when //then
                    assertThatThrownBy(()->stock.deductQuantity(quantity))
                            .isInstanceOf(IllegalArgumentException.class)
                            .hasMessage("차감할 수량의 재고가 없습니다.");
                })

        );
    }




}