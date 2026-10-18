package org.example;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id;
    private String name;
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

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }
}
