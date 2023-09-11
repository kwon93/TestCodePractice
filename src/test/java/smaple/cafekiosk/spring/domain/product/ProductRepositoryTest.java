package smaple.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import smaple.cafekiosk.spring.IntegrationTestSupport;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static smaple.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static smaple.cafekiosk.spring.domain.product.ProductType.*;

//@ActiveProfiles("test") //Spring profile 상태 지정가능.
//@SpringBootTest
//@DataJpaTest //@SpringBootTest 보다 가벼움. JPA에 필요한 bean만 띄워줌, 자동으로 RollBack이 됨.
@Transactional
class ProductRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("원하는 판매 상태를 가진 상품들 조회하기에 성공한다.")
    void findAllBySellingStatusIn() throws Exception{
         //given
        Product product1 = createProduct("001",HANDMADE,SELLING,"Americano",4000);

        Product product2 = Product.builder()
                .productNumber("002")
                .type(HANDMADE)
                .sellingStatus(HOLD)
                .name("CafeLatte")
                .price(4500)
                .build();

        Product product3 = Product.builder()
                .productNumber("003")
                .type(HANDMADE)
                .sellingStatus(STOP_SELLING)
                .name("팥빙수")
                .price(7000)
                .build();

        productRepository.saveAll(List.of(product1, product2, product3));

         //when
        List<Product> products =
                productRepository.findAllBySellingStatusIn
                        (List.of(SELLING, HOLD));
        //then
        assertThat(products).hasSize(2)
                .extracting("productNumber","name","sellingStatus") //내가 검증하고자하는 필드만 추출 가능.
                .containsExactlyInAnyOrder( //리스트 검증시 사용되는 검증메서드 순서 상관없음.
                        tuple("001","Americano", SELLING),
                        tuple("002","CafeLatte", HOLD));
    }

    @Test
     @DisplayName("findAllByProductNumberIn() : 삼품 번호 리스트로  상품 조회에 성공한다.")
    void findAllByProductNumberIn() throws Exception{
         //given
        Product product1 = createProduct("001",HANDMADE,SELLING,"Americano",4000);

        Product product2 = Product.builder()
                .productNumber("002")
                .type(HANDMADE)
                .sellingStatus(HOLD)
                .name("CafeLatte")
                .price(4500)
                .build();

        Product product3 = Product.builder()
                .productNumber("003")
                .type(HANDMADE)
                .sellingStatus(STOP_SELLING)
                .name("팥빙수")
                .price(7000)
                .build();

        productRepository.saveAll(List.of(product1, product2, product3));

         //when

        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001","002"));

        //then
        assertThat(products).hasSize(2)
                .extracting("productNumber","name","sellingStatus")
                .containsExactlyInAnyOrder( //리스트 검증시 사용되는 검증메서드 순서 상관없음.
                        tuple("001","Americano", SELLING),
                        tuple("002","CafeLatte", HOLD));

    }

    @Test
    @DisplayName("가장 마지막으로 저장한 상품의 상품 번호를 가져온다.")
    void findLatestProduct() throws Exception{
        //given
        String targetProductNumber = "003";

        Product product1 = createProduct("001", HANDMADE,SELLING,"Americano",4000);
        Product product2 = createProduct("002", HANDMADE,HOLD,"CafeLatte",4500);
        Product product3 = createProduct(targetProductNumber, HANDMADE,STOP_SELLING,"팥빙수",7000);

        productRepository.saveAll(List.of(product1, product2, product3));

        //when
        String latestProductNumber = productRepository.findLatestProductNumber();

        //then
        assertThat(latestProductNumber).isEqualTo(targetProductNumber);

    }

    @Test
    @DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어올 때, 상품이 하나도 없는 경우에는 null을 반환한다.")
    void findLatestProductWhenProductIsEmpty() throws Exception{
        //when
        String latestProductNumber = productRepository.findLatestProductNumber();

        //then
        assertThat(latestProductNumber).isNull();

    }


    private Product createProduct(String productNumber,
                                         ProductType type,
                                         ProductSellingStatus sellingStatus,
                                         String productName,
                                         int productPrice) {
        return Product.builder()
                .productNumber(productNumber)
                .type(type)
                .sellingStatus(sellingStatus)
                .name(productName)
                .price(productPrice)
                .build();
    }


}