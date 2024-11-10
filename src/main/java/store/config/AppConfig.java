package store.config;

import store.controller.StoreController;
import store.domain.Products;
import store.domain.Promotions;
import store.service.FileDataLoaderService;
import store.service.StoreService;
import store.service.UserInteractionCallback;
import store.util.File.FileReader;
import store.view.InputView;
import store.view.OutputView;

public class AppConfig {
    public StoreController storeController() {
        StoreController controller = new StoreController(inputView(), outputView());
        StoreService service = storeService(controller);
        controller.setStoreService(service);
        return controller;
    }

    private StoreService storeService(UserInteractionCallback callback) {
        Products products = fileDataLoader().loadProductsFromFile();
        Promotions promotions = fileDataLoader().loadPromotionsFromFile();
        return new StoreService(products, promotions, callback);
    }

    private InputView inputView() {
        return new InputView();
    }

    private OutputView outputView() {
        return new OutputView();
    }

    public FileDataLoaderService fileDataLoader() {
        return new FileDataLoaderService(fileReader());
    }

    private FileReader fileReader() {
        return new FileReader();
    }
}

