package store.domain;

import java.util.ArrayList;
import java.util.List;

public class Products {

    private final List<Product> products;

    public Products(List<Product> products) {
        this.products = products;
    }

    public String generateProductListView() {
        StringBuilder viewBuilder = new StringBuilder();
        viewBuilder.append("안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n\n");

        for (Product product : products) {
            viewBuilder.append("- ").append(product.formatProductInfo()).append("\n");
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
}
