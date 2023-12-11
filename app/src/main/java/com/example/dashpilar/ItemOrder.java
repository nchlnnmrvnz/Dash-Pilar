package com.example.dashpilar;

import java.util.LinkedHashMap;
import java.util.Map;

public class ItemOrder extends Item {
    private int quantity;
    private int sugarLevel;

    public ItemOrder (String name, float price, String description, int imageResource, LinkedHashMap<String, Float> addOns, int quantity, int sugarLevel) {
        super(name, price, description, imageResource, addOns);
        this.setQuantity(quantity);
        this.setSugarLevel(sugarLevel);
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSugarLevel() {
        return sugarLevel;
    }

    public void setSugarLevel(int sugarLevel) {
        this.sugarLevel = sugarLevel;
    }

    public float calculatePrice() {
        float price = this.getPrice();

        for(Map.Entry<String, Float> addOn : this.getAddOns().entrySet()) {
            price += addOn.getValue();
        }
        price *= this.getQuantity();

        return price;
    }
}