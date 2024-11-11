package store.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductsTest {

    private Products products;

    @BeforeEach
    void setUp() {
        Product product1 = new Product("사이다", 1000, 10, "탄산2+1");
        Product product2 = new Product("콜라", 1000, 0, null);
        products = new Products(List.of(product1, product2));
    }

    @Test
    void 상품목록_출력_확인() {
        String productListView = products.generateProductListView();
        assertThat(productListView).contains("사이다 1,000원 10개 탄산2+1");
        assertThat(productListView).contains("콜라 1,000원 재고 없음");
    }

    @Test
    void 상품찾기_이름으로() {
        List<Product> result = products.findByName("사이다");
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("사이다");
    }

    @Test
    void 상품재고_업데이트_확인() {
        products.updateProductStock("사이다", 2);
        List<Product> result = products.findByName("사이다");
        assertThat(result.get(0).getAvailableStock()).isEqualTo(8);
    }
}
