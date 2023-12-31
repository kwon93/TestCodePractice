package smaple.cafekiosk.spring.api.service.order.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCreateServiceRequest {

    private List<String> productNumber;

    @Builder
    public OrderCreateServiceRequest(List<String> productNumber) {
        this.productNumber = productNumber;
    }
}
