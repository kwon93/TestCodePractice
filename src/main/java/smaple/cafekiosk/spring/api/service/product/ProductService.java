package smaple.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smaple.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import smaple.cafekiosk.spring.api.service.product.request.ProductServieceCreateRequest;
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
    private final ProductNumberFactory productNumberFactory;

    @Transactional
    public ProductResponse createProduct(ProductServieceCreateRequest request) {
        //next Product
        String nextProductNumber = productNumberFactory.createNextProductNumber();
        Product product = request.toEntity(nextProductNumber);
        Product savedProduct = productRepository.save(product);

        return ProductResponse.of(savedProduct);
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
