package store.domain;

import java.util.ArrayList;
import java.util.List;

public class Receipt {
    private final List<ReceiptItem> purchaseItems;  // 구매 상품 내역
    private int totalAmount;                        // 총 구매액
    private int membershipDiscountPrice;
    private int totalCustomerPrice;

    public Receipt() {
        this.purchaseItems = new ArrayList<>();
        this.totalAmount = 0;
        this.membershipDiscountPrice = 0;
        this.totalCustomerPrice = 0;
    }

    // 구매 항목 추가 메서드 (무료 증정 수량 포함)
    public void addPurchaseItem(ReceiptItem item) {
        this.purchaseItems.add(item);
        this.totalAmount += item.getTotalPrice();
    }

    // 영수증 정보를 문자열로 반환하는 메서드
    public String generateReceiptString() {
        StringBuilder receiptString = new StringBuilder();
        receiptString.append("==============W 편의점================\n");
        receiptString.append(String.format("%-10s %6s %10s\n", "상품명", "수량", "금액"));

        // 구매 상품 목록 출력
        for (ReceiptItem item : purchaseItems) {
            receiptString.append(item.toPurchaseString()).append("\n");
        }

        // 무료 증정 상품 목록 출력
        receiptString.append("=============증    정===============\n");
        for (ReceiptItem item : purchaseItems) {
            String freeItem = item.toFreeItemString();
            if (!freeItem.isEmpty()) {
                receiptString.append(freeItem).append("\n");
            }
        }

        receiptString.append("====================================\n");
        receiptString.append(String.format("%-10s %6d %15s\n", "총구매액", getTotalItemsCount(), String.format("%,d", totalAmount)));
        receiptString.append(String.format("%-10s %22s\n", "행사할인", String.format("-%,d", getTotalDiscountPrice())));
        receiptString.append(String.format("%-10s %21s\n", "멤버십할인", String.format("-%,d", membershipDiscountPrice)));
        receiptString.append(String.format("%-10s %22s\n", "내실돈", String.format("%,d", totalCustomerPrice)));

        return receiptString.toString();
    }

    // 총 구매 항목 수량 계산
    private int getTotalItemsCount() {
        int totalCount = 0;
        for (ReceiptItem item : purchaseItems) {
            totalCount += item.getQuantity();
        }
        return totalCount;
    }

    private int getTotalDiscountPrice() {
        int totalDiscountPrice = 0;
        for (ReceiptItem item : purchaseItems) {
            totalDiscountPrice += item.getTotalDiscountPrice();
        }
        return totalDiscountPrice;
    }

    // 프로모션 미적용 금액 계산
    private int calculateNonPromotionalAmount() {
        int nonPromotionalAmount = 0;
        for (ReceiptItem item : purchaseItems) {
            if (!item.isPromotional()) {  // 프로모션 미적용 항목만 합산
                nonPromotionalAmount += item.getTotalPrice();
            }
        }
        return nonPromotionalAmount;
    }

    // 멤버십 할인 계산 메서드
    private int calculateMembershipDiscount() {
        int nonPromotionalAmount = calculateNonPromotionalAmount();
        membershipDiscountPrice = (int) (nonPromotionalAmount * 0.30);
        if (membershipDiscountPrice > 8000) {
            membershipDiscountPrice = 8000;
        }
        return membershipDiscountPrice;
    }

    public void membershipPayPrice() {
        int totalDiscountPrice = getTotalDiscountPrice();
        membershipDiscountPrice = calculateMembershipDiscount();
        totalCustomerPrice = totalAmount - (totalDiscountPrice + membershipDiscountPrice);
    }

    public void RegularPayPrice() {
        int totalDiscountPrice = getTotalDiscountPrice();
        totalCustomerPrice = totalAmount - totalDiscountPrice;
    }
}
