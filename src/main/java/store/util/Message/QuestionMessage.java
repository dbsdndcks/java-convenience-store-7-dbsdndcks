package store.util.Message;

public enum QuestionMessage {
    MORE_PROMOTION("현재 %s은(는) %d개를 무료로 받을 수 있습니다. 추가하시겠습니까? (Y/N)"),
    NON_PROMOTION("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)"),
    CSV_FORMAT_PROMOTION("%s,%d,%d,%s"),
    READ_ITEM("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])"),
    MEMBERSHIP_PROMPT("멤버십 할인을 받으시겠습니까? (Y/N)"),
    OTHER_PURCHASE("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");

    private final String message;

    QuestionMessage(String messageFormat) {
        this.message = messageFormat;
    }

    public String format(Object... args) {
        return String.format(message, args);
    }

    public String getMessage() {
        return message;
    }
}