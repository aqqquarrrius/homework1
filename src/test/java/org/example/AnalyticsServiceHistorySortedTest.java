package org.example;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnalyticsServiceHistorySortedTest {
    // тесты getTransactionHistorySortedByAmount()
    @Test
    void shouldReturnEmptyLinkedHashMapWhenUserIsNull() {
        AnalyticsService analyticsService = new AnalyticsService();

        LinkedHashMap<String, List<Transaction>> result = analyticsService.getTransactionHistorySortedByAmount(null);
        assertTrue(result.isEmpty());
        assertInstanceOf(LinkedHashMap.class, result);
    }

    @Test
    void shouldReturnEmptyLinkedHashMapWhenUserHasNoAccounts() {
        AnalyticsService analyticsService = new AnalyticsService();
        User user = new User("1", "Anna");
        LinkedHashMap<String, List<Transaction>> result = analyticsService.getTransactionHistorySortedByAmount(user);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyLinkedHashMapWhenAccountHasNoTransactions() {
        AnalyticsService analyticsService = new AnalyticsService();
        User user = new User("1", "Anna");
        BankAccount bankAccount = new BankAccount("1", user);
        user.addAccount(bankAccount);
        LinkedHashMap<String, List<Transaction>> result = analyticsService.getTransactionHistorySortedByAmount(user);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldGroupPaymentTransactionsByCategory() {
        AnalyticsService analyticsService = new AnalyticsService();
        User user = new User("1", "Anna");
        BankAccount bankAccount = new BankAccount("1", user);
        user.addAccount(bankAccount);
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

        LinkedHashMap<String, List<Transaction>> result = analyticsService.getTransactionHistorySortedByAmount(user);
        assertEquals(2, result.size());
        assertEquals(2, result.get("FOOD").size());
        assertEquals(1, result.get("TRANSPORT").size());
    }

    @Test
    void shouldSortTransactionsByAmountDescendingWithinEachCategory() {
        AnalyticsService analyticsService = new AnalyticsService();
        User user = new User("1", "Anna");
        BankAccount bankAccount = new BankAccount("1", user);
        user.addAccount(bankAccount);
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
            .category("FOOD")
            .date(LocalDateTime.now())
            .sourceAccount(bankAccount)
            .build();
        bankAccount.addTransaction(transaction1);
        bankAccount.addTransaction(transaction2);
        bankAccount.addTransaction(transaction3);
        LinkedHashMap<String, List<Transaction>> result = analyticsService.getTransactionHistorySortedByAmount(user);
        assertEquals(transaction2, result.get("FOOD").get(0));
        assertEquals(transaction1, result.get("FOOD").get(1));
        assertEquals(transaction3, result.get("FOOD").get(2));
    }
}