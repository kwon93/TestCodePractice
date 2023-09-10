package smaple.cafekiosk.spring.api.service.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import smaple.cafekiosk.spring.IntegrationTestSupport;
import smaple.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import smaple.cafekiosk.spring.api.service.product.request.ProductServieceCreateRequest;
import smaple.cafekiosk.spring.api.service.product.response.ProductResponse;
import smaple.cafekiosk.spring.domain.product.Product;
import smaple.cafekiosk.spring.domain.product.ProductRepository;
import smaple.cafekiosk.spring.domain.product.ProductSellingStatus;
import smaple.cafekiosk.spring.domain.product.ProductType;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static smaple.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static smaple.cafekiosk.spring.domain.product.ProductType.HANDMADE;

class ProductServiceTest extends IntegrationTestSupport {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @BeforeAll
    static void beforeAll() {
        //beforeClass
    }

    @BeforeEach //테스트간의 결합도를 올리는 경우를 가져오기에 지양하는것이 좋다. (공유 자원) , given절 미비로 또한 가독성이 떨어진다.
    void setUp(){
        //beforeEach의 사용 경우
        // 각 테스트 입장에서 봤을 때 : 아에 몰라도 테스트 내용을 이해하는데 문제가 없는가
        // 수정해도 모든 테스트에 영향을 주지 않는가
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("신규 상품을 등록한다. 상품 번호는 가장 최근 상품의 상품번호에서 1 증가한 값을 가진다.")
    void createProduct() throws Exception {
        //given
        Product product = createProduct("001", HANDMADE,SELLING,"Americano",4000);
        productRepository.save(product);

        ProductServieceCreateRequest request = ProductServieceCreateRequest.builder()
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("Cappuccino")
                .price(5000)
                .build();

        //when
        ProductResponse productResponse = productService.createProduct(request);

        //then
        assertThat(productResponse)
                .extracting("productNumber","type","sellingStatus","name","price")
                .contains("002",HANDMADE,SELLING,"Cappuccino",5000);
    }

    @Test
    @DisplayName("상품이 하나도 없는 경우에 상품을 등록하면 상품 번호는 001 이다.")
    void createProductWhenProductIsEmpty() throws Exception {
        //given
        ProductServieceCreateRequest request = ProductServieceCreateRequest.builder()
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("Cappuccino")
                .price(5000)
                .build();

        //when
        ProductResponse productResponse = productService.createProduct(request);

        //then
        assertThat(productResponse)
                .extracting("productNumber","type","sellingStatus","name","price")
                .contains("001",HANDMADE,SELLING,"Cappuccino",5000);

        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(1)
                .extracting("productNumber","type","sellingStatus","name","price")
                .contains(
                        tuple("001",HANDMADE,SELLING,"Cappuccino",5000));
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