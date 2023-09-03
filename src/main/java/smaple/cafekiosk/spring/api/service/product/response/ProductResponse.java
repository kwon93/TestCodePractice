package smaple.cafekiosk.spring.api.service.product.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import smaple.cafekiosk.spring.domain.product.Product;
import smaple.cafekiosk.spring.domain.product.ProductSellingStatus;
import smaple.cafekiosk.spring.domain.product.ProductType;

@Getter
@NoArgsConstructor
public class ProductResponse {

    private Long id;
    private String productNumber;
    private ProductType type;
    private ProductSellingStatus sellingStatus;
    private String name;
    private int price;

    @Builder //생성자를 프라이빗으로 막아 빌더로만 생성할 수 있게 함.
    private ProductResponse(Long id, String productNumber, ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
        this.id = id;
        this.productNumber = productNumber;
        this.type = type;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse of(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .productNumber(product.getProductNumber())
                .type(product.getType())
                .sellingStatus(product.getSellingStatus())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }
}
