package smaple.cafekiosk.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import smaple.cafekiosk.spring.api.controller.order.Ordercontroller;
import smaple.cafekiosk.spring.api.controller.product.ProductController;
import smaple.cafekiosk.spring.api.service.order.OrderService;
import smaple.cafekiosk.spring.api.service.product.ProductService;

@WebMvcTest(controllers = {Ordercontroller.class, ProductController.class})
public abstract class ControllerTestSupport {


    @Autowired
    protected MockMvc mockMvc;
    @MockBean
    protected OrderService orderService;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean //mockito (Bean을 Mocking)
    protected ProductService productService;

}
