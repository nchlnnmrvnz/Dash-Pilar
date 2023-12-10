package com.example.dashpilar;

import java.util.LinkedHashMap;
import java.util.Map;

public class ItemOrder extends Item {
    private int quantity;

    public ItemOrder (String name, float price, String description, int imageResource, LinkedHashMap<String, Float> addOns, int quantity) {
        super(name, price, description, imageResource, addOns);
        this.setQuantity(quantity);
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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