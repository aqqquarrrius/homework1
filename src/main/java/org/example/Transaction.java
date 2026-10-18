package org.example;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private String id;
    private BigDecimal amount;
    private String type;
    private LocalDateTime date;
    private BankAccount sourceAccount;
    private BankAccount targetAccount;

    public Transaction(String id, BigDecimal amount, String type, LocalDateTime date, BankAccount sourceAccount, BankAccount targetAccount) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Поле ID не может быть пустым");
        }
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Сумма транзакции должна быть больше нуля");
        }
        if (type == null || !(type.equals("DEPOSIT") || type.equals("WITHDRAWAL") || type.equals("TRANSFER"))) {
            throw new IllegalArgumentException("Выберите тип транзакции");
        }
        if (date == null) {
            throw new IllegalArgumentException("Дата не может быть null");
        }
        if (targetAccount == null && type.equals("DEPOSIT")) {
            throw new IllegalArgumentException("Счет получатель не может быть null");
        }
        if (sourceAccount == null && type.equals("WITHDRAWAL")) {
            throw new IllegalArgumentException("Счет отправителя не может быть null");
        }
        if ((sourceAccount == null || targetAccount == null) && type.equals("TRANSFER")) {
            throw new IllegalArgumentException("Счета не могут быть null");
        }
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.date = date;
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
    }

    public String getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public BankAccount getSourceAccount() {
        return sourceAccount;
    }
    public BankAccount getTargetAccount() {
        return targetAccount;
    }

    public String toString() {
        return "{id= " + id +
            ", amount= " + amount +
            ", type= " + type +
            ", date= " + date +
            ", sourceAccount= " + sourceAccount +
            ", targetAccount= " + targetAccount +
            "}";
    }
}
