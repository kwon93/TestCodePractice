package smaple.cafekiosk.spring.domain.stock;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
    void deductQuantity2(){
         //given
        Stock stock = Stock.create("001", 1);
        int quantity = 1;
        //when //then
        assertThatThrownBy(()->stock.deductQuantity(quantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("차감할 수량의 재고가 없습니다.");
        assertThat(stock.getQuantity()).isEqualTo(1);
    }





}