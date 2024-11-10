package store.controller;

import store.domain.Receipt;
import store.service.StoreService;

import store.view.InputView;
import store.view.OutputView;


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
        membershipMessage();
    }

    private void storeOpeningMessage() {
        String openiningMent = storeService.generateOpeningMessage();
        outputView.printOpeningMent(openiningMent);
    }

    private void customerPurchase() {
        String answer = inputView.getPurchaseinfo();
        Receipt receipt = storeService.processPayment(answer);
        outputView.printReceipt(receipt.generateReceiptString());
    }


    private void membershipMessage() {
    }
}
