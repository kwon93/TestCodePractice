package smaple.cafekiosk.spring.api.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smaple.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import smaple.cafekiosk.spring.api.service.order.request.OrderCreateServiceRequest;
import smaple.cafekiosk.spring.api.service.order.response.OrderResponse;
import smaple.cafekiosk.spring.domain.order.Order;
import smaple.cafekiosk.spring.domain.order.OrderRepository;
import smaple.cafekiosk.spring.domain.product.Product;
import smaple.cafekiosk.spring.domain.product.ProductRepository;
import smaple.cafekiosk.spring.domain.product.ProductType;
import smaple.cafekiosk.spring.domain.stock.Stock;
import smaple.cafekiosk.spring.domain.stock.StockRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;
    public OrderResponse createOrder(OrderCreateServiceRequest request, LocalDateTime registeredTime) {
        List<String> productNumbers = request.getProductNumber();
        //product
        List<Product> products = findProductsBy(productNumbers);
        deductStockQuantities(products);

        Order order = Order.create(products, registeredTime);
        Order savedOrder = orderRepository.save(order);

        //order
        return OrderResponse.of(savedOrder);
    }


    /**
     * 재고 감소 : 동시성 문제 (동시에 차감 요청이 왔을 때)
     * optimistic lock / pessimistic lock / .... 먼저 온 요청이 먼저 재고를 차감할 수 있도록...
     */
    @Transactional
    private void deductStockQuantities(List<Product> products) {
        //재고 차감 체크가 필요한 상품들 필터링
        List<String> stockProductNumbers = extractStockProductNumbers(products);
        //재고 Entity 조회
        Map<String, Stock> stockMap = createStockMapBy(stockProductNumbers);
        //상품별 Counting
        Map<String, Long> productCountingMap = createCountingMapBy(stockProductNumbers);

        //재고 차감 시도
        for (String stockProductNumber : new HashSet<>(stockProductNumbers)) {
            Stock stock = stockMap.get(stockProductNumber);
            int quantity = productCountingMap.get(stockProductNumber).intValue();

            if (stock.isQuantityLessThan(quantity)){
                throw new IllegalArgumentException("재고가 부족한 상품이 있습니다.");
            }

            stock.deductQuantity(quantity);
        }
    }


    private static List<String> extractStockProductNumbers(List<Product> products) {
        List<String> stockProductNumbers = products.stream()
                .filter(product -> ProductType.containsStockType(product.getType()))
                .map(Product::getProductNumber)
                .toList();
        return stockProductNumbers;
    }

    private Map<String, Stock> createStockMapBy(List<String> stockProductNumbers) {
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProductNumbers);

        return stocks.stream()
                .collect(Collectors.toMap(Stock::getProductNumber, s -> s));
    }

    private static Map<String, Long> createCountingMapBy(List<String> stockProductNumbers) {

        return stockProductNumbers.stream()
                .collect(Collectors.groupingBy(productNumber -> productNumber, Collectors.counting()));
    }

    private List<Product> findProductsBy(List<String> productNumbers) {
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);
        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductNumber, product -> product));

        return productNumbers.stream()
                .map(productNumber -> productMap.get(productNumber))
                .toList();

    }


}
