package com.example.dashpilar;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import java.util.ArrayList;
import java.util.Map;


public class Cart extends AppCompatActivity {
    static ArrayList<ItemOrder> cartList = new ArrayList<>();
    Button place_order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        place_order = findViewById(R.id.placeOrder);
        place_order.setOnClickListener(v -> {
            // logic for printing
        });

        ImageView goBack = findViewById(R.id.back);
        goBack.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());

        // Find the ScrollView in your XML layout
        NestedScrollView nestedScrollView = findViewById(R.id.nestedScrollView);

        // Create a LinearLayout to hold the dynamic content
        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        itemLayout.setOrientation(LinearLayout.VERTICAL);

        float totalPrice = 0;

        for (ItemOrder order : cartList) {
            // Create a LinearLayout for each item
            LinearLayout itemRow = new LinearLayout(this);
            itemRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            itemRow.setOrientation(LinearLayout.HORIZONTAL);
            itemRow.setPadding(0,8,0,8);
            itemRow.setGravity(Gravity.CENTER);

            // Create and add ImageView for your "minus" icon
            ImageView minusIcon = new ImageView(this);
            minusIcon.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            minusIcon.setImageResource(R.drawable.minus);
            minusIcon.setPadding(8,0,8,0);
            itemRow.addView(minusIcon);

            // Create and add a TextView for the item quantity
            TextView quantityText = new TextView(this);
            quantityText.setText(String.valueOf(order.getQuantity()));
            quantityText.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            quantityText.setTextColor(getResources().getColor(R.color.black));
            quantityText.setTextSize(20);
            itemRow.addView(quantityText);

            // Create and add ImageView for your "add" icon
            ImageView addIcon = new ImageView(this);
            addIcon.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            addIcon.setImageResource(R.drawable.add);
            addIcon.setPadding(8,0,8,0);
            itemRow.addView(addIcon);

            // Create a nested LinearLayout for item details
            LinearLayout itemDetails = new LinearLayout(this);

            // Set the layout parameters with layout_weight
            LinearLayout.LayoutParams itemDetailsParams = new LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT,
                    1.0f
            );

            itemDetails.setLayoutParams(itemDetailsParams);

            // Set the orientation
            itemDetails.setOrientation(LinearLayout.VERTICAL);


            // Create and add TextViews for item name and description
            TextView itemName = new TextView(this);
            itemName.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            itemName.setPadding(8,0,0,0);
            itemName.setText(order.getName());
            itemName.setTextColor(getResources().getColor(R.color.black));
            itemName.setTextSize(20);
            itemDetails.addView(itemName);

            TextView itemAddOns = new TextView(this);
            itemAddOns.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            StringBuilder addOns = new StringBuilder();
            float addOnsPrice = 0;
            for(Map.Entry<String, Float> addOn : order.getAddOns().entrySet()) {
                addOns.append(addOn.getKey()).append(", ");
                addOnsPrice += addOn.getValue();
            }

            if (addOns.length() >= 2)
                addOns = new StringBuilder(addOns.substring(0, addOns.length() - 2));
            else if(addOns.toString().equals(""))
                addOns = new StringBuilder("No add ons");

            itemAddOns.setText(addOns.toString());
            itemAddOns.setPadding(8,0,0,0);
            itemAddOns.setTextColor(getResources().getColor(R.color.black));
            itemAddOns.setTextSize(15);
            itemDetails.addView(itemAddOns);

            itemRow.addView(itemDetails);

            // Computation
            float itemPrice = (order.getPrice() + addOnsPrice) * order.getQuantity();
            totalPrice += itemPrice;

            // Create and add a TextView for the item price
            TextView itemPriceTextView = new TextView(this);
            itemPriceTextView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
            itemPriceTextView.setText(getString(R.string.regular_price_format, itemPrice));
            itemPriceTextView.setTextSize(15);
            itemPriceTextView.setGravity(Gravity.CENTER);
            itemPriceTextView.setTextColor(getResources().getColor(R.color.black));
            itemRow.addView(itemPriceTextView);

            // Add the itemRow to itemLayout
            itemLayout.addView(itemRow);

            // Create a View (divider)
            View divider = new View(this);
            divider.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 3));
            divider.setBackgroundColor(getResources().getColor(R.color.green));
            itemLayout.addView(divider);
        }

        //TextView totalTextView = findViewById(R.id.serviceMode);
        //totalTextView.setText(getString(R.string.regular_price_format, totalPrice));

        // Add itemLayout to the ScrollView
        // nestedScrollView.addView(itemLayout);
    }
}
