package store.domain;

import store.util.ReciptMessage;

import java.util.ArrayList;
import java.util.List;

public class Receipt {
    private final List<ReceiptItem> purchaseItems;
    private int totalAmount;
    private int membershipDiscountPrice;
    private int totalCustomerPrice;

    public Receipt() {
        this.purchaseItems = new ArrayList<>();
        this.totalAmount = 0;
        this.membershipDiscountPrice = 0;
        this.totalCustomerPrice = 0;
    }

    public void addPurchaseItem(ReceiptItem item) {
        this.purchaseItems.add(item);
        this.totalAmount += item.getTotalPrice();
    }

    public String generateReceiptString() {
        StringBuilder receiptBuilder = new StringBuilder();
        receiptBuilder.append(ReciptMessage.RECEIPT_HEADER.getMessage())
                .append(ReciptMessage.COLUMN_TITLES.getMessage())
                .append(getPurchaseItemsString())
                .append(ReciptMessage.FREE_ITEMS_HEADER.getMessage())
                .append(getFreeItemsString())
                .append(ReciptMessage.RECEIPT_FOOTER.getMessage())
                .append(getTotalAmountString())
                .append(getDiscountString(ReciptMessage.EVENT_DISCOUNT_LABEL.getMessage(), getTotalDiscountPrice()))
                .append(getDiscountString(ReciptMessage.MEMBERSHIP_DISCOUNT_LABEL.getMessage(), membershipDiscountPrice))
                .append(getFinalAmountString());
        return receiptBuilder.toString();
    }

    private String getPurchaseItemsString() {
        StringBuilder items = new StringBuilder();
        for (ReceiptItem item : purchaseItems) {
            items.append(item.toPurchaseString()).append("\n");
        }
        return items.toString();
    }

    private String getFreeItemsString() {
        StringBuilder freeItems = new StringBuilder();
        for (ReceiptItem item : purchaseItems) {
            String freeItem = item.toFreeItemString();
            if (!freeItem.isEmpty()) {
                freeItems.append(freeItem).append("\n");
            }
        }
        return freeItems.toString();
    }

    private String getTotalAmountString() {
        return String.format(ReciptMessage.TOTAL_FORMAT.getMessage(),
                ReciptMessage.TOTAL_PRICE.getMessage(), getTotalItemsCount(),
                String.format(ReciptMessage.CURRENCY_FORMAT.getMessage(), totalAmount));
    }

    private String getDiscountString(String label, int discountAmount) {
        return String.format(ReciptMessage.DISCOUNT_FORMAT.getMessage(), label,
                String.format("-" + ReciptMessage.CURRENCY_FORMAT.getMessage(), discountAmount));
    }

    private String getFinalAmountString() {
        return String.format(ReciptMessage.DISCOUNT_FORMAT.getMessage(), ReciptMessage.TOTAL_PAY_LABEL.getMessage(),
                String.format(ReciptMessage.CURRENCY_FORMAT.getMessage(), totalCustomerPrice));
    }

    private int getTotalItemsCount() {
        return purchaseItems.stream().mapToInt(ReceiptItem::getQuantity).sum();
    }

    private int getTotalDiscountPrice() {
        return purchaseItems.stream().mapToInt(ReceiptItem::getTotalDiscountPrice).sum();
    }

    private int calculateNonPromotionalAmount() {
        return purchaseItems.stream()
                .filter(item -> !item.isPromotional())
                .mapToInt(ReceiptItem::getTotalPrice)
                .sum();
    }

    private int calculateMembershipDiscount() {
        int nonPromotionalAmount = calculateNonPromotionalAmount();
        double discountPercentage = Double.parseDouble(ReciptMessage.MEMBERSHIP_DISCOUNT_PERCENTAGE.getMessage());
        membershipDiscountPrice = (int) (nonPromotionalAmount * discountPercentage);
        int maxDiscount = Integer.parseInt(ReciptMessage.MAX_MEMBERSHIP_DISCOUNT.getMessage());
        return Math.min(membershipDiscountPrice, maxDiscount);
    }

    public void membershipPayPrice() {
        int totalDiscountPrice = getTotalDiscountPrice();
        membershipDiscountPrice = calculateMembershipDiscount();
        totalCustomerPrice = totalAmount - (totalDiscountPrice + membershipDiscountPrice);
    }

    public void regularPayPrice() {
        int totalDiscountPrice = getTotalDiscountPrice();
        totalCustomerPrice = totalAmount - totalDiscountPrice;
    }

    public List<ReceiptItem> getPurchaseItems() {
        return purchaseItems;
    }
}
