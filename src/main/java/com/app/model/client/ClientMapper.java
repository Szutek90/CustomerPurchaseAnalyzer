package com.app.model.client;

import java.math.BigDecimal;
import java.util.function.Function;

public interface ClientMapper {
    Function<Client, Integer> toAge = client -> client.age;
    Function<Client, BigDecimal> toMoney = client -> client.money;
}
