package org.example;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @Getter
    @EqualsAndHashCode.Include
    private String id;

    @Getter
    private String name;

    @ToString.Exclude
    private List<BankAccount> accounts = new ArrayList<>();

    public User(String id, String name) {
        if(id == null || id.isBlank()){
            throw new IllegalArgumentException("ID не может быть пустым");
        }
        if(name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }
        this.id = id;
        this.name = name;
    }

    public List<BankAccount> getAccounts(){
        return List.copyOf(accounts);
    }

    public void addAccount(BankAccount account) {
        if(account != null && !accounts.contains(account)){
            accounts.add(account);
        }
    }
}
