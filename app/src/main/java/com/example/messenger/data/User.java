package com.example.messenger.data;

public class User {
    private String id;
    private String name;
    private String lastName;
    private int age;
    private boolean online;

    public User(String id, String name, String lastName, int age, boolean online) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.online = online;
    }

    // при работе с Firebase для получения данных необходимо использовать пустой конструктор
    public User() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public boolean isOnline() {
        return online;
    }
}
