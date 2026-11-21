package org.example;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.PriorityQueue;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnalyticsServiceTopNLargestTransactionsTest {
    //тесты для getTopNLargestTransactions()
    @Test
    void shouldReturnEmptyQueueWhenUserIsNull() {
        AnalyticsService analyticsService = new AnalyticsService();
        PriorityQueue<Transaction> result = analyticsService.getTopNLargestTransactions(null, 4);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyQueueWhenNIsZeroOrNegative() {
        AnalyticsService analyticsService = new AnalyticsService();
        User user = new User("1", "Anna");
        assertAll(
            () -> assertTrue(analyticsService.getTopNLargestTransactions(user, 0).isEmpty()),
            () -> assertTrue(analyticsService.getTopNLargestTransactions(user, -5).isEmpty())
        );
    }

    @Test
    void shouldReturnEmptyQueueWhenUserHasNoAccounts() {
        AnalyticsService analyticsService = new AnalyticsService();
        User user = new User("1", "Anna");
        PriorityQueue<Transaction> result = analyticsService.getTopNLargestTransactions(user, 4);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyQueueWhenAccountsHaveNoTransactions() {
        AnalyticsService analyticsService = new AnalyticsService();
        User user = new User("1", "Anna");
        BankAccount bankAccount = new BankAccount("123", user);
        user.addAccount(bankAccount);
        PriorityQueue<Transaction> result = analyticsService.getTopNLargestTransactions(user, 4);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnOnlyPaymentTransactions() {
        AnalyticsService analyticsService = new AnalyticsService();
        User user = new User("1", "Anna");
        BankAccount bankAccount = new BankAccount("123", user);
        user.addAccount(bankAccount);
        Transaction transaction1 = Transaction.builder()
            .id("1")
            .amount(new BigDecimal("500"))
            .type(TransactionType.PAYMENT)
            .category("FOOD")
            .date(LocalDateTime.now())
            .sourceAccount(bankAccount)
            .build();

        Transaction transaction2 = Transaction.builder()
            .id("2")
            .amount(new BigDecimal("1000"))
            .type(TransactionType.DEPOSIT)
            .date(LocalDateTime.now())
            .targetAccount(bankAccount)
            .build();

        Transaction transaction3 = Transaction.builder()
            .id("3")
            .amount(new BigDecimal("700"))
            .type(TransactionType.WITHDRAWAL)
            .date(LocalDateTime.now())
            .sourceAccount(bankAccount)
            .build();

        bankAccount.addTransaction(transaction1);
        bankAccount.addTransaction(transaction2);
        bankAccount.addTransaction(transaction3);

        PriorityQueue<Transaction> result = analyticsService.getTopNLargestTransactions(user, 5);
        assertEquals(1, result.size());
        assertEquals(transaction1, result.peek());
    }

    @Test
    void shouldReturnTopNLargestPaymentTransactionsFromAllAccounts() {
        AnalyticsService analyticsService = new AnalyticsService();
        User user = new User("1", "Anna");
        BankAccount bankAccount1 = new BankAccount("123", user);
        user.addAccount(bankAccount1);
        Transaction transaction1 = Transaction.builder()
            .id("1")
            .amount(new BigDecimal("300"))
            .type(TransactionType.PAYMENT)
            .category("FOOD")
            .date(LocalDateTime.now())
            .sourceAccount(bankAccount1)
            .build();

        Transaction transaction2 = Transaction.builder()
            .id("2")
            .amount(new BigDecimal("900"))
            .type(TransactionType.PAYMENT)
            .category("TRANSPORT")
            .date(LocalDateTime.now())
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
            .date(LocalDateTime.now())
            .sourceAccount(bankAccount2)
            .build();

        Transaction transaction4 = Transaction.builder()
            .id("4")
            .amount(new BigDecimal("700"))
            .type(TransactionType.PAYMENT)
            .category("TRANSPORT")
            .date(LocalDateTime.now())
            .sourceAccount(bankAccount2)
            .build();

        bankAccount2.addTransaction(transaction3);
        bankAccount2.addTransaction(transaction4);
        PriorityQueue<Transaction> result = analyticsService.getTopNLargestTransactions(user, 2);
        assertEquals(2, result.size());
        assertEquals(transaction4, result.poll());
        assertEquals(transaction2, result.poll());
    }

    @Test
    void shouldReturnAllAvailablePaymentsWhenTransactionsCountIsLessThanN() {
        AnalyticsService analyticsService = new AnalyticsService();
        User user = new User("1", "Anna");
        BankAccount bankAccount = new BankAccount("123", user);
        user.addAccount(bankAccount);
        Transaction transaction1 = Transaction.builder()
            .id("1")
            .amount(new BigDecimal("300"))
            .type(TransactionType.PAYMENT)
            .category("FOOD")
            .date(LocalDateTime.now())
            .sourceAccount(bankAccount)
            .build();

        Transaction transaction2 = Transaction.builder()
            .id("2")
            .amount(new BigDecimal("900"))
            .type(TransactionType.PAYMENT)
            .category("TRANSPORT")
            .date(LocalDateTime.now())
            .sourceAccount(bankAccount)
            .build();
        bankAccount.addTransaction(transaction1);
        bankAccount.addTransaction(transaction2);
        PriorityQueue<Transaction> result = analyticsService.getTopNLargestTransactions(user, 4);
        assertEquals(2, result.size());
        assertEquals(transaction1, result.poll());
        assertEquals(transaction2, result.poll());
    }

    @Test
    void shouldReturnEmptyQueueWhenUserHasNoPaymentTransaction(){
        AnalyticsService analyticsService = new AnalyticsService();
        User user = new User("1", "Anna");
        BankAccount bankAccount = new BankAccount("123", user);
        user.addAccount(bankAccount);
        Transaction transaction1 = Transaction.builder()
            .id("1")
            .amount(new BigDecimal("300"))
            .type(TransactionType.WITHDRAWAL)
            .category("FOOD")
            .date(LocalDateTime.now())
            .sourceAccount(bankAccount)
            .build();

        Transaction transaction2 = Transaction.builder()
            .id("2")
            .amount(new BigDecimal("900"))
            .type(TransactionType.DEPOSIT)
            .category("TRANSPORT")
            .date(LocalDateTime.now())
            .targetAccount(bankAccount)
            .build();

        bankAccount.addTransaction(transaction1);
        bankAccount.addTransaction(transaction2);
        PriorityQueue<Transaction> result = analyticsService.getTopNLargestTransactions(user, 5);
        assertTrue(result.isEmpty());
    }
}