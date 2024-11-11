package store.util;

public enum ReciptMessage {
    RECEIPT_HEADER("==============W 편의점================\n"),
    COLUMN_TITLES(String.format("%-10s %6s %10s\n", "상품명", "수량", "금액")),
    FREE_ITEMS_HEADER("=============증    정===============\n"),
    RECEIPT_FOOTER("====================================\n"),
    TOTAL_FORMAT("%-10s %6d %15s\n"),
    DISCOUNT_FORMAT("%-10s %22s\n"),
    TOTAL_PAY_LABEL("내실돈"),
    EVENT_DISCOUNT_LABEL("행사할인"),
    MEMBERSHIP_DISCOUNT_LABEL("멤버십할인"),
    TOTAL_PRICE("총구매액"),
    CURRENCY_FORMAT("%,d"),
    MAX_MEMBERSHIP_DISCOUNT("8000"),  // Change as needed for different values
    MEMBERSHIP_DISCOUNT_PERCENTAGE("0.30");

    private final String message;

    ReciptMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
