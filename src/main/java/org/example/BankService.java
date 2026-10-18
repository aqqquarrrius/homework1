package org.example;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BankService {
    private List<BankAccount> accounts = new ArrayList<>();

    public BankAccount createAccount(User user, String accountNumber) {
        if (user == null) {
            throw new IllegalArgumentException("Юзер не должен быть null");
        }

        if(accountNumber == null || accountNumber.isBlank()){
            throw new IllegalArgumentException("Номер счета не должен быть пустым");
        }
        for (BankAccount account : accounts) {
            if (accountNumber.equals(account.getAccountNumber())) {
                throw new IllegalArgumentException("Номер счета должен быть уникальным");
            }
        }
        BankAccount bankAccount = new BankAccount(accountNumber, user);
        user.addAccount(bankAccount);
        accounts.add(bankAccount);
        return bankAccount;
    }

    public boolean transfer(BankAccount source, BankAccount target, BigDecimal amount) {
        if(source == null || target == null || amount == null) return false;
        if(amount.signum() <= 0) return false;
        if(source.getBalance().compareTo(amount) < 0) return false;
        if(!source.withdraw(amount)) return false;
        if(!target.deposit(amount)) {
            source.deposit(amount);
                return false;
        }
        Transaction transaction = new Transaction(
            UUID.randomUUID().toString(),
            amount,
            "TRANSFER",
            LocalDateTime.now(),
            source,
            target
        );
        source.addTransaction(transaction);
        target.addTransaction(transaction);
        return true;
    }

    public List<Transaction> getTransactionHistory(BankAccount bankAccount){
        if(bankAccount == null){
            throw new IllegalArgumentException("Счет не должен быть null");
        }
    return bankAccount.getTransactions();
    }

    public BigDecimal getTotalBalance(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User не должен быть null");
        }
        BigDecimal totalBalance = BigDecimal.ZERO;
        for(BankAccount account : user.getAccounts()) {
            totalBalance = totalBalance.add(account.getBalance());
        }
        return totalBalance;
    }
}
