package org.example;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Transaction {
    @ToString.Include
    @EqualsAndHashCode.Include
    private String id;
    @ToString.Include
    private BigDecimal amount;
    @ToString.Include
    private TransactionType type;
    @ToString.Include
    private String category;
    @ToString.Include
    private LocalDateTime date;
    private BankAccount sourceAccount;
    private BankAccount targetAccount;

    @Builder
    public Transaction(String id, BigDecimal amount, TransactionType type, String category, LocalDateTime date, BankAccount sourceAccount, BankAccount targetAccount) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Поле ID не может быть пустым");
        }
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Сумма транзакции должна быть больше нуля");
        }
        if (type == null) {
            throw new IllegalArgumentException("Выберите тип транзакции");
        }
        if (date == null) {
            throw new IllegalArgumentException("Дата не может быть null");
        }
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.date = date;
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
        validateAccounts();
    }
    private void validateAccounts() {
        if (this.targetAccount == null && type == TransactionType.DEPOSIT) {
            throw new IllegalArgumentException("Счет получатель не может быть null");
        }
        if (this.sourceAccount == null && type == TransactionType.WITHDRAWAL) {
            throw new IllegalArgumentException("Счет отправителя не может быть null");
        }
        if ((this.sourceAccount == null || this.targetAccount == null) && type == TransactionType.TRANSFER) {
            throw new IllegalArgumentException("Счета не могут быть null");
        }
        if(type == TransactionType.PAYMENT && (this.sourceAccount == null || (category == null || category.isBlank()))) {
            throw new IllegalArgumentException("Счет отправителя и категория обязательны");
        }
    }
}
