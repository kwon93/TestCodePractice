package smaple.cafekiosk.spring.domain.stock;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 재고 관련 엔티티
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productNumber;

    private int quantity;

    @Builder
    public Stock(String productNumber, int quantity) {
        this.productNumber = productNumber;
        this.quantity = quantity;
    }

    public static Stock create(String productNumber, int quantity) {
        return Stock.builder()
                .productNumber(productNumber)
                .quantity(quantity)
                .build();
    }

    /**
     * 재고가 매개변숙값보다 적은지 확인하는 메서드
     * @param quantity
     * @return (@Code true, false)
     */
    public boolean isQuantityLessThan(Long quantity) {
        return this.quantity < quantity;
    }

    /**
     *
     * @param quantity
     */
    public void deductQuantity(Long quantity) {

    }
}
