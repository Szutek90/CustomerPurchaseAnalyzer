package com.app.service;

import com.app.model.client.Client;
import com.app.model.product.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class PurchasesTest {

    static Purchases purchases;
    static Client client1;
    static Client client2;
    static Client client3;

    @BeforeAll
    static void beforeAll() {
        var files = List.of("src/test/resources/testPurchases1.csv",
                "src/test/resources/testPurchases2.csv");
        purchases = new Purchases(files);
        client1 = new Client(1, "Adam", "Nowak", 25, BigDecimal.valueOf(2500));
        client2 = new Client(2, "Marta", "Witkowska", 27, BigDecimal.valueOf(2700));
        client3 = new Client(3, "Oliwia", "Kaczmarczyk", 28, BigDecimal.valueOf(2800));
    }

    @Test
    @DisplayName("When getting top clients by cattegory")
    void test1() {
        var readed = purchases.getTopClientsByCategory("Książka");
        var expected = List.of(client1);

        assertThat(readed).isEqualTo(expected);
    }

    @Test
    @DisplayName("When counting client purchases by category")
    void test2() {
        var readed = purchases.countClientPurchasesByCategory("Książka");
        var expected = Map.of(
                client1, 2,
                client3, 1,
                client2, 0);

        assertThat(readed).containsExactlyInAnyOrderEntriesOf(expected);
    }

    @Test
    @DisplayName("When getting client basket by id")
    void test3() {
        var readed = purchases.getClientBasketSummaryById(2);
        var expected = Map.of(client2, BigDecimal.valueOf(2170));

        assertThat(readed).isEqualTo(expected);
    }

    @Test
    @DisplayName("When getting average price by category")
    void test4() {
        var readed = purchases.getAveragePriceByCategory();
        var expected = Map.of(
                "Książka", BigDecimal.valueOf(165),
                "Elektronika", BigDecimal.valueOf(1200),
                "Rosliny", BigDecimal.valueOf(30),
                "Technologia", BigDecimal.valueOf(2400),
                "Wyposazenie", BigDecimal.valueOf(500),
                "Owoce", BigDecimal.valueOf(8));

        assertThat(readed).containsExactlyInAnyOrderEntriesOf(expected);
    }

    @Test
    @DisplayName("When getting most expensive product by category")
    void test5() {
        var readed = purchases.getMostExpensiveProductByCategory();
        var expected = Map.of(
                "Książka",
                List.of(new Product(6, "ZłoWZarządzie", "Książka", BigDecimal.valueOf(180))),
                "Elektronika",
                List.of(new Product(1, "Telefon", "Elektronika", BigDecimal.valueOf(1200))),
                "Rosliny",
                List.of(new Product(5, "Kwiaty", "Rosliny", BigDecimal.valueOf(30))),
                "Technologia",
                List.of(new Product(7, "Informatyka", "Technologia", BigDecimal.valueOf(2400))),
                "Wyposazenie",
                List.of(new Product(4, "Dywan", "Wyposazenie", BigDecimal.valueOf(500))),
                "Owoce",
                List.of(new Product(3, "Mandarynki", "Owoce", BigDecimal.valueOf(8))));

        assertThat(readed).containsExactlyInAnyOrderEntriesOf(expected);
    }

    @Test
    @DisplayName("When getting cheapest product by category")
    void test6() {
        var readed = purchases.getCheapestProductByCategory();
        var expected = Map.of(
                "Książka",
                List.of(new Product(2, "DumaIUprzedzenie", "Książka", BigDecimal.valueOf(150))),
                "Elektronika",
                List.of(new Product(1, "Telefon", "Elektronika", BigDecimal.valueOf(1200))),
                "Rosliny",
                List.of(new Product(5, "Kwiaty", "Rosliny", BigDecimal.valueOf(30))),
                "Technologia",
                List.of(new Product(7, "Informatyka", "Technologia", BigDecimal.valueOf(2400))),
                "Wyposazenie",
                List.of(new Product(4, "Dywan", "Wyposazenie", BigDecimal.valueOf(500))),
                "Owoce",
                List.of(new Product(3, "Mandarynki", "Owoce", BigDecimal.valueOf(8))));

        assertThat(readed).containsExactlyInAnyOrderEntriesOf(expected);
    }


    @Test
    @DisplayName("When getting sorted products by category")
    void test7() {
        var readed = purchases.getSortedByCategory();
        var expected = Map.of(
                "Książka",
                List.of(new Product(2, "DumaIUprzedzenie", "Książka", BigDecimal.valueOf(150)),
                        new Product(6, "ZłoWZarządzie", "Książka", BigDecimal.valueOf(180))),
                "Elektronika",
                List.of(new Product(1, "Telefon", "Elektronika", BigDecimal.valueOf(1200))),
                "Rosliny",
                List.of(new Product(5, "Kwiaty", "Rosliny", BigDecimal.valueOf(30))),
                "Technologia",
                List.of(new Product(7, "Informatyka", "Technologia", BigDecimal.valueOf(2400))),
                "Wyposazenie",
                List.of(new Product(4, "Dywan", "Wyposazenie", BigDecimal.valueOf(500))),
                "Owoce",
                List.of(new Product(3, "Mandarynki", "Owoce", BigDecimal.valueOf(8))));

        assertThat(readed).containsExactlyInAnyOrderEntriesOf(expected);
    }

    @Test
    @DisplayName("When getting most common category by age")
    void test8() {
        var readed = purchases.getMostCommonCategoryByAge();
        var expected = Map.of(
                25, List.of("Elektronika", "Książka", "Owoce"),
                27, List.of("Wyposazenie", "Rosliny"),
                28, List.of("Technologia", "Książka")
        );
        assertThat(readed).hasSameSizeAs(expected);

        expected.forEach((key, value) -> {
            assertThat(readed).containsKey(key);
            assertThat(value).containsExactlyInAnyOrderElementsOf(readed.get(key));
        });
    }

    @Test
    @DisplayName("When getting client paid most by category")
    void test9() {
        var readed = purchases.getClientPaidMostByCategory("Technologia");
        assertThat(readed).isEqualTo(client3);
    }

    @Test
    @DisplayName("When getting client paid most")
    void test10() {
        var readed = purchases.getClientPaidMost();
        assertThat(readed).isEqualTo(client1);
    }

}
