package smaple.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smaple.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import smaple.cafekiosk.spring.api.service.product.response.ProductResponse;
import smaple.cafekiosk.spring.domain.product.Product;
import smaple.cafekiosk.spring.domain.product.ProductRepository;
import smaple.cafekiosk.spring.domain.product.ProductSellingStatus;
import smaple.cafekiosk.spring.domain.product.ProductType;

import java.util.List;
import java.util.stream.Collectors;

/**
 * readOnly = true : 읽기 전용
 * CRUD 에서 CUD 작업이 동작 안함.
 * JPA : CUD 스냅샷 저장, 변경 감지를 안하게되어 성능 향상 효과가 있다.
 *
 * CQRS : Command / Query
 * 입력(생성,수정,삭제) 과 읽기 작업을 분리하자.
 *
 */
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProductService {

    //동시성 이슈 존재 : 동시에 여러명이 제품을 동시에 등록할 때....

    private final ProductRepository productRepository;

    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        //next Product
        String nextProductNumber = createNextProductNumber();

        Product product = request.toEntity(nextProductNumber);
        Product savedProduct = productRepository.save(product);

        return ProductResponse.of(savedProduct);
    }

    private String createNextProductNumber(){
        //productNumber 부여 001,002,003.....DB에서 마지막 저장된 프로덕트의 상품 번호를 읽어와서 +1
        String  latestProductNumber = productRepository.findLatestProductNumber();
        if (latestProductNumber == null){
            return "001";
        }

        int latestProductNumberInt = Integer.parseInt(latestProductNumber);
        int nextProductNumberInt = latestProductNumberInt + 1;

        // 9 -> 009, 10 -> 010 3칸 padding 되는 포맷
        return String.format("%03d", nextProductNumberInt);
    }

    /**
     * 판매 여부 조회
     */
    public List<ProductResponse> getSellingProducts(){
        List<Product> products =
                productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }


}
