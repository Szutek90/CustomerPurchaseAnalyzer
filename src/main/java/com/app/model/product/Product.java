package com.app.model.product;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class Product {
    private final int id;
    private final String name;
    final String category;
    final BigDecimal price;

    public boolean hasTheSameCategory(String category) {
        return this.category.equals(category);
    }

    public static Product parse(String line){
        var splitted = line.split(";");
        return new Product(Integer.parseInt(splitted[0]), splitted[1], splitted[2],
                new BigDecimal(splitted[3]));
    }

    public boolean hasTheSamePrice(BigDecimal price) {
        return this.price.equals(price);
    }
}
