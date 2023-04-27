package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.CardBalance;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.getCard;


public class MoneyTransferTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    public void shouldTransferFrom1To2() {
        val cardBalance = new CardBalance();
        val amount = 6000;
        val balanceOne = cardBalance.getCardBalance(1);
        val balanceTwo = cardBalance.getCardBalance(2);
        val transactionPage = cardBalance.transferClick(2);
        transactionPage.transferMoney(Integer.toString(amount), getCard(1).getCardNumber());
        val firstCardBalanceFinish = cardBalance.getCardBalance(1);
        val secondCardBalanceFinish = cardBalance.getCardBalance(2);
        assertEquals(balanceOne - amount, firstCardBalanceFinish);
        assertEquals(balanceTwo + amount, secondCardBalanceFinish);
    }

    @Test
    public void shouldTransferFrom2To1() {
        int amount = 2500;
        val cardBalance = new CardBalance();
        val balanceOne = cardBalance.getCardBalance(1);
        val balanceTwo = cardBalance.getCardBalance(2);
        val transactionPage = cardBalance.transferClick(1);
        transactionPage.transferMoney(Integer.toString(amount), getCard(2).getCardNumber());
        val firstCardBalanceFinish = cardBalance.getCardBalance(1);
        val secondCardBalanceFinish = cardBalance.getCardBalance(2);
        assertEquals(balanceOne + amount, firstCardBalanceFinish);
        assertEquals(balanceTwo - amount, secondCardBalanceFinish);
    }

    @Test
    public void shouldTransferFrom1To1() {
        int amount = 3333;
        val cardBalance = new CardBalance();
        val transactionPage = cardBalance.transferClick(1);
        transactionPage.transferMoney(Integer.toString(amount), getCard(1).getCardNumber());
        transactionPage.invalidCard();
    }

    @Test
    public void shouldTransferFrom1To2OverLimit() {
        int amount = 15555;
        val cardBalance = new CardBalance();
        val transactionPage = cardBalance.transferClick(2);
        transactionPage.transferMoney(Integer.toString(amount), getCard(1).getCardNumber());
        transactionPage.errorLimit();
    }
}
