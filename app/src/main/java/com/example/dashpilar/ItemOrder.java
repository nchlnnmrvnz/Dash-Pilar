package com.example.dashpilar;

import java.util.LinkedHashMap;

public class ItemOrder {
    private String name;
    private float price;
    private int quantity;
    private LinkedHashMap<String, Float> addOns = new LinkedHashMap<>();

    public ItemOrder (String name, float price, int quantity, LinkedHashMap<String, Float> addOns) {
        this.setName(name);
        this.setPrice(price);
        this.setQuantity(quantity);
        this.setAddOns(addOns);
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    private void setPrice(float price) {
        this.price = price;
    }

    public LinkedHashMap<String, Float> getAddOns() {
        return addOns;
    }

    private void setAddOns(LinkedHashMap<String, Float> addOns) {
        this.addOns = addOns;
    }

    public int getQuantity() {
        return quantity;
    }

    private void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
