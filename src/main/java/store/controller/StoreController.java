package store.controller;

import store.domain.Receipt;
import store.service.StoreService;

import store.service.UserInteractionCallback;
import store.util.exception.RestartException;
import store.view.InputView;
import store.view.OutputView;


public class StoreController implements UserInteractionCallback {
    private InputView inputView;
    private OutputView outputView;
    private StoreService storeService;

    public StoreController(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void setStoreService(StoreService storeService) {
        this.storeService = storeService;
    }

    public void run() {
        try {
            storeOpeningMessage();
            customerPurchase();
            membershipMessage();
        } catch (RestartException re) {
            run();
        }
    }
    private void storeOpeningMessage() {
        String openiningMent = storeService.generateOpeningMessage();
        outputView.printProducts(openiningMent);
    }

    private void customerPurchase() {
        String answer = inputView.readItem();
        Receipt receipt = storeService.processPayment(answer);
        outputView.printReceipt(receipt.generateReceiptString());
    }


    private void membershipMessage() {
    }


    @Override
    public boolean askUser(String message) {
        String answer = inputView.InputMessage(message);
        return "Y".equals(answer);
    }
}
