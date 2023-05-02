package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.CardBalance;
import ru.netology.page.LoginPage;
import ru.netology.page.TransactionPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.getCard;
import static ru.netology.data.DataHelper.getVerificationCodeFor;


public class MoneyTransferTest {
    LoginPage loginPage;
    CardBalance cardBalance;

    @BeforeEach
    void setup() {
        loginPage = open("http://localhost:9999", LoginPage.class);
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = getVerificationCodeFor(authInfo);
        cardBalance = verificationPage.validVerify(verificationCode);
    }

    @Test
    public void shouldTransferFrom1To2() {
        val amount = 6000;
        val firstCardBalance = cardBalance.getCardBalance(1);
        val secondCardBalance = cardBalance.getCardBalance(2);
        val expectedBalanceOne = firstCardBalance - amount;
        val expectedBalanceTwo = secondCardBalance + amount;
        val transactionPage = cardBalance.transferClick(2);
        transactionPage.makeValidTransfer(Integer.toString(amount), getCard(1).getCardNumber());

        val actualBalanceOne = cardBalance.getCardBalance(1);
        val actualBalanceTwo = cardBalance.getCardBalance(2);
        assertEquals(expectedBalanceOne, actualBalanceOne);
        assertEquals(expectedBalanceTwo, actualBalanceTwo);
    }

    @Test
    public void shouldTransferFrom2To1() {
        int amount = 2500;
        val firstCardBalance = cardBalance.getCardBalance(1);
        val secondCardBalance = cardBalance.getCardBalance(2);
        val expectedBalanceOne = firstCardBalance + amount;
        val expectedBalanceTwo = secondCardBalance - amount;
        val transactionPage = cardBalance.transferClick(1);
        transactionPage.makeValidTransfer(Integer.toString(amount), getCard(2).getCardNumber());

        val actualBalanceOne = cardBalance.getCardBalance(1);
        val actualBalanceTwo = cardBalance.getCardBalance(2);
        assertEquals(expectedBalanceOne, actualBalanceOne);
        assertEquals(expectedBalanceTwo, actualBalanceTwo);
    }

    @Test
    public void shouldTransferFrom1To1() {
        int amount = 3333;
        val firstCardBalance = cardBalance.getCardBalance(1);
        val secondCardBalance = cardBalance.getCardBalance(2);
        val transactionPage = cardBalance.transferClick(1);
        transactionPage.transferMoney(Integer.toString(amount), getCard(1).getCardNumber());
        transactionPage.invalidCard();
        val actualFirstCardBalance = cardBalance.getCardBalance(1);
        val actualSecondCardBalance = cardBalance.getCardBalance(2);
        assertEquals(firstCardBalance, actualFirstCardBalance);
        assertEquals(secondCardBalance, actualSecondCardBalance);
    }

    @Test
    public void shouldTransferFrom1To2OverLimit() {
        int amount = 15555;
        val firstCardBalance = cardBalance.getCardBalance(1);
        val secondCardBalance = cardBalance.getCardBalance(2);
        val transactionPage = cardBalance.transferClick(2);
        transactionPage.transferMoney(Integer.toString(amount), getCard(1).getCardNumber());
        transactionPage.errorLimit();
        val actualFirstCardBalance = cardBalance.getCardBalance(1);
        val actualSecondCardBalance = cardBalance.getCardBalance(2);
        assertEquals(firstCardBalance, actualFirstCardBalance);
        assertEquals(secondCardBalance, actualSecondCardBalance);
    }
}
