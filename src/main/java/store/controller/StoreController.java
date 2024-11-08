package store.controller;

import store.domain.Products;
import store.service.FileDataLoaderService;
import store.service.StoreService;
import store.util.parser.InputParser;
import store.util.validator.InputValidator;
import store.view.InputView;
import store.view.OutputView;

import java.util.Map;

public class StoreController {
    private InputView inputView;
    private OutputView outputView;
    private StoreService storeService;

    public StoreController(InputView inputView, OutputView outputView, StoreService storeService) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.storeService = storeService;
    }

    public void run() {
        storeOpeningMessage();
        customerPurchase();
    }

    private void storeOpeningMessage() {
        String openiningMent = storeService.generateOpeningMessage();
        outputView.printOpeningMent(openiningMent);
    }

    private void customerPurchase() {
        String answer = inputView.getPurchaseinfo();
        storeService.processPayment(answer);
    }
}
