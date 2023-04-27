package ru.netology.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static ru.netology.data.DataHelper.getCard;

public class CardBalance {

    private SelenideElement heading = $("[data-test-id=dashboard]");
    private ElementsCollection cards = $$(".list__item div");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public CardBalance() {
        heading.shouldBe(visible);
    }

    public int getCardBalance(int number) {
        val text = cards.findBy(attribute("data-test-id", getCard(number).getCardID())).text();
        return extractBalance(text);
    }

    private int extractBalance(String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    public TransactionPage transfer(DataHelper.CardsInfo cardId) {
        $("[data-test-id='" + cardId.getCardNumber() + "'] [data-test-id=action-deposit]").click();
        return new TransactionPage();
    }

    public TransactionPage transferClick(int indexCardTo) {
        cards.findBy(attribute("data-test-id", getCard(indexCardTo).getCardID()))
                .find("[data-test-id=action-deposit]").click();
        return new TransactionPage();
    }
}