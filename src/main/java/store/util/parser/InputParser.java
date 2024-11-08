package store.util.parser;

import java.util.HashMap;
import java.util.Map;

public class InputParser {

    public static Map<String,Integer> purchaseParse(String input) {
        Map<String, Integer> purchasedProducts = new HashMap<>();
        String[] items = input.replace("[", "").replace("]", "").split(",");
        for (String item : items) {
            String[]parts = item.split(",");
            String productName = parts[0];
            int quantity = Integer.parseInt(parts[1]);
            purchasedProducts.put(productName, quantity);
        }
        return purchasedProducts;
    }

}
