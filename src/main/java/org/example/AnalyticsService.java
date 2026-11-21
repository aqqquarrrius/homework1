package org.example;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

public class AnalyticsService {

    public BigDecimal getMonthlySpendingByCategory(
        BankAccount bankAccount, String category) {
        if (bankAccount == null || category == null || category.isBlank()) {
            return BigDecimal.ZERO;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneMonthAgo = now.minusMonths(1);
        BigDecimal paymentSum = BigDecimal.ZERO;
        for (Transaction transaction : bankAccount.getTransactions()) {
            if (transaction.getType() == TransactionType.PAYMENT &&
                Objects.equals(category, transaction.getCategory()) &&
                !transaction.getDate().isBefore(oneMonthAgo) &&
                !transaction.getDate().isAfter(now)) {
                paymentSum = paymentSum.add(transaction.getAmount());
            }
        }
        return paymentSum;
    }

    public Map<String, BigDecimal> getMonthlySpendingByCategories(User user, Set<String> categories) {
        if (user == null || categories == null || categories.isEmpty()) {
            return new HashMap<>();
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneMonthAgo = now.minusMonths(1);
        Map<String, BigDecimal> spendingByCategory = new HashMap<>();
        for (BankAccount bankAccount : user.getAccounts()) {
            for (Transaction transaction : bankAccount.getTransactions()) {
                if (transaction.getType() == TransactionType.PAYMENT &&
                    categories.contains(transaction.getCategory()) &&
                    !transaction.getDate().isBefore(oneMonthAgo) &&
                    !transaction.getDate().isAfter(now)) {
                    BigDecimal oldSum = spendingByCategory.getOrDefault(transaction.getCategory(), BigDecimal.ZERO);
                    spendingByCategory.put(transaction.getCategory(), oldSum.add(transaction.getAmount()));
                }
            }
        }
        return spendingByCategory;
    }

    public LinkedHashMap<String, List<Transaction>> getTransactionHistorySortedByAmount(User user) {
        if (user == null) {
            return new LinkedHashMap<>();
        }
        LinkedHashMap<String, List<Transaction>> sortedByAmount = new LinkedHashMap<>();
        for (BankAccount bankAccount : user.getAccounts()) {
            for (Transaction transaction : bankAccount.getTransactions()) {
                if (transaction.getType() == TransactionType.PAYMENT) {
                    List<Transaction> transactionList = sortedByAmount.getOrDefault(transaction.getCategory(), new ArrayList<>());
                    transactionList.add(transaction);
                    sortedByAmount.put(transaction.getCategory(), transactionList);
                }
            }
        }
        sortedByAmount.values().forEach(list -> {
            list.sort(Comparator.comparing(Transaction::getAmount).reversed());
        });
        return sortedByAmount;
    }

    public List<Transaction> getLastNTransactions(User user, int n) {
        if (user == null || n <= 0) {
            return new ArrayList<>();
        }
        List<Transaction> allTransactions = new ArrayList<>();
        for (BankAccount bankAccount : user.getAccounts()) {
            for (Transaction transaction : bankAccount.getTransactions()) {
                allTransactions.add(transaction);
            }
        }
        allTransactions.sort(Comparator.comparing(Transaction::getDate).reversed());
        int endIndex = Math.min(n, allTransactions.size());
        return new ArrayList<>(allTransactions.subList(0, endIndex));
    }


    public PriorityQueue<Transaction> getTopNLargestTransactions(User user, int n) {
        if (user == null || n <= 0) {
            return new PriorityQueue<>();
        }
        PriorityQueue<Transaction> topNTransactions = new PriorityQueue<>(Comparator.comparing(Transaction::getAmount));
        for (BankAccount bankAccount : user.getAccounts()) {
            for (Transaction transaction : bankAccount.getTransactions()) {
                if (transaction.getType() == TransactionType.PAYMENT) {
                    topNTransactions.add(transaction);
                    if(topNTransactions.size() > n) {
                        topNTransactions.poll();
                    }
                }
            }
        }
        return topNTransactions;
    }
}

