package store.domain;

import store.util.Message.QuestionMessage;

import java.text.DecimalFormat;

public class Product {
    private static final DecimalFormat priceFormat = new DecimalFormat("#,###");
    private static final String KOREA_WON = "원 ";
    private static final String STOCK_COUNT = "개 ";
    private static final String OUT_OF_STOCK = "재고 없음 ";

    private String name;
    private int price;
    private int quantity;
    private String promotion;


    public Product(String name, int price, int quantity, String promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public boolean hasStock() {
        return quantity > 0;
    }

    public Product createOutOfStockProduct() {
        return new Product(name, price, 0, null);
    }

    public String getPromotion() {
        return promotion;
    }

    public String formatProductInfo() {
        StringBuilder productInfo = new StringBuilder();

        productInfo.append(name).append(" ")
                .append(priceFormat.format(price)).append(KOREA_WON)
                .append(formatQuantity(quantity))
                .append(formatPromotion(promotion));
        return productInfo.toString();
    }

    private String formatQuantity(int quantity) {
        String stock = quantity + STOCK_COUNT;
        if (quantity <= 0) {
            return OUT_OF_STOCK;
        }
        return stock;
    }

    private String formatPromotion(String promotionName) {
        if (promotionName == null) {
            return " ";
        }
        return promotionName;
    }
    public int getAvailableStock() {
        return this.quantity;
    }
    public boolean productNameEqual(String productName) {
        return this.name.equals(productName);
    }

    public boolean hasPromotion() {
        return promotion != null;
    }

    public int getMinStockAndQuantity(int stock) {
        return Math.min(quantity,stock);
    }

    public void decrementStock(int promoQuantity) {
        quantity -= promoQuantity;
    }

    public ReceiptItem addReceiptItem(int promoQuantity) {
        return new ReceiptItem(name,promoQuantity,price);
    }


    public String getMorePromotionProductMessage(int freeQuantity) {
        return QuestionMessage.MORE_PROMOTION.format(name, freeQuantity);
    }

    public String getNonPromotionMessage(int totalNonPromotionalQuantity) {
        return QuestionMessage.NON_PROMOTION.format(name, totalNonPromotionalQuantity);
    }

    public String getName() {
        return name;
    }
    public String toCsvFormat() {
        return QuestionMessage.CSV_FORMAT_PROMOTION.format(name, price, quantity, formatPromotionToString(promotion));
    }

    private Object formatPromotionToString(String promotion) {
        if (promotion != null) {
            return promotion;
        }
        return "null";
    }
}
