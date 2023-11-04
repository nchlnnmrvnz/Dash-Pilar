package com.example.dashpilar;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Constants {
    public static ArrayList<Item> coffeeCollection = new ArrayList<>();
    public static ArrayList<Item> milkteaCollection = new ArrayList<>();
    public static ArrayList<Item> dessertCollection = new ArrayList<>();
    public static ArrayList<Item> frappeCollection = new ArrayList<>();
    public static ArrayList<Item> hotDrinksCollection = new ArrayList<>();
    public static ArrayList<Item> allItemsCollection = new ArrayList<>();
    public static LinkedHashMap<String, Float> drinkAddOnsCollection = new LinkedHashMap<>();

    static {
        drinkAddOnsCollection.put("Pearls", 9.00f);
        drinkAddOnsCollection.put("Salty Cream", 9.00f);
        drinkAddOnsCollection.put("Crushed Oreo", 9.00f);
        drinkAddOnsCollection.put("Coffee Shot", 9.00f);

        coffeeCollection.add(new Item("Dash Latte Coffee", 39.00f, drinkAddOnsCollection));
        coffeeCollection.add(new Item("Cloud Latte Coffee", 39.00f, drinkAddOnsCollection));
        coffeeCollection.add(new Item("Cloud Seasalt Coffee", 39.00f, drinkAddOnsCollection));
        coffeeCollection.add(new Item("Spanish Latte Coffee", 39.00f, drinkAddOnsCollection));
        coffeeCollection.add(new Item("Macchiato Coffee", 39.00f, drinkAddOnsCollection));
        coffeeCollection.add(new Item("Sweet Americano Coffee", 39.00f, drinkAddOnsCollection));
        coffeeCollection.add(new Item("Dark Mocha Coffee", 39.00f, drinkAddOnsCollection));

        milkteaCollection.add(new Item("Tokyo Milktea", 39.00f, drinkAddOnsCollection));
        milkteaCollection.add(new Item("Sapporo Milktea", 39.00f, drinkAddOnsCollection));
        milkteaCollection.add(new Item("Hokkaido Milktea", 39.00f, drinkAddOnsCollection));
        milkteaCollection.add(new Item("Okinawa Milktea", 39.00f, drinkAddOnsCollection));
        milkteaCollection.add(new Item("Nagoya Milktea", 39.00f, drinkAddOnsCollection));
        milkteaCollection.add(new Item("Kyoto Milktea", 39.00f, drinkAddOnsCollection));
        milkteaCollection.add(new Item("Osaka Milktea", 39.00f, drinkAddOnsCollection));

        dessertCollection.add(new Item("Forrest Cake Dessert", 49.00f, drinkAddOnsCollection));
        dessertCollection.add(new Item("Matcha Cream Dessert", 49.00f, drinkAddOnsCollection));
        dessertCollection.add(new Item("Choco Nutty Dessert", 49.00f, drinkAddOnsCollection));
        dessertCollection.add(new Item("Mango Dream Dessert", 49.00f, drinkAddOnsCollection));
        dessertCollection.add(new Item("Dark Chocolate Dessert", 49.00f, drinkAddOnsCollection));
        dessertCollection.add(new Item("Velvet Cake Dessert", 49.00f, drinkAddOnsCollection));

        frappeCollection.add(new Item("Dark Forrest Frappe", 59.00f, drinkAddOnsCollection));
        frappeCollection.add(new Item("Taro Cream Frappe", 59.00f, drinkAddOnsCollection));
        frappeCollection.add(new Item("Red Chocolate Frappe", 59.00f, drinkAddOnsCollection));
        frappeCollection.add(new Item("Vanilla Bean Frappe", 59.00f, drinkAddOnsCollection));
        frappeCollection.add(new Item("Oreo Cream Frappe", 59.00f, drinkAddOnsCollection));
        frappeCollection.add(new Item("Nutty Choco Frappe", 59.00f, drinkAddOnsCollection));

        hotDrinksCollection.add(new Item("Hot Dash Latte", 49.00f, drinkAddOnsCollection));
        hotDrinksCollection.add(new Item("Hot Spanish Latte", 49.00f, drinkAddOnsCollection));
        hotDrinksCollection.add(new Item("Hot Caramel Macchiato", 49.00f, drinkAddOnsCollection));
        hotDrinksCollection.add(new Item("Hot Matcha Latte", 49.00f, drinkAddOnsCollection));
        hotDrinksCollection.add(new Item("Hot Dark Chocolate", 49.00f, drinkAddOnsCollection));
        hotDrinksCollection.add(new Item("Hot Hazelnut", 49.00f, drinkAddOnsCollection));

        allItemsCollection.addAll(coffeeCollection);
        allItemsCollection.addAll(milkteaCollection);
        allItemsCollection.addAll(dessertCollection);
        allItemsCollection.addAll(frappeCollection);
        allItemsCollection.addAll(hotDrinksCollection);
    }
}
