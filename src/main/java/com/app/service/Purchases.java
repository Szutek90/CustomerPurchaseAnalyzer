package com.app.service;

import com.app.file_operator.impl.PurchasesFileOperator;
import com.app.model.client.Client;
import com.app.model.client.ClientMapper;
import com.app.model.product.Product;
import com.app.model.product.ProductMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Purchases {
    private final Map<Client, Map<Product, Integer>> purchaseHistory;
    private final PurchasesFileOperator fileOperator;

    public Purchases(List<String> fileNames) {
        fileOperator = new PurchasesFileOperator();
        purchaseHistory = loadHistory(fileNames);
    }

    public static Map<Client, Map<Product, Integer>> parse(String line) {
        var splitted = line.split("\\[");
        var splittedProducts = splitted[1].replace("]", "").split(" ");
        var client = Client.parse(splitted[0]);
        var products = new ArrayList<Product>();
        for (var product : splittedProducts) {
            products.add(Product.parse(product));
        }
        var clientProducts = new HashMap<>(Map.of(client, products));
        return clientProducts.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue()
                        .stream()
                        .collect(Collectors.toMap(
                                Function.identity(),
                                product -> 1,
                                Integer::sum
                        ))));
    }

    public List<Client> getTopClientsByCategory(String category) {
        return countClientPurchasesByCategory(category)
                .entrySet()
                .stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue,
                        Collectors.mapping(Map.Entry::getKey, Collectors.toList())))
                .entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getKey))
                .map(Map.Entry::getValue)
                .orElseThrow(IllegalStateException::new);
    }

    public Map<Client, Integer> countClientPurchasesByCategory(String category) {
        return purchaseHistory.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue()
                        .entrySet()
                        .stream()
                        .filter(product -> product.getKey().hasTheSameCategory(category))
                        .mapToInt(Map.Entry::getValue)
                        .sum()
                ));
    }

    public Map<Client, BigDecimal> getClientBasketSummaryById(int id) {
        var sum = countBasketByClient(id);
        return purchaseHistory.entrySet().stream()
                .filter(e -> e.getKey().hasTheSameId(id))
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> ClientMapper.toMoney.apply(entry.getKey())
                                .subtract(sum)));
    }

    public Map<String, BigDecimal> getAveragePriceByCategory() {
        return getSortedByCategory().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> {
                            var products = entry.getValue();
                            if (products.isEmpty()) {
                                return BigDecimal.ZERO;
                            }
                            var sum = products.stream()
                                    .map(ProductMapper.toBigDecimal)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                            return sum.divide(new BigDecimal(products.size()),
                                    RoundingMode.HALF_UP);
                        }));
    }

    public Map<String, List<Product>> getMostExpensiveProductByCategory() {
        return getSortedByCategory().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> {
                            var mostExpensiveProduct = entry.getValue().stream()
                                    .map(ProductMapper.toBigDecimal)
                                    .max(BigDecimal::compareTo)
                                    .orElseThrow(IllegalArgumentException::new);
                            return entry.getValue().stream()
                                    .filter(e -> e.hasTheSamePrice(mostExpensiveProduct))
                                    .toList();
                        }));
    }

    public Map<String, List<Product>> getCheapestProductByCategory() {
        return getSortedByCategory().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            var lowestPrice = entry.getValue()
                                    .stream()
                                    .map(ProductMapper.toBigDecimal)
                                    .min(Comparator.naturalOrder())
                                    .orElseThrow(IllegalArgumentException::new);
                            return entry.getValue().stream()
                                    .filter(e -> e.hasTheSamePrice(lowestPrice))
                                    .toList();

                        }
                ));
    }

    public Map<String, List<Product>> getSortedByCategory() {
        return purchaseHistory.entrySet().stream()
                .flatMap(entry -> entry.getValue().entrySet().stream())
                .collect(Collectors.groupingBy(entry -> ProductMapper.toCategory.apply(entry.getKey()),
                        Collectors.mapping(
                                Map.Entry::getKey,
                                Collectors.toList()
                        )));
    }

    public Map<Integer, List<String>> getMostCommonCategoryByAge() {
        return purchaseHistory.entrySet()
                .stream()
                .collect(Collectors.groupingBy(e -> ClientMapper.toAge.apply(e.getKey()),
                        Collectors.flatMapping(e -> e.getValue().keySet()
                                        .stream()
                                        .map(ProductMapper.toCategory),
                                Collectors.toList()
                        )))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public Client getClientPaidMostByCategory(String category) {
        return purchaseHistory.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(),
                        calculateTotalSpentByCategory(entry.getValue(), category)))
                .max(Comparator.comparing(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElseThrow(IllegalStateException::new);
    }

    public Client getClientPaidMost() {
        return purchaseHistory.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), calculateTotalSpent(entry.getValue())))
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow(IllegalStateException::new);
    }

    private BigDecimal calculateTotalSpentByCategory(Map<Product, Integer> products, String category) {
        return products.entrySet().stream()
                .filter(e -> e.getKey().hasTheSameCategory(category))
                .map(entry -> ProductMapper.toBigDecimal.apply(entry.getKey())
                        .multiply(BigDecimal.valueOf(entry.getValue())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateTotalSpent(Map<Product, Integer> products) {
        return products.entrySet().stream()
                .map(entry -> ProductMapper.toBigDecimal.apply(entry.getKey())
                        .multiply(BigDecimal.valueOf(entry.getValue())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Map<Client, Map<Product, Integer>> loadHistory(List<String> fileNames) {
        var purchases = new HashMap<Client, Map<Product, Integer>>();
        for (var fileName : fileNames) {
            var purchasesFromFile = fileOperator.readFile(fileName);
            for (var entry : purchasesFromFile.entrySet()) {
                purchases.merge(entry.getKey(), entry.getValue(), (existingMap, newMap) -> {
                    newMap.forEach((product, quantity) ->
                            existingMap.merge(product, quantity, Integer::sum)
                    );
                    return existingMap;
                });
            }
        }
        return purchases;
    }

    private BigDecimal countBasketByClient(int id) {
        return purchaseHistory.entrySet().stream()
                .filter(entry -> entry.getKey().hasTheSameId(id))
                .flatMap(entry -> entry.getValue().entrySet().stream())
                .map(e -> ProductMapper.toBigDecimal.apply(e.getKey())
                        .multiply(BigDecimal.valueOf(e.getValue())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}


/*
Przygotuj klasę Zakupy, która posiada pole składowe - mapę.
Kluczem mapy jest obiekt klasy Klient, natomiast wartością mapa, która
jako klucz posiada obiekt klasy Produkt, natomiast wartością jest
liczba całkowita, określająca, ile razy klient zakupił ten produkt.
Przygotuj konstruktor, który jako argument pobiera nazwy plików
tekstowych i wypełnia mapę danymi z plików tekstowych. Następnie napisz
metody, które wykorzystując operacje na kolekcjach oraz mapach
(programiści Java mogą stosować strumienie Java 8) rozwiążą następujące
problemy:
 */