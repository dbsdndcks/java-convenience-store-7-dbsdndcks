package store.domain;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Products {

    private final List<Product> products;
    private static final String PRODUCT_LIST_FILE = "products.md";
    private static final String PRODUCT_LIST_COVER_FILE = "src/main/resources/products.md";
    private static final String FILE_HEADER = "name,price,quantity,promotion\n";
    private static final String WELCOME_MESSAGE = "안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n\n";
    private static final String PRODUCT_PREFIX = "- ";

    public Products(List<Product> products) {
        this.products = new ArrayList<>(products);
        addOutOfStockProducts();
    }

    private void addOutOfStockProducts() {
        List<String> productNamesWithStock = new ArrayList<>();

        for (Product product : products) {
            if (product.hasStock() && !product.hasPromotion()) {
                productNamesWithStock.add(product.getName());
            }
        }

        List<Product> outOfStockProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.hasPromotion() && !productNamesWithStock.contains(product.getName())) {
                Product outOfStockProduct = product.createOutOfStockProduct();
                outOfStockProducts.add(outOfStockProduct);
                productNamesWithStock.add(product.getName());
            }
        }
        products.addAll(outOfStockProducts);
    }

    public String generateProductListView() {
        StringBuilder viewBuilder = new StringBuilder();
        viewBuilder.append(WELCOME_MESSAGE);
        for (Product product : products) {
            viewBuilder.append(PRODUCT_PREFIX).append(product.formatProductInfo()).append("\n");
        }
        return viewBuilder.toString();
    }

    public List<Product> findByName(String productName) {
        List<Product> requestProduct = new ArrayList<>();
        for (Product product : products) {
            if (product.productNameEqual(productName)) {
                requestProduct.add(product);
            }
        }
        return requestProduct;
    }

    public void updateProductStock(String productName, int quantity) {
        for (Product product : products) {
            if (product.productNameEqual(productName)) {
                product.decrementStock(quantity);
            }
        }
    }

    public void saveProductsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCT_LIST_COVER_FILE))) {
            writer.write(FILE_HEADER);
            for (Product product : products) {
                writer.write(product.toCsvFormat() + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

}
