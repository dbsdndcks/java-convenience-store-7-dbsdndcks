package store.input;

import org.junit.jupiter.api.Test;
import store.util.validator.InputValidator;
import store.util.Message.ErrorMessage;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InputValidatorTest {

    @Test
    void 상품명과수량_올바른형식입력시_정상처리() {
        String input = "[사이다-2],[감자칩-1]";
        InputValidator.validateInputProduct(input);
    }

    @Test
    void 상품명또는수량_형식올바르지않을경우_예외발생() {
        String invalidInput = "[사이다-2],[감자칩]";
        assertThatThrownBy(() -> InputValidator.validateInputProduct(invalidInput))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.INVALID_FORMAT.getMessage());
    }


    @Test
    void 프로모션_Y_입력시_정상처리() {
        String input = "Y";
        InputValidator.validateInputQuestion(input);
    }

    @Test
    void 프로모션_공백_입력시_예외처리() {
        String input = " ";
        assertThatThrownBy(() -> InputValidator.validateInputQuestion(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.INVALID_INPUT.getMessage());
    }

    @Test
    void 프로모션_YN_외_유효하지않은값입력시_예외발생() {
        String input = "X";
        assertThatThrownBy(() -> InputValidator.validateInputQuestion(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.INVALID_INPUT.getMessage());
    }
}
