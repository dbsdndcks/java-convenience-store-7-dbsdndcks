package store.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class PromotionTest {

    @Test
    void 프로모션_날짜_유효성_확인() {
        Promotion promotion = new Promotion("탄산2+1", 2, 1, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        assertThat(promotion.isValidOnDate()).isTrue();
    }

    @Test
    void 프로모션_날짜_유효하지_않은경우() {
        Promotion promotion = new Promotion("탄산2+1", 2, 1, LocalDate.now().minusDays(5), LocalDate.now().minusDays(1));
        assertThat(promotion.isValidOnDate()).isFalse();
    }

    @Test
    void 프로모션_적용수량_계산() {
        Promotion promotion = new Promotion("탄산2+1", 2, 1, LocalDate.now(), LocalDate.now().plusDays(10));
        int freeQuantity = promotion.currentFreeQuantity(6); // 6개의 구매 시 2세트 적용
        assertThat(freeQuantity).isEqualTo(2);
    }

    @Test
    void 프로모션_미적용수량_계산() {
        Promotion promotion = new Promotion("탄산2+1", 2, 1, LocalDate.now(), LocalDate.now().plusDays(10));
        int remainingQuantity = promotion.hasNonPromotionalProduct(7); // 7개 구매, 2세트 적용 후 1개 남음
        assertThat(remainingQuantity).isEqualTo(1);
    }
}
