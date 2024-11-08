package store.domain;

import java.util.List;

public class Promotions {
    private final List<Promotion> promotions;

    public Promotions(List<Promotion> promotionList) {
        this.promotions = promotionList;
    }

    public Promotion findByName(String productName) {
        for (Promotion promotion : promotions) {
            if (promotion.promotionNameEqual(productName)) {
                return  promotion;
            }
        }
        return null;
    }
}
