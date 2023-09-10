package smaple.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import smaple.cafekiosk.spring.domain.product.ProductRepository;

@Component
@RequiredArgsConstructor
public class ProductNumberFactory {

    private final ProductRepository productRepository;

    public String createNextProductNumber(){
        //productNumber 부여 001,002,003.....DB에서 마지막 저장된 프로덕트의 상품 번호를 읽어와서 +1
        String  latestProductNumber = productRepository.findLatestProductNumber();
        if (latestProductNumber == null){
            return "001";
        }

        int latestProductNumberInt = Integer.parseInt(latestProductNumber);
        int nextProductNumberInt = latestProductNumberInt + 1;

        // 9 -> 009, 10 -> 010 3칸 padding 되는 포맷
        return String.format("%03d", nextProductNumberInt);
    }
}

