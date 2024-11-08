package store.service;

import store.domain.Product;
import store.domain.Products;
import store.domain.Promotion;
import store.domain.Promotions;
import store.util.parser.InputParser;

import java.time.LocalDate;
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

    public void processPayment(String input) {
        Map<String, Integer> parsedAnswer = InputParser.purchaseParse(input);
        LocalDate today = LocalDate.now();

        for (Map.Entry<String, Integer> answer : parsedAnswer.entrySet()) {
            handleProductPurchase(answer.getKey(), answer.getValue(), today);
        }
    }

    private void handleProductPurchase(String productName, int quantity, LocalDate date) {
        List<Product> productsList = products.findByName(productName);
        if (productsList.isEmpty()) throw new IllegalArgumentException("존재하지 않는 상품: " + productName);
        int remainingQuantity = quantity;
        int finalPrice = 0;
        for (Product product : productsList) {
            if (remainingQuantity <= 0) break;
            finalPrice += processProductPurchase(product, remainingQuantity);
            remainingQuantity = updateRemainingQuantity(product, remainingQuantity);
        }
    }

   

}
