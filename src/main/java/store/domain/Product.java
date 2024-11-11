package store.domain;

import java.text.DecimalFormat;
import java.util.List;

public class Product {
    private static final DecimalFormat priceFormat = new DecimalFormat("#,###");
    private static final String KOREA_WON = "원 ";
    private static final String STOCK_COUNT = "개 ";
    private static final String OUT_OF_STOCK = "재고 없음 ";

    private String name;
    private int price;
    private int quantity;
    private int remainingQuantity;
    private String promotion;


    public Product(String name, int price, int quantity, String promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.remainingQuantity = quantity;
        this.promotion = promotion;
    }

    // 일반 재고가 있는지 확인하는 메서드
    public boolean hasStock() {
        return quantity > 0;
    }

    // 재고가 없는 상품을 생성하는 메서드
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

    public void validateQuantity(int quantity) {
        if (remainingQuantity < quantity) {
            throw new IllegalArgumentException("Error 상품재고보다 많은 갯수 입력");
        }
    }

    public String getMorePromotionProductMessage(int freeQuantity) {
        return String.format("현재 %s은(는) %d개를 무료로 받을 수 있습니다. 추가하시겠습니까? (Y/N)", name, freeQuantity);
    }

    public String getNonPromotionMessage(int totalNonPromotionalQuantity) {
        return String.format("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)", name, totalNonPromotionalQuantity);
    }

    public String getName() {
        return name;
    }
    public String toCsvFormat() {
        return String.format("%s,%d,%d,%s", name, price, quantity, formatPromotionToString(promotion));
    }

    private Object formatPromotionToString(String promotion) {
        if (promotion != null) {
            return promotion;
        }
        return "null";
    }
}
