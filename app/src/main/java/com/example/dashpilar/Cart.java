package com.example.dashpilar;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
import android.view.View;
import android.view.Gravity;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class Cart extends AppCompatActivity {
    static ArrayList<ItemOrder> cartList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ImageView goBack = findViewById(R.id.imageView_back);
        goBack.setOnClickListener(view -> onBackPressed());

        // Find the ScrollView in your XML layout
        ScrollView scrollView = findViewById(R.id.scrollView);

        // Create a LinearLayout to hold the dynamic content
        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        itemLayout.setOrientation(LinearLayout.VERTICAL);

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
            quantityText.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            quantityText.setText("1");
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
                itemName.setText("Dash Latte");
                itemName.setTextColor(getResources().getColor(R.color.black));
                itemName.setTextSize(20);
                itemDetails.addView(itemName);

                TextView itemAddOns = new TextView(this);
                itemAddOns.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                itemAddOns.setText("coffee shot, salty cream");
                itemAddOns.setPadding(8,0,0,0);
                itemAddOns.setTextColor(getResources().getColor(R.color.black));
                itemAddOns.setTextSize(15);
                itemDetails.addView(itemAddOns);

            itemRow.addView(itemDetails);

            // Create and add a TextView for the item price
            TextView itemPrice = new TextView(this);
            itemPrice.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
            itemPrice.setText("₱39.00");
            itemPrice.setTextSize(15);
            itemPrice.setGravity(Gravity.CENTER);
            itemPrice.setTextColor(getResources().getColor(R.color.black));
            itemRow.addView(itemPrice);

            // Add the itemRow to itemLayout
            itemLayout.addView(itemRow);

            // Create a View (divider)
            View divider = new View(this);
            divider.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 3));
            divider.setBackgroundColor(getResources().getColor(R.color.green));
            itemLayout.addView(divider);
        }

        // Add itemLayout to the ScrollView
        scrollView.addView(itemLayout);
    }
}
