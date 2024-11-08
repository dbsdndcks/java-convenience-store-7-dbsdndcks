package store.config;

import store.controller.StoreController;
import store.service.FileDataLoaderService;
import store.service.StoreService;
import store.util.File.FileReader;
import store.view.InputView;
import store.view.OutputView;

public class AppConfig {
    public StoreController storeController() {
        return new StoreController(inputView(), outputView(), storeService());
    }

    private InputView inputView() {
        return new InputView();
    }

    private OutputView outputView() {
        return new OutputView();
    }

    private StoreService storeService(){return fileDataLoader().initializeStoreService();}

    public FileDataLoaderService fileDataLoader() {
        return new FileDataLoaderService(fileReader());
    }

    private FileReader fileReader() {
        return new FileReader();
    }


}
