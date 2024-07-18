package com.app.model.client;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Objects;

@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class Client {
    private final int id;
    private final String name;
    private final String surname;
    final int age;
    final BigDecimal money;

    public static Client parse(String line) {
        var splitted = line.split(";");
        return new Client(Integer.parseInt(splitted[0]), splitted[1], splitted[2],
                Integer.parseInt(splitted[3]), new BigDecimal(splitted[4]));
    }

    public boolean hasTheSameId(int id) {
        return this.id == id;
    }

}
