package store.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.domain.*;
import store.service.StoreService;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StoreIntegrationTest {

    private StoreService storeService;
    private Products products;
    private Promotions promotions;

    @BeforeEach
    void setUp() {
        Product product1 = new Product("사이다", 1000, 10, "탄산2+1");
        Product product2 = new Product("콜라", 1000, 10, "탄산1+1");
        Product product3 = new Product("물", 500, 5, null);

        Promotion promotion1 = new Promotion("탄산2+1", 2, 1, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        Promotion promotion2 = new Promotion("탄산1+1", 1, 1, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));

        products = new Products(List.of(product1, product2, product3));
        promotions = new Promotions(List.of(promotion1, promotion2));

        storeService = new StoreService(products, promotions, message -> true); // 기본적으로 모든 사용자 응답을 'Y'로 처리
    }

    @Test
    void 상품목록_출력_및_프로모션_적용_테스트() {
        String productListView = products.generateProductListView();

        // 상품 목록에 필요한 내용이 있는지 검증
        assertThat(productListView).contains("사이다 1,000원 10개 탄산2+1");
        assertThat(productListView).contains("콜라 1,000원 10개 탄산1+1");
        assertThat(productListView).contains("물 500원 5개");
    }

    @Test
    void 결제_및_프로모션적용_확인() {
        Receipt receipt = storeService.processPayment("[사이다-3]");

        // 프로모션에 의해 추가 증정 수량 확인 (2+1 프로모션)
        String receiptContent = receipt.generateReceiptString();
        assertThat(receiptContent).contains("사이다");
        assertThat(receiptContent).contains("3,000");
        assertThat(receiptContent).contains("1");
    }

    @Test
    void 재고부족시_예외발생_테스트() {
        assertThatThrownBy(() -> storeService.processPayment("[사이다-15]"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("재고 수량을 초과하여 구매할 수 없습니다");
    }


    @Test
    void 비프로모션상품_멤버십_할인적용_확인() {
        Receipt receipt = storeService.processPayment("[물-3]");
        storeService.calculateMembership(receipt);

        String receiptContent = receipt.generateReceiptString();
        assertThat(receiptContent).contains("멤버십할인");
        assertThat(receiptContent).contains("내실돈");
    }
}
