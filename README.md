# Shopping program
![Install](https://img.shields.io/badge/install-passing-green)
![Install](https://img.shields.io/badge/coverage-89%25-light%20green)

The program is used to manage customer purchases based on data stored in text files. It consists of three main classes: Customer, Product and Purchases, which work together to analyze and report purchasing information. Analyzes customer purchase data, calculates statistics and reports on customer spending, purchasing preferences by age, average product prices and customers' ability to pay for purchases. These results are presented in the form of maps, which facilitates their interpretation and further analysis.

## Technologies and libraries used

* Java
* Maven
* Lombok
* JUnit
* AssertJ

## Jacoco Coverage

![App Screenshot](src/test/resources/jacoco_raport.PNG)

## Required files

* Purchases file with pattern ( csv ):

  clientId;name;surname;age;money[productId;name;category;price productId;name;category;price]

## Description

### Customer and Product classes:
* The customer stores personal data, age and amount of cash.
* The product includes name, category and price.

### File Operator:

* Text files contain information about customers and the products they purchased. Each line in the file contains customer data and a list of purchased products.

### Purchases Class:
* Manages purchase data by creating a map where the key is the customer and the value is a map that connects products to the number of times a customer purchases them.
* The class constructor reads data from text files and organizes them into appropriate structures.

### Functionalities:
* Spend Analysis: Determine the customer who paid the most for all purchases and for purchases in a specific category.
* Listings:
  * Age of customers and most frequently purchased product categories of this age.
  * Average product prices across categories and identify the most expensive and cheapest products in each category.
  * Customers who most often bought products in a given category.
* Ability to Pay Assessment