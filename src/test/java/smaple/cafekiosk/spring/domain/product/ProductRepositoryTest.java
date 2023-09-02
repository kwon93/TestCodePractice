package smaple.cafekiosk.spring.domain.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test") //Spring profile 상태 지정가능.
//@SpringBootTest
@DataJpaTest //@SpringBootTest 보다 가벼움. JPA에 필요한 bean만 띄워줌
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("원하는 판매 상태를 가진 상품들 조회하기에 성공한다.")
    void findAllBySellingStatusIn() throws Exception{
         //given
        Product product1 = Product.builder()
                .productNumber("001")
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("Americano")
                .price(4000)
                .build();

        Product product2 = Product.builder()
                .productNumber("002")
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.HOLD)
                .name("CafeLatte")
                .price(4500)
                .build();

        Product product3 = Product.builder()
                .productNumber("003")
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.STOP_SELLING)
                .name("팥빙수")
                .price(7000)
                .build();

        productRepository.saveAll(List.of(product1, product2, product3));

         //when
        List<Product> products =
                productRepository.findAllBySellingStatusIn
                        (List.of(ProductSellingStatus.SELLING, ProductSellingStatus.HOLD));
        //then
        assertThat(products).hasSize(2)
                .extracting("productNumber","name","sellingStatus") //내가 검증하고자하는 필드만 추출 가능.
                .containsExactlyInAnyOrder( //리스트 검증시 사용되는 검증메서드 순서 상관없음.
                        tuple("001","Americano",ProductSellingStatus.SELLING),
                        tuple("002","CafeLatte",ProductSellingStatus.HOLD));
    }




}