package com.example.dashpilar;

import java.util.LinkedHashMap;
import java.util.Map;

public class Item {
    private String name;
    private float price;
    private String description;
    private int imageResource;
    private LinkedHashMap<String, Float> addOns = new LinkedHashMap<>();

    public Item (String name, float price, String description, int imageResource, LinkedHashMap<String, Float> addOns) {
        this.setName(name);
        this.setPrice(price);
        this.setDescription(description);
        this.setImageResource(imageResource);
        this.setAddOns(addOns);
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

    public String getAddOnString() {
        StringBuilder str = new StringBuilder();

        for(Map.Entry<String, Float> addOn : addOns.entrySet()) {
            String addOnStr;
            switch(addOn.getKey()) {
                case "Pearls": addOnStr = "P"; break;
                case "Salty Cream": addOnStr = "SC"; break;
                case "Crushed Oreo": addOnStr = "CO"; break;
                case "Coffee Shot": addOnStr = "CS"; break;
                default: addOnStr = "";
            }
            str.append(addOnStr).append(", ");
        }

        if (str.length() >= 2)
            str = new StringBuilder(str.substring(0, str.length() - 2));
        else if(str.toString().equals(""))
            str = new StringBuilder("No add ons");

        return str.toString();
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
}