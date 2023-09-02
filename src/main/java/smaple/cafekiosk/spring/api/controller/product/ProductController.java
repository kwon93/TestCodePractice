package smaple.cafekiosk.spring.api.controller.product;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import smaple.cafekiosk.spring.api.service.product.ProductService;
import smaple.cafekiosk.spring.api.service.product.response.ProductResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/api/v1/product/selling")
    public List<ProductResponse> getSellingProducts(){
        return productService.getSellingProducts();
    }
}