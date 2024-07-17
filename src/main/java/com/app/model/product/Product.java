package com.app.model.product;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private final int id;
    private final String name;
    final String category;
    final BigDecimal price;

    public Product(int id, String name, String category, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }

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

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;
        return id == product.id && Objects.equals(name, product.name) && Objects.equals(category, product.category) && Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(category);
        result = 31 * result + Objects.hashCode(price);
        return result;
    }
}
