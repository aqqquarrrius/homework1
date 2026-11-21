package org.example;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnalyticsServiceLastNTransactionsTest {
    //тесты для getLastNTransactions()
    @Test
    void shouldReturnEmptyListWhenUserIsNull() {
        AnalyticsService analyticsService = new AnalyticsService();
        List<Transaction> result = analyticsService.getLastNTransactions(null, 5);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenNIsZeroOrNegative() {
        AnalyticsService analyticsService = new AnalyticsService();
        User user = new User("1", "Anna");
        assertAll(
            () -> assertTrue(analyticsService.getLastNTransactions(user, 0).isEmpty()),
            () -> assertTrue(analyticsService.getLastNTransactions(user, -5).isEmpty())
        );
    }

    @Test
    void shouldReturnEmptyListWhenUserHasNoAccounts() {
        AnalyticsService analyticsService = new AnalyticsService();
        User user = new User("1", "Anna");
        List<Transaction> result = analyticsService.getLastNTransactions(user, 5);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenAccountsHaveNoTransactions() {
        AnalyticsService analyticsService = new AnalyticsService();
        User user = new User("1", "Anna");
        BankAccount bankAccount = new BankAccount("123", user);
        user.addAccount(bankAccount);
        List<Transaction> result = analyticsService.getLastNTransactions(user, 5);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnLastNTransactionsFromAllAccountsSortedByDate() {
        AnalyticsService analyticsService = new AnalyticsService();
        User user = new User("1", "Anna");
        BankAccount bankAccount1 = new BankAccount("123", user);
        user.addAccount(bankAccount1);
        Transaction transaction1 = Transaction.builder()
            .id("1")
            .amount(new BigDecimal("200"))
            .type(TransactionType.PAYMENT)
            .category("FOOD")
            .date(LocalDateTime.of(2026, 11, 10, 10, 0))
            .sourceAccount(bankAccount1)
            .build();

        Transaction transaction2 = Transaction.builder()
            .id("2")
            .amount(new BigDecimal("300"))
            .type(TransactionType.PAYMENT)
            .category("TRANSPORT")
            .date(LocalDateTime.of(2026, 11, 15, 12, 10))
            .sourceAccount(bankAccount1)
            .build();

        bankAccount1.addTransaction(transaction1);
        bankAccount1.addTransaction(transaction2);

        BankAccount bankAccount2 = new BankAccount("456", user);
        user.addAccount(bankAccount2);
        Transaction transaction3 = Transaction.builder()
            .id("3")
            .amount(new BigDecimal("500"))
            .type(TransactionType.PAYMENT)
            .category("FOOD")
            .date(LocalDateTime.of(2026, 11, 12, 10, 0))
            .sourceAccount(bankAccount2)
            .build();

        Transaction transaction4 = Transaction.builder()
            .id("4")
            .amount(new BigDecimal("100"))
            .type(TransactionType.PAYMENT)
            .category("TRANSPORT")
            .date(LocalDateTime.of(2026, 11, 18, 12, 10))
            .sourceAccount(bankAccount2)
            .build();

        bankAccount2.addTransaction(transaction3);
        bankAccount2.addTransaction(transaction4);
        List<Transaction> result = analyticsService.getLastNTransactions(user, 2);
        assertEquals(List.of(transaction4, transaction2), result);
    }

    @Test
    void shouldReturnAllTransactionsWhenNIsGreaterThanTransactionCount() {
        AnalyticsService analyticsService = new AnalyticsService();
        User user = new User("1", "Anna");
        BankAccount bankAccount = new BankAccount("1", user);
        user.addAccount(bankAccount);
        Transaction transaction1 = Transaction.builder()
            .id("1")
            .amount(new BigDecimal("200"))
            .type(TransactionType.PAYMENT)
            .category("FOOD")
            .date(LocalDateTime.of(2026, 11, 10, 10, 0))
            .sourceAccount(bankAccount)
            .build();

        Transaction transaction2 = Transaction.builder()
            .id("2")
            .amount(new BigDecimal("300"))
            .type(TransactionType.PAYMENT)
            .category("TRANSPORT")
            .date(LocalDateTime.of(2026, 11, 15, 12, 10))
            .sourceAccount(bankAccount)
            .build();
        bankAccount.addTransaction(transaction1);
        bankAccount.addTransaction(transaction2);
        List<Transaction> result = analyticsService.getLastNTransactions(user, 5);
        assertEquals(List.of(transaction2, transaction1), result);
    }
}