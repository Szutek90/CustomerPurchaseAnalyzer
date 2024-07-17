package com.app.model.product;

import java.math.BigDecimal;
import java.util.function.Function;

public interface ProductMapper {
    Function<Product, BigDecimal> toBigDecimal = product -> product.price;
    Function<Product, String> toCategory = product -> product.category;
}
