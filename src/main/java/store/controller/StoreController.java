package store.controller;

import store.domain.Receipt;
import store.service.StoreService;

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
        while (true) {
            try {
                storeOpeningMessage();
                customerPurchase();
                otherPurchaseMessage();
            } catch (RestartException re) {
                // 재시작 예외를 캐치하여 루프를 재실행
                continue;
            }
            // 재시작 예외가 발생하지 않는 경우 루프 종료
            break;
        }
        System.out.println("프로그램이 종료되었습니다.");
    }

    private void storeOpeningMessage() {
        String openiningMent = storeService.generateOpeningMessage();
        outputView.printProducts(openiningMent);
    }

    private void customerPurchase() {
        while (true) {
            try {
                String answer = inputView.readItem();
                Receipt receipt = storeService.processPayment(answer);
                membershipMessage(receipt);
                outputView.printReceipt(receipt.generateReceiptString());
                break;  // 성공적으로 실행되면 루프를 종료
            } catch (IllegalArgumentException e) {
                outputView.printError(e.getMessage());  // 예외 메시지 출력
            }
        }
    }


    private void membershipMessage(Receipt receipt) {
        String answer = inputView.membershipMessage();
        if (answer.equals("Y")) {
            storeService.calculateMembership(receipt);
        }
        if (answer.equals("N")) {
            storeService.calculateRegular(receipt);
        }
    }

    private void otherPurchaseMessage() {
        String answer = inputView.otherPurchaseMessage();
        if (answer.equals("Y")) {
            throw new RestartException();
        }
        if (answer.equals("N")) {

        }
    }


    @Override
    public boolean askUser(String message) {
        String answer = inputView.InputMessage(message);
        return "Y".equals(answer);
    }
}
