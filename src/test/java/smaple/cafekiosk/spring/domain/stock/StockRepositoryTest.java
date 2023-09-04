package smaple.cafekiosk.spring.domain.stock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import smaple.cafekiosk.spring.domain.product.Product;
import smaple.cafekiosk.spring.domain.product.ProductSellingStatus;
import smaple.cafekiosk.spring.domain.product.ProductType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@DataJpaTest
public class StockRepositoryTest {

    @Autowired
    private StockRepository stockRepository;

    @Test
    @DisplayName("상품번호 리스트로 재고를 조회한다. ")
    void findAllByProductNumberIn() throws Exception{
        //given
        Stock stock1 = Stock.create("001", 1);
        Stock stock2 = Stock.create("002", 2);
        Stock stock3 = Stock.create("003", 3);

        stockRepository.saveAll(List.of(stock1,stock2,stock3));
        //when
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(List.of("001", "002"));
        //then
        assertThat(stocks).hasSize(2)
                .extracting("productNumber","quantity") //내가 검증하고자하는 필드만 추출 가능.
                .containsExactlyInAnyOrder( //리스트 검증시 사용되는 검증메서드 순서 상관없음.
                        tuple("001",1),
                        tuple("002",2)
                );
    }
}
