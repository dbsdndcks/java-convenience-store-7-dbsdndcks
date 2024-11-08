package store.service;

import store.domain.Product;
import store.domain.Products;
import store.domain.Promotion;
import store.domain.Promotions;
import store.util.File.FileReader;
import store.util.parser.ProductParser;
import store.util.parser.PromotionParser;

import java.util.List;

public class FileDataLoaderService {
    private static final String PRODUCT_LIST_FILE = "products.md";
    private static final String PROMOTION_LIST_FILE = "promotions.md";

    private final FileReader fileReader;

    public FileDataLoaderService(FileReader fileReader) {
        this.fileReader = fileReader;
    }

    public Products loadProductsFromFile() {
        List<Product> productList = fileReader.readFile(PRODUCT_LIST_FILE, ProductParser::parse);
        return new Products(productList);
    }

    public Promotions loadPromotionsFromFile() {
        List<Promotion> promotionList = fileReader.readFile(PROMOTION_LIST_FILE, PromotionParser::parse);
        return new Promotions(promotionList);
    }

    public StoreService initializeStoreService() {
        Products products = loadProductsFromFile();
        Promotions promotions = loadPromotionsFromFile();

        return new StoreService(products, promotions);
    }
}
