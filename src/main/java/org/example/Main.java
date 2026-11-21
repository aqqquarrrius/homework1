package org.example;

import java.math.BigDecimal;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        User user1 = new User("123", "Anna");
        User user2 = new User("123", "Maria");

        System.out.println(user1.equals(user2));

//        User user = new User("user1", "Jhon Doe");
//
//        BankService bankService = new BankService();
//
//        bankService.createAccount(user, "ACC123");
//        bankService.createAccount(user, "ACC456");
//
//        List<BankAccount> accounts = user.getAccounts();
//        BankAccount acc1 = accounts.get(0);
//        BankAccount acc2 = accounts.get(1);
//
//        acc1.deposit(new BigDecimal("1000"));
//
//        System.out.println("Перевод средств: " + bankService.transfer(acc1, acc2, new BigDecimal("500")));
////        System.out.println(bankService.transfer(acc1, acc2, new BigDecimal("600")));
////        System.out.println(bankService.transfer(acc1, acc2, new BigDecimal("-100")));
////        System.out.println(bankService.transfer(acc1, acc2, new BigDecimal("0")));
//
//        System.out.println("Внесли: " + acc1.deposit(new BigDecimal("200")));
//        System.out.println("Сняли: " + acc1.withdraw(new BigDecimal("100")));
//
//        System.out.println("Баланс счета ACC1: " + acc1.getBalance());
//        System.out.println("Баланс счета ACC2: " + acc2.getBalance());
//
//        System.out.println("История транзакций счета ACC123: ");
//        System.out.println(bankService.getTransactionHistory(acc1));
//
//        System.out.println("Общий баланс: " + bankService.getTotalBalance(user));

    }
}