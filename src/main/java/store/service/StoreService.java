package store.service;

import store.controller.UserInteractionCallback;
import store.domain.*;
import store.util.Message.ErrorMessage;
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
        parsedAnswer.forEach((productName, quantity) -> handleProductPurchase(productName, quantity, receipt));
        return receipt;
    }

    private void handleProductPurchase(String productName, int quantity, Receipt receipt) {
        List<Product> productsList = products.findByName(productName);
        if (productsList.isEmpty()) throw new IllegalArgumentException(ErrorMessage.NON_EXISTENT_PRODUCT.getMessage());
        validateTotalQuantity(quantity, productsList);
        processProductsList(productsList, quantity, receipt);
    }

    private void processProductsList(List<Product> productsList, int quantity, Receipt receipt) {
        int remainingQuantity = quantity;
        for (Product product : productsList) {
            if (remainingQuantity <= 0) break;
            remainingQuantity = processProductPurchase(product, remainingQuantity, receipt);
        }
    }

    private void validateTotalQuantity(int quantity, List<Product> productsList) {
        int totalStock = calculateTotalStock(productsList);
        if (totalStock < quantity) {
            throw new IllegalArgumentException(ErrorMessage.EXCEEDS_STOCK.getMessage());
        }
    }

    private int calculateTotalStock(List<Product> productsList) {
        return productsList.stream().mapToInt(Product::getAvailableStock).sum();
    }

    private int processProductPurchase(Product product, int quantity, Receipt receipt) {
        return product.hasPromotion()
                ? applyPromotion(product, quantity, promotions.promotionFindByName(product.getPromotion()), receipt)
                : handleRegularProduct(product, quantity, receipt);
    }

    private int applyPromotion(Product product, int quantity, Promotion promotion, Receipt receipt) {
        if (!promotion.isValidOnDate()) return handleRegularProduct(product, quantity, receipt);
        return processPromotion(product, quantity, promotion, receipt);
    }

    private int processPromotion(Product product, int quantity, Promotion promotion, Receipt receipt) {
        int promoQuantity = product.getMinStockAndQuantity(quantity);
        int freeQuantity = calculateFreeQuantity(product, promotion, promoQuantity);
        decrementProductStock(product, promoQuantity + freeQuantity);
        addProductToReceipt(receipt, product, promoQuantity, freeQuantity);
        return quantity - promoQuantity;
    }

    private int calculateFreeQuantity(Product product, Promotion promotion, int promoQuantity) {
        int freeQuantity = promotion.currentFreeQuantity(promoQuantity);
        if (promotion.expectedFreeQuantityTrue(promoQuantity) && userAcceptsAdditionalItem(product)) {
            freeQuantity += 1;
        } else {
            handleRemainingNonPromotionalQuantity(promoQuantity, product, promotion);
        }
        return freeQuantity;
    }

    private boolean userAcceptsAdditionalItem(Product product) {
        String message = product.getMorePromotionProductMessage(1);
        return userInteractionCallback.askUser(message);
    }

    private void handleRemainingNonPromotionalQuantity(int promoQuantity, Product product, Promotion promotion) {
        int remainingNonPromotionalQuantity = promotion.hasNonPromotionalProduct(promoQuantity);
        if (remainingNonPromotionalQuantity > 0 && !userAcceptsNonPromotionalQuantity(product, remainingNonPromotionalQuantity)) {
            throw new RestartException();
        }
    }

    private boolean userAcceptsNonPromotionalQuantity(Product product, int remainingNonPromotionalQuantity) {
        String message = product.getNonPromotionMessage(remainingNonPromotionalQuantity);
        return userInteractionCallback.askUser(message);
    }

    private void decrementProductStock(Product product, int totalQuantity) {
        product.decrementStock(totalQuantity);
    }

    private void addProductToReceipt(Receipt receipt, Product product, int promoQuantity, int freeQuantity) {
        ReceiptItem receiptItem = product.addReceiptItem(promoQuantity);
        receiptItem.addAdditionalQuantity(freeQuantity);
        receipt.addPurchaseItem(receiptItem);
    }

    private int handleRegularProduct(Product product, int quantity, Receipt receipt) {
        int regularQuantity = product.getMinStockAndQuantity(quantity);
        decrementProductStock(product, regularQuantity);
        addProductToReceipt(receipt, product, regularQuantity, 0);
        return quantity - regularQuantity;
    }

    public void calculateMembership(Receipt receipt) {
        receipt.membershipPayPrice();
    }

    public void calculateRegular(Receipt receipt) {
        receipt.regularPayPrice();
    }

    public void updateProductStock(Receipt receipt) {
        receipt.getPurchaseItems().forEach(item -> products.updateProductStock(item.getName(), item.getQuantity()));
    }

    public void saveProductsToFile() {
        products.saveProductsToFile();
    }
}
