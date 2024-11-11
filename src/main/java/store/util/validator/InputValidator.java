package store.util.validator;

import store.util.Message.ErrorMessage;

import java.util.regex.Pattern;

public class InputValidator {
    private static final Pattern INPUT_PATTERN = Pattern.compile("^\\[(.+)-(\\d+)](,\\[(.+)-(\\d+)])*$");

    public static void validateInputQuestion(String input) {
        validateInputIsEmpty(input);
        validateMembershipInput(input);
    }

    private static void validateInputIsEmpty(String input) {
        if (input.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT.getMessage());
        }
    }

    private static void validateMembershipInput(String input) {
        if (!input.equals("Y") && !input.equals("N")) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT.getMessage());
        }
    }

    public static void validateInputProduct(String input) {
        if (!INPUT_PATTERN.matcher(input).matches()) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FORMAT.getMessage());
        }
    }
}
