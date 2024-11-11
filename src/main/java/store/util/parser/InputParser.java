package store.util.parser;

import store.util.Message.ErrorMessage;

import java.util.HashMap;
import java.util.Map;

public class InputParser {

    private static final String ITEM_START = "[";
    private static final String ITEM_END = "]";
    private static final String ITEM_DELIMITER = ",";
    private static final String QUANTITY_DELIMITER = "-";
    private static final int EXPECTED_PARTS_LENGTH = 2;

    public static Map<String, Integer> purchaseParse(String input) {
        Map<String, Integer> purchasedProducts = new HashMap<>();
        String[] items = parseItems(input);
        for (String item : items) {
            addItemToMap(purchasedProducts, item);
        }
        return purchasedProducts;
    }

    private static String[] parseItems(String input) {
        return input.replace(ITEM_START, "").replace(ITEM_END, "").split(ITEM_DELIMITER);
    }

    private static void addItemToMap(Map<String, Integer> purchasedProducts, String item) {
        String[] parts = item.split(QUANTITY_DELIMITER);
        validatePartsLength(parts);
        String productName = parts[0];
        int quantity = Integer.parseInt(parts[1]);
        purchasedProducts.put(productName, quantity);
    }

    private static void validatePartsLength(String[] parts) {
        if (parts.length != EXPECTED_PARTS_LENGTH) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FORMAT.getMessage());
        }
    }
}
