package com.app.file_operator.impl;

import com.app.model.client.Client;
import com.app.model.product.Product;

import java.util.Map;

public class MapComparator {
    public static boolean compareMaps(Map<Client, Map<Product, Integer>> map1, Map<Client, Map<Product, Integer>> map2) {
        if (map1.size() != map2.size()) {
            return false;
        }
        for (var entry : map1.entrySet()) {
            var key = entry.getKey();
            if (!map2.containsKey(key)) {
                return false;
            }
            var value1 = entry.getValue();
            var value2 = map2.get(key);
            if (!compareInnerMap(value1, value2)) {
                return false;
            }
        }
        return true;
    }

    private static boolean compareInnerMap(Map<Product, Integer> map1, Map<Product, Integer> map2) {
        if (map1.size() != map2.size()) {
            return false;
        }
        for (var entry : map1.entrySet()) {
            var key = entry.getKey();
            if (!map2.containsKey(key)) {
                return false;
            }
        }
        return true;
    }
}
