package com.app.file_operator.impl;

import com.app.model.client.Client;
import com.app.model.product.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

class PurchasesFileOperatorTest {
    @Test
    @DisplayName("When reading file")
    void test() {
        var operator = new PurchasesFileOperator();
        var fileName = "src/test/resources/testPurchases1.csv";

        var client1 = new Client(1, "Adam", "Nowak",25, BigDecimal.valueOf(2500));
        var client2 = new Client(2, "Marta", "Witkowska",27, BigDecimal.valueOf(2700));

        var product1 = new Product(1,"Telefon","Elektronika", BigDecimal.valueOf(1200));
        var product2 = new Product(2,"DumaIUprzedzenie","Książka", BigDecimal.valueOf(150));
        var product3 = new Product(3,"Mandarynki","Owoce", BigDecimal.valueOf(8));
        var product4 = new Product(4,"Dywan","Wyposazenie", BigDecimal.valueOf(500));
        var product5 = new Product(5,"Kwiaty","Rosliny", BigDecimal.valueOf(30));
        var readed = operator.readFile(fileName);
        var expected = Map.of(
                client1,Map.of(product1,2,
                        product2,2,
                        product3,2),
                client2,Map.of( product4, 2
                ,product5, 2)
        );
        Assertions.assertThat(MapComparator.compareMaps(readed,expected)).isTrue();
    }
}


//1;Adam;Nowak;25;2500 [1;Telefon;Elektronika;1200 2;DumaIUprzedzenie;Książka;150 3;Mandarynki;Owoce;8]
//1;Adam;Nowak;25;2500 [1;Telefon;Elektronika;1200 2;DumaIUprzedzenie;Książka;150 3;Mandarynki;Owoce;8]
//2;Marta;Witkowska;27;2700 [4;Dywan;Wyposazenie;500 5;Kwiaty;Rośliny;30]
