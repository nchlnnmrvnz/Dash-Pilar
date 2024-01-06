package com.example.dashpilar;

public class AddOn {
    private String name;
    private float price;
    private boolean available;

    AddOn(String name, float price) {
        this.name = name;
        this.price = price;
        this.available = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
