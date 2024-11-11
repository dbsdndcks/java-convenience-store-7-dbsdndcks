package store.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PromotionsTest {

    @Test
    void 프로모션_찾기_이름으로() {
        Promotion promotion = new Promotion("탄산2+1", 2, 1, LocalDate.now(), LocalDate.now().plusDays(10));
        Promotions promotions = new Promotions(List.of(promotion));

        Promotion foundPromotion = promotions.promotionFindByName("탄산2+1");
        assertThat(foundPromotion).isNotNull();
        assertThat(foundPromotion.promotionNameEqual("탄산2+1")).isTrue();
    }

    @Test
    void 프로모션_존재하지_않을때_확인() {
        Promotions promotions = new Promotions(List.of(new Promotion("탄산2+1", 2, 1, LocalDate.now(), LocalDate.now().plusDays(10))));
        Promotion result = promotions.promotionFindByName("탄산3+1");
        assertThat(result).isNull();
    }
}
