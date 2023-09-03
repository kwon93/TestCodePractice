package smaple.cafekiosk.spring.api.service.order.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import smaple.cafekiosk.spring.api.service.product.response.ProductResponse;
import smaple.cafekiosk.spring.domain.order.Order;
import smaple.cafekiosk.spring.domain.order.OrderStatus;
import smaple.cafekiosk.spring.domain.orderProduct.OrderProduct;

import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class OrderResponse {

    private Long id;
    private int totalPrice;
    private LocalDateTime registeredDateTime;
    private List<ProductResponse> products = new ArrayList<>();

    @Builder
    public OrderResponse(Long id, int totalPrice, LocalDateTime registeredDateTime, List<ProductResponse> products) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.registeredDateTime = registeredDateTime;
        this.products = products;
    }

    public static OrderResponse of(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .totalPrice(order.getTotalPrice())
                .registeredDateTime(order.getRegisteredDateTime())
                .products(order.getOrderProduct().stream()
                        .map(orderProduct -> ProductResponse.of(orderProduct.getProduct()))
                        .toList())
                .build();

    }
}
