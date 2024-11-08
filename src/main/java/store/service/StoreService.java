package store.service;

import store.domain.Product;
import store.domain.Products;
import store.domain.Promotions;
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

    public void processPayment(String input) {
        //InputValidator.validatePurchaseInput(input);
        Map<String, Integer> parsedAnswer = InputParser.purchaseParse(input);
        for (Map.Entry<String, Integer> answer : parsedAnswer.entrySet()) {
            String requestName = answer.getKey();
            int requestQuantity = answer.getValue();

            List<Product> requestProduct = products.findByName(requestName);
            if (requestProduct.isEmpty()) {
                throw new IllegalArgumentException("존재하지 않는 상품");
            }
        }
    }
}
