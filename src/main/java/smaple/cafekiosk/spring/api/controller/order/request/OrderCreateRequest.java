package smaple.cafekiosk.spring.api.controller.order.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import smaple.cafekiosk.spring.api.service.order.request.OrderCreateServiceRequest;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
@Getter
@NoArgsConstructor
public class OrderCreateRequest {

    @NotEmpty(message = "상품의 번호를 입력해주세요.")
    private List<String> productNumber;

    @Builder
    public OrderCreateRequest(List<String> productNumber) {
        this.productNumber = productNumber;
    }

    public OrderCreateServiceRequest toServiceRequest() {
        return OrderCreateServiceRequest.builder()
                .productNumber(productNumber)
                .build();
    }
}
