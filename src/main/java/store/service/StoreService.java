package store.service;

import store.controller.UserInteractionCallback;
import store.domain.Product;
import store.domain.Products;
import store.domain.Promotion;
import store.domain.Promotions;
import store.domain.Receipt;
import store.domain.ReceiptItem;
import store.util.exception.RestartException;
import store.util.parser.InputParser;

import java.util.List;
import java.util.Map;

public class StoreService {
    private final Products products;
    private final Promotions promotions;
    private final UserInteractionCallback userInteractionCallback;

    public StoreService(Products products, Promotions promotions, UserInteractionCallback userInteractionCallback) {
        this.products = products;
        this.promotions = promotions;
        this.userInteractionCallback = userInteractionCallback;
    }

    public String generateOpeningMessage() {
        return products.generateProductListView();
    }

    public Receipt processPayment(String input) {
        Receipt receipt = new Receipt();
        Map<String, Integer> parsedAnswer = InputParser.purchaseParse(input);

        for (Map.Entry<String, Integer> answer : parsedAnswer.entrySet()) {
            handleProductPurchase(answer.getKey(), answer.getValue(), receipt);
        }
        return receipt;
    }

    private void handleProductPurchase(String productName, int quantity, Receipt receipt) {
        List<Product> productsList = products.findByName(productName);
        if (productsList.isEmpty()) throw new IllegalArgumentException("존재하지 않는 상품: " + productName);
        validateTotalQuantity(quantity, productsList);
        int remainingQuantity = quantity;
        for (Product product : productsList) {
            if (remainingQuantity <= 0) break;
            remainingQuantity = processProductPurchase(product, remainingQuantity, receipt);
        }
    }

    public void validateTotalQuantity(int quantity, List<Product> productsList) {
        int totalStock = productsList.stream()
                .mapToInt(Product::getAvailableStock)
                .sum();
        if (totalStock < quantity) {
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }
    }

    private int processProductPurchase(Product product, int quantity, Receipt receipt) {
        if (product.hasPromotion()) {
            return applyPromotion(product, quantity, promotions.promotionFindByName(product.getPromotion()), receipt);
        }
        return handleRegularProduct(product, quantity, receipt);
    }

    private int applyPromotion(Product product, int quantity, Promotion promotion, Receipt receipt) {
        if (!promotion.isValidOnDate()) {
            return handleRegularProduct(product, quantity, receipt); // 프로모션이 유효하지 않으면 일반 구매로 처리
        }
        int promoQuantity = product.getMinStockAndQuantity(quantity);
        int freeQuantity = promotion.currentFreeQuantity(promoQuantity);
        int remainingNonPromotionalQuantity = promotion.hasNonPromotionalProduct(promoQuantity);
        boolean addPromotion = promotion.expectedFreeQuantityTrue(promoQuantity);

        if (addPromotion) {
            String message = product.getMorePromotionProductMessage(1);
            boolean userAgrees = userInteractionCallback.askUser(message);
            if (userAgrees) {
                freeQuantity += 1;
            }
        }
        if(!addPromotion && remainingNonPromotionalQuantity > 0) {
            int totalNonPromotionalQuantity = (quantity - promoQuantity) + remainingNonPromotionalQuantity;
            String message = product.getNonPromotionMessage(totalNonPromotionalQuantity);
            boolean userAgrees = userInteractionCallback.askUser(message);
            if (!userAgrees) {
                throw new RestartException();
            }
        }
        product.decrementStock(promoQuantity + freeQuantity);

        // 프로모션 항목을 하나의 ReceiptItem으로 추가
        ReceiptItem receiptItem = product.addReceiptItem(promoQuantity);
        receiptItem.addAdditionalQuantity(freeQuantity);
        receipt.addPurchaseItem(receiptItem);

        // 남은 수량 반환
        return quantity - promoQuantity;
    }

    private int handleRegularProduct(Product product, int quantity, Receipt receipt) {
        int regularQuantity = product.getMinStockAndQuantity(quantity);
        product.decrementStock(regularQuantity);

        // 일반 구매 항목을 하나의 ReceiptItem으로 추가
        ReceiptItem purchaseItem = product.addReceiptItem(regularQuantity);
        receipt.addPurchaseItem(purchaseItem);

        // 남은 수량 반환
        return quantity - regularQuantity;
    }

    public void calculateMembership(Receipt receipt) {
        receipt.membershipPayPrice();
    }

    public void calculateRegular(Receipt receipt) {
        receipt.RegularPayPrice();
    }
}
