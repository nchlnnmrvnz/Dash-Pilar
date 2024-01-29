package com.example.dashpilar;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FirestoreSalesWriter {
    private final FirebaseFirestore db;

    public FirestoreSalesWriter() {
        db = FirebaseFirestore.getInstance();
    }

    public void writeToSalesCollection(String orderNumber, String paymentMethod, String serviceMode,
                                       String collection, ArrayList<ItemOrder> cart) {
        // Get current date and time
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        // Create a new sales document
        Map<String, Object> order = new HashMap<>();
        order.put("orderNumber", orderNumber);
        order.put("date", dateFormat.format(Calendar.getInstance().getTime()));
        order.put("time", timeFormat.format(Calendar.getInstance().getTime()));

        ArrayList<Object> itemNames = new ArrayList<>();
        for (ItemOrder item : cart) {
            if (item.getQuantity() > 0) {
                Map<String, Object> itemAttributes = new HashMap<>();
                itemAttributes.put("itemName", item.getName());

                Map<String, Object> addOns = new HashMap<>();
                if (item.getCheckedAddOns() != null) {
                    for(AddOn addOn : item.getCheckedAddOns())
                        addOns.put(addOn.getName(), addOn.getPrice());
                }
                itemAttributes.put("addOns", addOns);

                itemAttributes.put("basePrice", item.getPrice());

                if (item.getChosenDrink() != null)
                    itemAttributes.put("flavor", item.getChosenDrink());

                itemAttributes.put("itemSubTotal", item.calculatePrice());
                itemAttributes.put("quantity", item.getQuantity());

                if (item.getSugarLevel() != -1)
                    itemAttributes.put("sugarLevel", item.getSugarLevel());

                itemNames.add(itemAttributes);
            }
        }
        order.put("orderedItems", itemNames);
        order.put("paymentMethod", paymentMethod);
        order.put("serviceMode", serviceMode);
        order.put("totalAmount", getTotalAmount(cart));
        order.put("totalItems", getTotalItems(cart));

        db.collection(collection).document(orderNumber).set(order);
    }

    float getTotalAmount(ArrayList<ItemOrder> cart) {
        float total = 0.00f;

        for (int i = 0; i < cart.size(); i++){
            total += cart.get(i).calculatePrice();
        }

        return total;
    }

    int getTotalItems(ArrayList<ItemOrder> cart) {
        int numberOfOrders = 0;

        for (ItemOrder order : cart) {
            numberOfOrders += order.getQuantity();
        }

        return numberOfOrders;
    }
}