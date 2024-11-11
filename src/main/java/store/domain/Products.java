package store.domain;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Products {

    private final List<Product> products;
    private static final String PRODUCT_LIST_FILE = "products.md";

    public Products(List<Product> products) {
        this.products = products;
        addOutOfStockProducts();
    }

    private void addOutOfStockProducts() {
        List<String> productNamesWithStock = new ArrayList<>();

        // 일반 상품이 존재하는 이름을 수집
        for (Product product : products) {
            if (product.hasStock() && !product.hasPromotion()) {
                productNamesWithStock.add(product.getName());
            }
        }

        // 프로모션 상품만 있고 일반 재고가 없는 경우에 "재고 없음" 상태로 추가
        List<Product> outOfStockProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.hasPromotion() && !productNamesWithStock.contains(product.getName())) {
                Product outOfStockProduct = product.createOutOfStockProduct();
                outOfStockProducts.add(outOfStockProduct);
                productNamesWithStock.add(product.getName());  // 중복 추가 방지
            }
        }
        products.addAll(outOfStockProducts);
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

    // 상품 재고 업데이트 메서드
    public void updateProductStock(String productName, int quantity) {
        for (Product product : products) {
            if (product.productNameEqual(productName)) {
                product.decrementStock(quantity);  // 재고 감소
            }
        }
    }

    // 변경된 상품 목록을 파일에 저장
    public void saveProductsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCT_LIST_FILE))) {
            writer.write("name,price,quantity,promotion\n");
            for (Product product : products) {
                writer.write(product.toCsvFormat() + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

}
