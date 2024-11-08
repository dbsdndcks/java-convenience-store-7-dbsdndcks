package store.domain;

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
        if (quantity < 0) {
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

    public boolean productNameEqual(String productName) {
        return this.name.equals(productName);
    }

    public boolean hasPromotion() {
        return promotion != null;
    }

}
