package store.domain;

import java.util.ArrayList;
import java.util.List;

public class Receipt {
    private List<ReceiptItem> purchaseItems;    // 구매 상품 내역
    private int totalAmount;                    // 총 구매액
    private int promotionDiscount;              // 행사 할인
    private int membershipDiscount;             // 멤버십 할인
    private int finalAmount;                    // 최종 결제 금액

    public Receipt() {
        this.purchaseItems = new ArrayList<>();
        this.totalAmount = 0;
        this.promotionDiscount = 0;
        this.membershipDiscount = 0;
        this.finalAmount = 0;
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
        receiptString.append("상품명\t\t수량\t금액\n");

        // 구매 상품 목록 출력
        for (ReceiptItem item : purchaseItems) {
            receiptString.append(item.toPurchaseString()).append("\n");
        }

        // 무료 증정 상품 목록 출력
        receiptString.append("=============증\t정===============\n");
        for (ReceiptItem item : purchaseItems) {
            String freeItem = item.toFreeItemString();
            if (!freeItem.isEmpty()) {
                receiptString.append(freeItem).append("\n");
            }
        }

        receiptString.append("====================================\n");
        receiptString.append(String.format("총구매액\t\t%d\t%,d원\n", getTotalItemsCount(), totalAmount));
        receiptString.append(String.format("행사할인\t\t\t-%,d원\n", promotionDiscount));
        receiptString.append(String.format("멤버십할인\t\t\t-%,d원\n", membershipDiscount));
        receiptString.append(String.format("내실돈\t\t\t%,d원\n", finalAmount));

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
}