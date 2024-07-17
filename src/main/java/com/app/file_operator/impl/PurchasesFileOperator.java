package com.app.file_operator.impl;

import com.app.file_operator.FileOperator;
import com.app.model.client.Client;
import com.app.model.product.Product;
import com.app.service.Purchases;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

public class PurchasesFileOperator implements FileOperator<Client, Product, Integer> {

    @Override
    public Map<Client, Map<Product, Integer>> readFile(String filename) {
        try (var lines = Files.lines(Paths.get(filename))) {
            return  lines
                    .map(Purchases::parse)
                    .flatMap(map -> map.entrySet().stream())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (existing, replacement) -> {
                                replacement.forEach((product, quantity) ->
                                        existing.merge(product, quantity, Integer::sum));
                                return existing;
                            }
                    ));

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
