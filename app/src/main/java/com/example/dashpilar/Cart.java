package com.example.dashpilar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Cart extends AppCompatActivity {
    static ArrayList<ItemOrder> cartList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ImageView imageViewBack = findViewById(R.id.imageView_back);
        imageViewBack.setOnClickListener(v -> onBackPressed());

        LinearLayout linearLayout = findViewById(R.id.itemLayout);
        TextView textView = new TextView(this);

        StringBuilder list = new StringBuilder();
        int counter = 1;
        float total = 0;
        for(ItemOrder order : cartList) {
            float itemPrice = 0;
            list.append(counter).append("\n");
            list.append("Name: ").append(order.getName()).append("\n");
            list.append("Price: ").append(order.getPrice()).append("\n");
            itemPrice += order.getPrice();
            list.append("Quantity: ").append(order.getQuantity()).append("\n");

            list.append("Add ons:\n");
            for(Map.Entry<String, Float> addOn : order.getAddOns().entrySet()) {
                list.append(addOn.getKey()).append(" - ").append(addOn.getValue()).append("\n");
                itemPrice += addOn.getValue();
            }

            itemPrice *= order.getQuantity();
            list.append("Subtotal: ").append(itemPrice).append("\n");
            total += itemPrice;
            list.append("\n");
            counter++;
        }
        list.append("Total: ").append(total);

        textView.setTextSize(30);
        textView.setTextColor(getResources().getColor(R.color.green));
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        ));

        Toast.makeText(this, list.toString(), Toast.LENGTH_LONG).show();
        linearLayout.addView(textView);
    }
}