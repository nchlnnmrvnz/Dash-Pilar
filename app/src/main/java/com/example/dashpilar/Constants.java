package com.example.dashpilar;

import java.util.LinkedHashMap;
import java.util.Map;

public class Constants {
    public static LinkedHashMap<String, Float> coffeeCollection = new LinkedHashMap<>();
    public static LinkedHashMap<String, Float> milkteaCollection = new LinkedHashMap<>();
    public static LinkedHashMap<String, Float> dessertCollection = new LinkedHashMap<>();
    public static LinkedHashMap<String, Float> frappeCollection = new LinkedHashMap<>();
    public static LinkedHashMap<String, Float> hotDrinksCollection = new LinkedHashMap<>();
    public static LinkedHashMap<String, Float> allItemsCollection = new LinkedHashMap<>();

    static {
        coffeeCollection.put("Dash Latte Coffee", 39.00f);
        coffeeCollection.put("Cloud Latte Coffee", 39.00f);
        coffeeCollection.put("Cloud Seasalt Coffee", 39.00f);
        coffeeCollection.put("Spanish Latte Coffee", 39.00f);
        coffeeCollection.put("Macchiato Coffee", 39.00f);
        coffeeCollection.put("Sweet Americano Coffee", 39.00f);
        coffeeCollection.put("Dark Mocha Coffee", 39.00f);

        milkteaCollection.put("Tokyo Milktea", 39.00f);
        milkteaCollection.put("Sapporo Milktea", 39.00f);
        milkteaCollection.put("Hokkaido Milktea", 39.00f);
        milkteaCollection.put("Okinawa Milktea", 39.00f);
        milkteaCollection.put("Nagoya Milktea", 39.00f);
        milkteaCollection.put("Kyoto Milktea", 39.00f);
        milkteaCollection.put("Osaka Milktea", 39.00f);

        dessertCollection.put("Forrest Cake Dessert", 49.00f);
        dessertCollection.put("Matcha Cream Dessert", 49.00f);
        dessertCollection.put("Choco Nutty Dessert", 49.00f);
        dessertCollection.put("Mango Dream Dessert", 49.00f);
        dessertCollection.put("Dark Chocolate Dessert", 49.00f);
        dessertCollection.put("Velvet Cake Dessert", 49.00f);

        frappeCollection.put("Dark Forrest Frappe", 59.00f);
        frappeCollection.put("Taro Cream Frappe", 59.00f);
        frappeCollection.put("Red Chocolate Frappe", 59.00f);
        frappeCollection.put("Vanilla Bean Frappe", 59.00f);
        frappeCollection.put("Oreo Cream Frappe", 59.00f);
        frappeCollection.put("Nutty Choco Frappe", 59.00f);


        hotDrinksCollection.put("Hot Dash Latte", 49.00f);
        hotDrinksCollection.put("Hot Spanish Latte", 49.00f);
        hotDrinksCollection.put("Hot Caramel Macchiato", 49.00f);
        hotDrinksCollection.put("Hot Matcha Latte", 49.00f);
        hotDrinksCollection.put("Hot Dark Chocolate", 49.00f);
        hotDrinksCollection.put("Hot Hazelnut", 49.00f);


        allItemsCollection.putAll(coffeeCollection);
        allItemsCollection.putAll(milkteaCollection);
        allItemsCollection.putAll(dessertCollection);
        allItemsCollection.putAll(frappeCollection);
        allItemsCollection.putAll(hotDrinksCollection);
    }
}
