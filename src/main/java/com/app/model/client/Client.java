package com.app.model.client;

import java.math.BigDecimal;
import java.util.Objects;

public class Client {
    private final int id;
    private final String name;
    private final String surname;
    final int age;
    final BigDecimal money;

    public Client(int id, String name, String surname, int age, BigDecimal money) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.money = money;
    }

    public static Client parse(String line) {
        var splitted = line.split(";");
        return new Client(Integer.parseInt(splitted[0]), splitted[1], splitted[2], Integer.parseInt(splitted[3]),
                BigDecimal.valueOf(Double.parseDouble(splitted[4])));
    }

    public boolean hasTheSameId(int id) {
        return this.id == id;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                ", money=" + money +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client client = (Client) o;
        return id == client.id && age == client.age && Objects.equals(name, client.name) && Objects.equals(surname, client.surname) && Objects.equals(money, client.money);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(surname);
        result = 31 * result + age;
        result = 31 * result + Objects.hashCode(money);
        return result;
    }
}
