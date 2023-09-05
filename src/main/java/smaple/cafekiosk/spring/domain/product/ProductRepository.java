package smaple.cafekiosk.spring.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    List<Product> findAllByProductNumberIn(List<String> productNumber);


    //역순정렬로 상위에 노출된 한개만 가져오는 Query
    @Query(value = "select p.product_number from product p order by id desc limit 1",nativeQuery = true)
    String findLatestProductNumber();
}
