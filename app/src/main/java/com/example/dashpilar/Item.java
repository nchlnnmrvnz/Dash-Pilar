package com.example.dashpilar;

import java.util.LinkedHashMap;

public class Item {
    private String name;
    private float price;
    private int imageResource;
    private LinkedHashMap<String, Float> addOns = new LinkedHashMap<>();

    public Item (String name, float price, int imageResource, LinkedHashMap<String, Float> addOns) {
        this.setName(name);
        this.setPrice(price);
        this.setImageResource(imageResource);
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

    public int getImageResource() {
        return imageResource;
    }

    private void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}