package store.domain;

public class ReceiptItem {
    private static final String PURCHASE_FORMAT = "%-10s %8d %,10d";
    private static final String FREE_ITEM_FORMAT = "%-10s %8d";

    private String name;
    private int price;
    private int quantity;
    private int additionalQuantity = 0;

    public ReceiptItem(String name, int quantity, int price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public void addAdditionalQuantity(int addAdditionalQuantity) {
        additionalQuantity += addAdditionalQuantity;
    }

    public int getTotalPrice() {
        return price * quantity;
    }

    // Generates purchase item string
    public String toPurchaseString() {
        return String.format(PURCHASE_FORMAT, name, quantity, getTotalPrice());
    }

    // Generates free item string
    public String toFreeItemString() {
        if (additionalQuantity > 0) {
            return String.format(FREE_ITEM_FORMAT, name, additionalQuantity);
        }
        return "";
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotalDiscountPrice() {
        return price * additionalQuantity;
    }

    public boolean isPromotional() {
        return additionalQuantity > 0;
    }

    public String getName() {
        return name;
    }
}
