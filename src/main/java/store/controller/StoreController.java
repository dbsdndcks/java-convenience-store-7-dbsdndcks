package store.controller;

import store.domain.Receipt;
import store.service.StoreService;
import store.util.exception.RestartException;
import store.util.validator.InputValidator;
import store.view.InputView;
import store.view.OutputView;

public class StoreController implements UserInteractionCallback {
    private final InputView inputView;
    private final OutputView outputView;
    private StoreService storeService;

    public StoreController(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void setStoreService(StoreService storeService) {
        this.storeService = storeService;
    }

    public void run() {
        while (true) {
            try {
                storeOpeningMessage();
                customerPurchase();
                otherPurchaseMessage();
            } catch (RestartException re) {
                continue;
            }
            return;
        }
    }

    private void storeOpeningMessage() {
        String openingMessage = storeService.generateOpeningMessage();
        outputView.printProducts(openingMessage);
    }

    private void customerPurchase() {
        while (true) {
            try {
                handlePurchase();
                return;
            } catch (IllegalArgumentException e) {
                outputView.printError(e.getMessage());
            }
        }
    }

    private void handlePurchase() {
        String answer = inputView.readItem();
        InputValidator.validateInputProduct(answer);
        Receipt receipt = purchaseProcess(answer);
        outputView.printReceipt(receipt.generateReceiptString());
    }

    private Receipt purchaseProcess(String answer) {
        Receipt receipt = storeService.processPayment(answer);
        updateStock(receipt);
        handleMembership(receipt);
        return receipt;
    }

    private void updateStock(Receipt receipt) {
        storeService.updateProductStock(receipt);
        storeService.saveProductsToFile();
    }

    private void handleMembership(Receipt receipt) {
        while (true) {
            try {
                processMembershipAnswer(receipt);
                return;
            } catch (IllegalArgumentException e) {
                outputView.printError(e.getMessage());
            }
        }
    }

    private void processMembershipAnswer(Receipt receipt) {
        String answer = inputView.membershipMessage();
        InputValidator.validateInputQuestion(answer);
        if (answer.equals("Y")) storeService.calculateMembership(receipt);
        else storeService.calculateRegular(receipt);
    }

    private void otherPurchaseMessage() {
        while (true) {
            try {
                if (handleOtherPurchase()) throw new RestartException();
                return;
            } catch (IllegalArgumentException e) {
                outputView.printError(e.getMessage());
            }
        }
    }

    private boolean handleOtherPurchase() {
        String answer = inputView.otherPurchaseMessage();
        InputValidator.validateInputQuestion(answer);
        return "Y".equals(answer);
    }

    @Override
    public boolean askUser(String message) {
        while (true) {
            try {
                return processUserAnswer(message);
            } catch (IllegalArgumentException e) {
                outputView.printError(e.getMessage());
            }
        }
    }

    private boolean processUserAnswer(String message) {
        String answer = inputView.inputMessage(message);
        InputValidator.validateInputQuestion(answer);
        return "Y".equals(answer);
    }
}
