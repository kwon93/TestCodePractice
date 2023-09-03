package smaple.cafekiosk.spring.api.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import smaple.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import smaple.cafekiosk.spring.api.service.order.response.OrderResponse;
import smaple.cafekiosk.spring.domain.order.Order;
import smaple.cafekiosk.spring.domain.order.OrderRepository;
import smaple.cafekiosk.spring.domain.product.Product;
import smaple.cafekiosk.spring.domain.product.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository ordeRepository;
    public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registeredTime) {
        List<String> productNumbers = request.getProductNumber();
        //product
        List<Product> duplicateProduct = findProductsBy(productNumbers);

        Order order = Order.create(duplicateProduct, registeredTime);
        Order savedOrder = ordeRepository.save(order);


        //order
        return OrderResponse.of(savedOrder);
    }

    private List<Product> findProductsBy(List<String> productNumbers) {
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);
        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductNumber, product -> product));

        List<Product> duplicateProduct = productNumbers.stream()
                .map(productNumber -> productMap.get(productNumber))
                .toList();
        return duplicateProduct;
    }


}
