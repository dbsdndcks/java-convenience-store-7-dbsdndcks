package store.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReceiptTest {

    private Receipt receipt;

    @BeforeEach
    void setUp() {
        receipt = new Receipt();
    }

    @Test
    void 영수증_구매항목_추가_및_총액_확인() {
        ReceiptItem item1 = new ReceiptItem("사이다", 2, 1000);
        receipt.addPurchaseItem(item1);
        assertThat(receipt.getPurchaseItems()).hasSize(1);
        assertThat(receipt.generateReceiptString()).contains("사이다");
        assertThat(receipt.generateReceiptString()).contains("총구매액");
    }

    @Test
    void 멤버십할인_적용_후_할인가격_계산() {
        ReceiptItem item = new ReceiptItem("콜라", 4, 1000);
        receipt.addPurchaseItem(item);
        receipt.membershipPayPrice();
        assertThat(receipt.generateReceiptString()).contains("멤버십할인");
    }

    @Test
    void 정규가격_계산() {
        ReceiptItem item = new ReceiptItem("사이다", 3, 1500);
        receipt.addPurchaseItem(item);
        receipt.regularPayPrice();
        assertThat(receipt.generateReceiptString()).contains("내실돈");
    }
}
