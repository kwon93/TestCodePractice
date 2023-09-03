package smaple.cafekiosk.spring.api.controller.order;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import smaple.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import smaple.cafekiosk.spring.api.service.order.OrderService;
import smaple.cafekiosk.spring.api.service.order.response.OrderResponse;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class Ordercontroller {

    private final OrderService orderService;

    @PostMapping("/api/v1/orders/new")
    public OrderResponse createOder(@RequestBody OrderCreateRequest request){
        LocalDateTime registeredTime = LocalDateTime.now();
        return orderService.createOrder(request, registeredTime);
    }


}
