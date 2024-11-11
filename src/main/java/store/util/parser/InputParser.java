package store.util.parser;

import java.util.HashMap;
import java.util.Map;

public class InputParser {

    public static Map<String,Integer> purchaseParse(String input) {
        Map<String, Integer> purchasedProducts = new HashMap<>();
        String[] items = input.replace("[", "").replace("]", "").split(",");
        for (String item : items) {
            String[]parts = item.split("-");
            if (parts.length != 2) {
                throw new IllegalArgumentException("[ERROR] 입력 형식이 올바르지 않습니다. 예: [사이다-2],[감자칩-1]");
            }
            String productName = parts[0];
            int quantity = Integer.parseInt(parts[1]);
            purchasedProducts.put(productName, quantity);
        }
        return purchasedProducts;
    }

}
