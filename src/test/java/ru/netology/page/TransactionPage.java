package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class TransactionPage {
    private SelenideElement sumAmount = $("[data-test-id=amount] input");
    private SelenideElement fromAccount = $("[data-test-id=from] input");
    private SelenideElement transferButton = $("[data-test-id=action-transfer]");
    private SelenideElement cancelButton = $("[data-test-id=action-cancel]");
    private SelenideElement errorNotification = $("[data-test-id=error-notification]");
    private SelenideElement errorButton = $("[data-test-id=error-notification] button");


    public void transferMoney(String amount, String from) {
        sumAmount.setValue(amount);
        fromAccount.setValue(from);
        transferButton.click();
    }

    public void errorLimit() {
        $("[data-test-id=error-notification]").should(Condition.exactText("Ошибка"));
    }

    public void invalidCard() {
        $("[data-test-id=error-notification]").should(Condition.text("Ошибка! "));
    }
}


