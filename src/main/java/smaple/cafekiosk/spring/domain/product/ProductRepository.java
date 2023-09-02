package smaple.cafekiosk.spring.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * select *
     * from product
     * where selling_type in ('SELLING','HOLD');
     * @param sellingTypes
     * @return selling 이거나 hold 인 제품들만 return
     */
    List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingTypes);

}
