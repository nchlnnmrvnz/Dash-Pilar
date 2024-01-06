package com.example.dashpilar;

import java.util.ArrayList;

public class Constants {
    public static final ArrayList<Item> coffeeCollection = new ArrayList<>();
    public static final ArrayList<Item> milkteaCollection = new ArrayList<>();
    public static final ArrayList<Item> dessertCollection = new ArrayList<>();
    public static final ArrayList<Item> frappeCollection = new ArrayList<>();
    public static final ArrayList<Item> hotDrinksCollection = new ArrayList<>();
    public static final ArrayList<Item> allItemsCollection = new ArrayList<>();
    public static final ArrayList<Item> crofflesCollection = new ArrayList<>();
    public static final ArrayList<Item> plainCroffleComboCollection = new ArrayList<>();
    public static final ArrayList<Item> limitedEditionCollection = new ArrayList<>();
    public static final ArrayList<AddOn> drinkAddOnsCollection = new ArrayList<>();
    public static final ArrayList<String> unavailableItems = new ArrayList<>();

    static {
        drinkAddOnsCollection.add(new AddOn("Pearls", 9.00f));
        drinkAddOnsCollection.add(new AddOn("Salty Cream", 9.00f));
        drinkAddOnsCollection.add(new AddOn("Crushed Oreo", 9.00f));
        drinkAddOnsCollection.add(new AddOn("Coffee Shot", 9.00f));

        coffeeCollection.add(new Item("Dash Latte", 39.00f,
                "A cold coffee-based signature latte drink",
                R.drawable.dash_latte, true, drinkAddOnsCollection, null));
        coffeeCollection.add(new Item("Cloud Latte", 39.00f,
                "A cold coffee-based latte drink with cloud foam",
                R.drawable.cloud_latte, true, drinkAddOnsCollection, null));
        coffeeCollection.add(new Item("Cloud Seasalt", 39.00f,
                "A cold coffee-based signature latte drink with cloud foam and salt",
                R.drawable.cloud_seasalt, true, drinkAddOnsCollection, null));
        coffeeCollection.add(new Item("Spanish Latte", 39.00f,
                "A cold coffee-based latte drink with condensed milk",
                R.drawable.spanish_latte, true, drinkAddOnsCollection, null));
        coffeeCollection.add(new Item("Macchiato", 39.00f,
                "A cold coffee-based drink with caramel syrup",
                R.drawable.macchiato_coffee, true, drinkAddOnsCollection, null));
        coffeeCollection.add(new Item("Sweet Americano", 39.00f,
                "A cold coffee-based americano drink with condensed milk",
                R.drawable.sweet_americano, true, drinkAddOnsCollection, null));
        coffeeCollection.add(new Item("Dark Mocha", 39.00f,
                "A cold coffee-based latte drink with dark mocha",
                R.drawable.dark_mocha, true, drinkAddOnsCollection, null));

        milkteaCollection.add(new Item("Tokyo Milktea", 39.00f,
                "A milktea-based roasted brown sugar drink served with pearls",
                R.drawable.tokyo, true, drinkAddOnsCollection, null));
        milkteaCollection.add(new Item("Sapporo Milktea", 39.00f,
                "A milktea-based wintermelon drink served with pearls",
                R.drawable.saporro, true, drinkAddOnsCollection, null));
        milkteaCollection.add(new Item("Hokkaido Milktea", 39.00f,
                "A milktea-based oreo drink served with pearls",
                R.drawable.hokkaido, true, drinkAddOnsCollection, null));
        milkteaCollection.add(new Item("Okinawa Milktea", 39.00f,
                "A milktea-based caramel drink served with pearls",
                R.drawable.okinawa, true, drinkAddOnsCollection, null));
        milkteaCollection.add(new Item("Nagoya Milktea", 39.00f,
                "A milktea-based chocolate drink served with pearls",
                R.drawable.nagoya, true, drinkAddOnsCollection, null));
        milkteaCollection.add(new Item("Kyoto Milktea", 39.00f,
                "A milktea-based matcha drink served with pearls",
                R.drawable.kyoto, true, drinkAddOnsCollection, null));
        milkteaCollection.add(new Item("Osaka Milktea", 39.00f,
                "A milktea-based thai drink served with pearls",
                R.drawable.osaka, true, drinkAddOnsCollection, null));

        dessertCollection.add(new Item("Forrest Cake Dessert", 49.00f,
                "A cream-based forrest dessert drink served with salty cream",
                R.drawable.forrest_cake, true, drinkAddOnsCollection, null));
        dessertCollection.add(new Item("Matcha Cream Dessert", 49.00f,
                "A cream-based matcha dessert drink served with salty cream",
                R.drawable.matcha_cream, true, drinkAddOnsCollection, null));
        dessertCollection.add(new Item("Choco Nutty Dessert", 49.00f,
                "A cream-based nutty chocolate dessert drink served with salty cream",
                R.drawable.choco_nutty, true, drinkAddOnsCollection, null));
        dessertCollection.add(new Item("Mango Dream Dessert", 49.00f,
                "A cream-based mango dessert drink served with salty cream",
                R.drawable.mango_dream, true, drinkAddOnsCollection, null));
        dessertCollection.add(new Item("Dark Chocolate Dessert", 49.00f,
                "A cream-based dark chocolate dessert drink served with salty cream",
                R.drawable.dark_chocolate, true, drinkAddOnsCollection, null));
        dessertCollection.add(new Item("Velvet Cake Dessert", 49.00f,
                "A cream-based velvet cake dessert drink served with salty cream",
                R.drawable.velvet_cake, true, drinkAddOnsCollection, null));

        frappeCollection.add(new Item("Dark Forrest Frappe", 59.00f,
                "A frappe-based dark forrest drink served with whipped cream",
                R.drawable.dark_forrest, true, drinkAddOnsCollection, null));
        frappeCollection.add(new Item("Taro Cream Frappe", 59.00f,
                "A frappe-based taro drink served with whipped cream",
                R.drawable.taro_cream, true, drinkAddOnsCollection, null));
        frappeCollection.add(new Item("Red Chocolate Frappe", 59.00f,
                "A frappe-based red chocolate drink served with whipped cream",
                R.drawable.red_chocolate, true, drinkAddOnsCollection, null));
        frappeCollection.add(new Item("Vanilla Bean Frappe", 59.00f,
                "A frappe-based vanilla bean drink served with whipped cream",
                R.drawable.vanilla_bean, true, drinkAddOnsCollection, null));
        frappeCollection.add(new Item("Oreo Cream Frappe", 59.00f,
                "A frappe-based oreo drink served with whipped cream",
                R.drawable.oreo_cream, true, drinkAddOnsCollection, null));
        frappeCollection.add(new Item("Nutty Choco Frappe", 59.00f,
                "A frappe-based nutty chocolate drink served with whipped cream",
                R.drawable.nutty_choco, true, drinkAddOnsCollection, null));

        hotDrinksCollection.add(new Item("Hot Dash Latte", 49.00f,
                "A hot coffee-based signature latte drink",
                R.drawable.dash_latte_hot, true, drinkAddOnsCollection, null));
        hotDrinksCollection.add(new Item("Hot Spanish Latte", 49.00f,
                "A hot coffee-based latte drink with condensed milk",
                R.drawable.spanish_latte_hot, true, drinkAddOnsCollection, null));
        hotDrinksCollection.add(new Item("Hot Caramel Macchiato", 49.00f,
                "A hot coffee-based drink with caramel syrup",
                R.drawable.caramel_macchiato, true, drinkAddOnsCollection, null));
        hotDrinksCollection.add(new Item("Hot Matcha Latte", 49.00f,
                "A hot coffee-based matcha drink",
                R.drawable.matcha_latte, true, drinkAddOnsCollection, null));
        hotDrinksCollection.add(new Item("Hot Dark Chocolate", 49.00f,
                "A hot coffee-based dark chocolate drink",
                R.drawable.dark_chocolate_hot, true, drinkAddOnsCollection, null));
        hotDrinksCollection.add(new Item("Hot Hazelnut", 49.00f,
                "A hot coffee-based hazelnut drink",
                R.drawable.hazelnut, true, drinkAddOnsCollection, null));

        crofflesCollection.add(new Item("Plain Croffle", 69.00f,
                "A sugar coated croffle delicacy",
                R.drawable.croffle, false, null, null));
        crofflesCollection.add(new Item("Cinnamon Croffle", 79.00f,
                "A cinnamon sprinkled croffle delicacy",
                R.drawable.croffle, false, null, null));
        crofflesCollection.add(new Item("Chocolate Croffle", 79.00f,
                "A chocolate drizzled croffle delicacy",
                R.drawable.croffle, false, null, null));
        crofflesCollection.add(new Item("Peanut Butter Croffle", 79.00f,
                "A peanut butter drizzled croffle delicacy",
                R.drawable.croffle, false, null, null));
        crofflesCollection.add(new Item("Salted Caramel Croffle", 89.00f,
                "A salted caramel drizzled croffle delicacy",
                R.drawable.croffle, false, null, null));

        plainCroffleComboCollection.add(new Item("Plain Croffle + Any Specialty Coffee", 99.00f,
                "A combination of our sugar coated croffle with a choice of any specialty coffee",
                R.drawable.specialty_coffee_plain_combo, true, drinkAddOnsCollection, coffeeCollection));
        plainCroffleComboCollection.add(new Item("Plain Croffle + Any Milktea Flavor", 99.00f,
                "A combination of our sugar coated croffle with a choice of any milktea flavor with pearls",
                R.drawable.milktea_plain_combo, true, drinkAddOnsCollection, milkteaCollection));
        plainCroffleComboCollection.add(new Item("Plain Croffle + Any Dessert Flavor", 109.00f,
                "A combination of our sugar coated croffle with a choice of any dessert with salty cream",
                R.drawable.dessert_plain_combo, true, drinkAddOnsCollection, dessertCollection));
        plainCroffleComboCollection.add(new Item("Plain Croffle + Any Blended Frappe Flavor", 119.00f,
                "A combination of our sugar coated croffle with a choice of any blended frappe flavor with whipped cream",
                R.drawable.blended_frappe_plain_combo, true, drinkAddOnsCollection, frappeCollection));
        plainCroffleComboCollection.add(new Item("Plain Croffle + Any Hot Drink", 109.00f,
                "A combination of our sugar coated croffle with a choice of any hot drink flavor",
                R.drawable.hot_drink_plain_combo, true, drinkAddOnsCollection, hotDrinksCollection));

        limitedEditionCollection.add(new Item("Strawberry Milk", 49,
                "A milk-based strawberry drink",
                R.drawable.strawberry, true, drinkAddOnsCollection, null));
        limitedEditionCollection.add(new Item("Matcha Strawberry Milk", 49,
                "A milk-based strawberry drink with matcha flavor",
                R.drawable.matcha_strawberry, true, drinkAddOnsCollection, null));

        allItemsCollection.addAll(coffeeCollection);
        allItemsCollection.addAll(milkteaCollection);
        allItemsCollection.addAll(dessertCollection);
        allItemsCollection.addAll(frappeCollection);
        allItemsCollection.addAll(hotDrinksCollection);
        allItemsCollection.addAll(limitedEditionCollection);
        allItemsCollection.addAll(crofflesCollection);
        allItemsCollection.addAll(plainCroffleComboCollection);
    }
}