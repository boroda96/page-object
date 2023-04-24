package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;
import static java.lang.String.valueOf;

public class TransactionPage {
    private SelenideElement sumAmount = $("[data-test-id=amount] input");
    private SelenideElement fromAccount = $("[data-test-id=from] input");
    private SelenideElement clickReplenish = $("[data-test-id=action-transfer]");
    //перевод
    public void transferMoney(int amount, DataHelper.CardsInfo from) {
        sumAmount.setValue(valueOf(amount));
        fromAccount.setValue(String.valueOf(from));
        clickReplenish.click();
        new CardBalance();
    }

    public void errorLimit() {
        $("[data-test-id=error-notification]").should(Condition.exactText("Ошибка"));
    }

    public void invalidCard() {
        $("[data-test-id=error-notification]").should(Condition.text("Ошибка! "));
    }
}