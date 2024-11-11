package store.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    @Test
    void 상품정보_형식_출력_확인() {
        Product product = new Product("사이다", 1000, 10, "탄산2+1");
        String productInfo = product.formatProductInfo();
        assertThat(productInfo).isEqualTo("사이다 1,000원 10개 탄산2+1");
    }

    @Test
    void 재고가_없는_상품_생성() {
        Product product = new Product("사이다", 1000, 0, null);
        assertThat(product.hasStock()).isFalse();
        assertThat(product.formatProductInfo()).contains("재고 없음");
    }

    @Test
    void 상품_프로모션_메시지_생성() {
        Product product = new Product("콜라", 1000, 5, "탄산2+1");
        String promotionMessage = product.getMorePromotionProductMessage(1);
        assertThat(promotionMessage).contains("현재 콜라은(는) 1개를 무료로 받을 수 있습니다. 추가하시겠습니까? (Y/N)");
    }

    @Test
    void 상품_이름_일치_확인() {
        Product product = new Product("사이다", 1000, 5, "탄산2+1");
        assertThat(product.productNameEqual("사이다")).isTrue();
    }
}
