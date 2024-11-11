package store.view;

import camp.nextstep.edu.missionutils.Console;
import store.util.Message.QuestionMessage;

public class InputView {
    public String readItem() {
        System.out.println(QuestionMessage.READ_ITEM.getMessage());
        return Console.readLine();
    }

    public String inputMessage(String message) {
        System.out.println(message);
        return Console.readLine();
    }

    public String membershipMessage() {
        System.out.println(QuestionMessage.MEMBERSHIP_PROMPT.getMessage());
        return Console.readLine();
    }

    public String otherPurchaseMessage() {
        System.out.println(QuestionMessage.OTHER_PURCHASE.getMessage());
        return Console.readLine();
    }
}
