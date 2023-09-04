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
        Long quantity = 2L; // 경계값 테스트

        //when
        boolean result = stock.isQuantityLessThan(quantity);
        
        //then
        assertThat(result).isTrue();
    }

}