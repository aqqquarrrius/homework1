package org.example;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnalyticsServiceMonthlySpendingTest {
    // Тесты getMonthlySpendingByCategory()
    @Test
    void shouldReturnZeroWhenBankAccountIsNull() {
        AnalyticsService analyticsService = new AnalyticsService();

        BigDecimal result = analyticsService.getMonthlySpendingByCategory(null, "FOOD");

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void shouldReturnZeroWhenBankCategoryIsNull() {
        AnalyticsService analyticsService = new AnalyticsService();
        User user = new User("1", "Anna");
        BankAccount bankAccount = new BankAccount("123", user);

        BigDecimal result = analyticsService.getMonthlySpendingByCategory(bankAccount, null);

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void shouldReturnZeroWhenCategoryIsBlank() {
        AnalyticsService analyticsService = new AnalyticsService();
        User user = new User("1", "Anna");
        BankAccount bankAccount = new BankAccount("123", user);

        BigDecimal result = analyticsService.getMonthlySpendingByCategory(bankAccount, "  ");

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void shouldReturnZeroWhenAccountHasNotTransactions() {
        AnalyticsService analyticsService = new AnalyticsService();
        User user = new User("1", "Anna");
        BankAccount bankAccount = new BankAccount("123", user);

        BigDecimal result = analyticsService.getMonthlySpendingByCategory(bankAccount, "FOOD");

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void shouldReturnZeroWhenTransactionIsDeposit() {
        AnalyticsService analyticsService = new AnalyticsService();
        User user = new User("1", "Anna");
        BankAccount bankAccount = new BankAccount("123", user);

        Transaction transaction = Transaction.builder()
            .id("3")
            .amount(new BigDecimal("200"))
            .type(TransactionType.DEPOSIT)
            .date(LocalDateTime.now())
            .targetAccount(bankAccount)
            .build();

        bankAccount.addTransaction(transaction);

        BigDecimal result = analyticsService.getMonthlySpendingByCategory(bankAccount, "FOOD");
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void shouldReturnPaymentAmountByCategory() {
        AnalyticsService analyticsService = new AnalyticsService();
        User user = new User("1", "Anna");
        BankAccount bankAccount = new BankAccount("123", user);

        Transaction transaction = Transaction.builder()
            .id("3")
            .amount(new BigDecimal("200"))
            .type(TransactionType.PAYMENT)
            .category("FOOD")
            .date(LocalDateTime.now())
            .sourceAccount(bankAccount)
            .build();

        bankAccount.addTransaction(transaction);

        BigDecimal result = analyticsService.getMonthlySpendingByCategory(bankAccount, "FOOD");
        assertEquals(new BigDecimal("200"), result);
    }

    @Test
    void shouldReturnZeroWhenPaymentHasDifferentCategory() {
        AnalyticsService analyticsService = new AnalyticsService();
        User user = new User("1", "Anna");
        BankAccount bankAccount = new BankAccount("123", user);

        Transaction transaction = Transaction.builder()
            .id("3")
            .amount(new BigDecimal("300"))
            .type(TransactionType.PAYMENT)
            .category("TRANSPORT")
            .date(LocalDateTime.now())
            .sourceAccount(bankAccount)
            .build();

        bankAccount.addTransaction(transaction);

        BigDecimal result = analyticsService.getMonthlySpendingByCategory(bankAccount, "FOOD");
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void shouldSumMultiplePaymentsByCategory() {
        AnalyticsService analyticsService = new AnalyticsService();
        User user = new User("1", "Anna");
        BankAccount bankAccount = new BankAccount("123", user);

        Transaction transaction1 = Transaction.builder()
            .id("1")
            .amount(new BigDecimal("200"))
            .type(TransactionType.PAYMENT)
            .category("FOOD")
            .date(LocalDateTime.now())
            .sourceAccount(bankAccount)
            .build();

        Transaction transaction2 = Transaction.builder()
            .id("2")
            .amount(new BigDecimal("300"))
            .type(TransactionType.PAYMENT)
            .category("FOOD")
            .date(LocalDateTime.now())
            .sourceAccount(bankAccount)
            .build();

        Transaction transaction3 = Transaction.builder()
            .id("3")
            .amount(new BigDecimal("100"))
            .type(TransactionType.PAYMENT)
            .category("TRANSPORT")
            .date(LocalDateTime.now())
            .sourceAccount(bankAccount)
            .build();
        bankAccount.addTransaction(transaction1);
        bankAccount.addTransaction(transaction2);
        bankAccount.addTransaction(transaction3);

        BigDecimal result = analyticsService.getMonthlySpendingByCategory(bankAccount, "FOOD");
        assertEquals(new BigDecimal("500"), result);
    }

    @Test
    void shouldIgnorePaymentsOlderThanOneMonth() {
        AnalyticsService analyticsService = new AnalyticsService();
        User user = new User("1", "Anna");
        BankAccount bankAccount = new BankAccount("123", user);

        Transaction transaction1 = Transaction.builder()
            .id("1")
            .amount(new BigDecimal("200"))
            .type(TransactionType.PAYMENT)
            .category("FOOD")
            .date(LocalDateTime.now())
            .sourceAccount(bankAccount)
            .build();

        Transaction transaction2 = Transaction.builder()
            .id("2")
            .amount(new BigDecimal("300"))
            .type(TransactionType.PAYMENT)
            .category("FOOD")
            .date(LocalDateTime.now().minusMonths(2))
            .sourceAccount(bankAccount)
            .build();

        bankAccount.addTransaction(transaction1);
        bankAccount.addTransaction(transaction2);

        BigDecimal result = analyticsService.getMonthlySpendingByCategory(bankAccount, "FOOD");
        assertEquals(new BigDecimal("200"), result);
    }

    @Test
    void shouldReturnPaymentsEqualsOneMonthAgo() {
        AnalyticsService analyticsService = new AnalyticsService();
        User user = new User("1", "Anna");
        BankAccount bankAccount = new BankAccount("123", user);

        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1).plusSeconds(1);
        Transaction transaction1 = Transaction.builder()
            .id("1")
            .amount(new BigDecimal("200"))
            .type(TransactionType.PAYMENT)
            .category("FOOD")
            .date(oneMonthAgo)
            .sourceAccount(bankAccount)
            .build();

        bankAccount.addTransaction(transaction1);
        BigDecimal result = analyticsService.getMonthlySpendingByCategory(bankAccount, "FOOD");
        assertEquals(new BigDecimal("200"), result);
    }
}