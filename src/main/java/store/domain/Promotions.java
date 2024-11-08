package store.domain;

import java.util.List;
import java.util.Optional;

public class Promotions {
    private final List<Promotion> promotions;

    public Promotions(List<Promotion> promotionList) {
        this.promotions = promotionList;
    }

    public Promotion promotionFindByName(String productName) {
        for (Promotion promotion : promotions) {
            if (promotion.promotionNameEqual(productName)) {
                return  promotion;
            }
        }
        return null;
    }
}
