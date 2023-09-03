package smaple.cafekiosk.spring.domain.orderProduct;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import smaple.cafekiosk.spring.domain.BaseEntity;
import smaple.cafekiosk.spring.domain.order.Order;
import smaple.cafekiosk.spring.domain.product.Product;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    public OrderProduct(Order order, Product product) {
        this.order = order;
        this.product = product;
    }


}
