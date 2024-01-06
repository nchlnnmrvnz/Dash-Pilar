package com.example.dashpilar;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ItemOrder extends Item {

    private String getChosenDrink;
    private int quantity;
    private int sugarLevel;
    private LinkedHashMap<String, Float> checkedAddOns = new LinkedHashMap<>();

    public ItemOrder (String name, float price, String description, int imageResource, boolean sugarLevelSelectable,
                      ArrayList<AddOn> addOns, ArrayList<Item> drinkChoices,
                      LinkedHashMap<String, Float> checkedAddOns, int quantity, int sugarLevel) {
        super(name, price, description, imageResource, sugarLevelSelectable, addOns, drinkChoices);
        this.setQuantity(quantity);
        this.setSugarLevel(sugarLevel);
        this.setCheckedAddOns(checkedAddOns);
        this.setGetChosenDrink(null);
    }

    public String getChosenDrink() {
        return getChosenDrink;
    }

    public void setGetChosenDrink(String getChosenDrink) {
        this.getChosenDrink = getChosenDrink;
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

        for(Map.Entry<String, Float> addOn : this.getCheckedAddOns().entrySet()) {
            price += addOn.getValue();
        }
        price *= this.getQuantity();

        return price;
    }

    public LinkedHashMap<String, Float> getCheckedAddOns() {
        return checkedAddOns;
    }

    public String getCheckedAddOnString() {
        StringBuilder str = new StringBuilder();

        if (isSugarLevelSelectable()) {
            for(Map.Entry<String, Float> addOn : checkedAddOns.entrySet()) {
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
        }
        return str.toString();
    }

    public void setCheckedAddOns(LinkedHashMap<String, Float> checkedAddOns) {
        this.checkedAddOns = checkedAddOns;
    }
}