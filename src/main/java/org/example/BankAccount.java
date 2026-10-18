package org.example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BankAccount {
    private String accountNumber;
    private BigDecimal balance = BigDecimal.ZERO;
    private User owner;
    private List<Transaction> transactions = new ArrayList<>();

    public BankAccount(String accountNumber, User owner) {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("Некорректный номер счета");
        }
        if (owner == null) {
            throw new IllegalArgumentException("Владелец счёта не может быть null");
        }
        this.accountNumber = accountNumber;
        this.owner = owner;
    }

    public boolean deposit(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) return false;
        balance = balance.add(amount);
        return true;
    }

    public boolean withdraw(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) return false;
        if (0 > balance.compareTo(amount)) {
            return false;
        }
        balance = balance.subtract(amount);
        return true;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return List.copyOf(transactions);
    }

    public void addTransaction(Transaction transaction) {
        if (transaction != null) {
            transactions.add(transaction);
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public User getOwner() {
        return owner;
    }

    public String toString() {
        return "{accountNumber= " + accountNumber +
            ", balance=" + balance +
            "}";
    }
}
