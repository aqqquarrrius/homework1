package org.example;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnalyticsServiceSpendingByCategoriesTest {
    // Тесты getMonthlySpendingByCategories()
    @Test
    void shouldReturnEmptyMapWhenUserIsNull() {
        AnalyticsService analyticsService = new AnalyticsService();

        Map<String, BigDecimal> result = analyticsService.getMonthlySpendingByCategories(null, Set.of("FOOD", "TRANSPORT"));
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyMapWhenCategoriesIsEmpty() {
        AnalyticsService analyticsService = new AnalyticsService();
        User user = new User("1", "Anna");
        Map<String, BigDecimal> result = analyticsService.getMonthlySpendingByCategories(user, Set.of());
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyMapWhenCategoriesIsNull() {
        AnalyticsService analyticsService = new AnalyticsService();
        User user = new User("1", "Anna");
        Map<String, BigDecimal> result = analyticsService.getMonthlySpendingByCategories(user, null);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyMapWhenUserHasNoAccounts() {
        AnalyticsService analyticsService = new AnalyticsService();
        User user = new User("1", "Anna");
        Map<String, BigDecimal> result = analyticsService.getMonthlySpendingByCategories(user, Set.of("FOOD", "TRANSPORT"));
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnMonthlySpendingForSelectedCategory() {
        AnalyticsService analyticsService = new AnalyticsService();
        User user = new User("1", "Anna");
        BankAccount bankAccount = new BankAccount("1", user);
        user.addAccount(bankAccount);

        Transaction transaction1 = Transaction.builder()
            .id("1")
            .amount(new BigDecimal("500"))
            .type(TransactionType.PAYMENT)
            .category("FOOD")
            .date(LocalDateTime.now())
            .sourceAccount(bankAccount)
            .build();

        bankAccount.addTransaction(transaction1);
        Map<String, BigDecimal> result = analyticsService.getMonthlySpendingByCategories(user, Set.of("FOOD"));
        assertEquals(new BigDecimal("500"), result.get("FOOD"));
        assertEquals(1, result.size());
    }

    @Test
    void shouldSumPaymentsByCategoriesAcrossAllUserAccounts() {
        AnalyticsService analyticsService = new AnalyticsService();
        User user = new User("1", "Anna");
        BankAccount bankAccount = new BankAccount("123", user);
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

        BankAccount bankAccount2 = new BankAccount("456", user);
        user.addAccount(bankAccount2);
        Transaction transaction4 = Transaction.builder()
            .id("4")
            .amount(new BigDecimal("400"))
            .type(TransactionType.PAYMENT)
            .category("FOOD")
            .date(LocalDateTime.now())
            .sourceAccount(bankAccount2)
            .build();

        Transaction transaction5 = Transaction.builder()
            .id("5")
            .amount(new BigDecimal("500"))
            .type(TransactionType.PAYMENT)
            .category("TRANSPORT")
            .date(LocalDateTime.now())
            .sourceAccount(bankAccount2)
            .build();

        Transaction transaction6 = Transaction.builder()
            .id("6")
            .amount(new BigDecimal("350"))
            .type(TransactionType.PAYMENT)
            .category("TRANSPORT")
            .date(LocalDateTime.now())
            .sourceAccount(bankAccount2)
            .build();
        bankAccount2.addTransaction(transaction4);
        bankAccount2.addTransaction(transaction5);
        bankAccount2.addTransaction(transaction6);

        Map<String, BigDecimal> result = analyticsService.getMonthlySpendingByCategories(user, Set.of("FOOD", "TRANSPORT"));
        assertEquals(new BigDecimal("900"), result.get("FOOD"));
        assertEquals(new BigDecimal("950"), result.get("TRANSPORT"));

        assertEquals(2, result.size());
    }
}