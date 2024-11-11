package store.util.parser;

import store.domain.Product;

public class ProductParser {

    public static Product parse(String line) {
        String[] data = line.split(",");
        String name = data[0];
        int price = Integer.parseInt(data[1]);
        int quantity = Integer.parseInt(data[2]);
        String promotion = data[3];
        if (data[3].equals("null")) {
            promotion = null;
        }
        return new Product(name, price, quantity, promotion);
    }
}
