package com.example.dashpilar;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Item {
    private String name;
    private float price;
    private String description;
    private int imageResource;
    private boolean sugarLevelSelectable;
    private LinkedHashMap<String, Float> addOns = new LinkedHashMap<>();
    private ArrayList<Item> drinkChoices = new ArrayList<>();

    public Item (String name, float price, String description, int imageResource, boolean sugarLevelSelectable, LinkedHashMap<String, Float> addOns, ArrayList<Item> drinkChoices) {
        this.setName(name);
        this.setPrice(price);
        this.setDescription(description);
        this.setImageResource(imageResource);
        this.setSugarLevelSelectable(sugarLevelSelectable);
        this.setAddOns(addOns);
        this.setDrinkChoices(drinkChoices);
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    public LinkedHashMap<String, Float> getAddOns() {
        return addOns;
    }

    void setAddOns(LinkedHashMap<String, Float> addOns) {
        this.addOns = addOns;
    }

    public int getImageResource() {
        return imageResource;
    }

    private void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public ArrayList<Item> getDrinkChoices() {
        return drinkChoices;
    }

    private void setDrinkChoices(ArrayList<Item> drinkChoices) {
        this.drinkChoices = drinkChoices;
    }

    public boolean isSugarLevelSelectable() {
        return sugarLevelSelectable;
    }

    public void setSugarLevelSelectable(boolean sugarLevelSelectable) {
        this.sugarLevelSelectable = sugarLevelSelectable;
    }
}