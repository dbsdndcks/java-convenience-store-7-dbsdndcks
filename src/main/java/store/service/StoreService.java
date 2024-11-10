package store.service;

import store.domain.*;
import store.util.parser.InputParser;

import java.util.List;
import java.util.Map;

public class StoreService {
    private final Products products;
    private final Promotions promotions;

    public StoreService(Products products, Promotions promotions) {
        this.products = products;
        this.promotions = promotions;
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

        int remainingQuantity = quantity;
        for (Product product : productsList) {
            if (remainingQuantity <= 0) break;
            remainingQuantity = processProductPurchase(product, remainingQuantity, receipt);
        }
        if (remainingQuantity > 0) {
            throw new IllegalArgumentException("Error: 상품 재고보다 많은 수량을 입력하셨습니다.");
        }
    }

    private int processProductPurchase(Product product, int quantity, Receipt receipt) {
        if (product.hasPromotion()) {
            return applyPromotion(product, quantity, promotions.promotionFindByName(product.getPromotion()), receipt);
        }
        return handleRegularProduct(product, quantity, receipt);
    }

    private int applyPromotion(Product product, int quantity, Promotion promotion, Receipt receipt) {
        int promoQuantity = product.getMinStockAndQuantity(quantity);
        int discountedQuantity = promotion.calculateDiscountedQuantity(promoQuantity);
        int freeQuantity = promotion.getFreeQuantity(promoQuantity);

        System.out.println("프로모션 최대 수량: " + promoQuantity);
        System.out.println("할인된 결제 수량: " + discountedQuantity);
        System.out.println("무료 증정 수량: " + freeQuantity);

        product.decrementStock(promoQuantity);


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
}
