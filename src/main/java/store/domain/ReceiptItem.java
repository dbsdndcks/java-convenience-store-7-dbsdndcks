package store.domain;

public class ReceiptItem {
    private String name;
    private int price;
    private int quantity;
    private int additonalQuantity = 0;


    public ReceiptItem(String name, int quantity, int price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public void addAdditionalQuantity(int addAdditonalQuantity) {
        additonalQuantity += addAdditonalQuantity;
    }

    public int getTotalPrice() {
        return price * quantity;
    }

    // 구매 항목 문자열 생성 메서드
    public String toPurchaseString() {
        return String.format("%-10s %8d %,10d", name, quantity, getTotalPrice());
    }

    // 증정 항목 문자열 생성 메서드
    public String toFreeItemString() {
        if (additonalQuantity > 0) {
            return String.format("%-10s %8d", name, additonalQuantity);
        }
        return "";
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotalDiscountPrice() {
        return price * additonalQuantity;
    }

    public boolean isPromotional() {
        return additonalQuantity > 0;
    }

    public String getName() {
        return name;
    }
}

