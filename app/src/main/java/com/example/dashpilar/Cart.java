package com.example.dashpilar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class Cart extends AppCompatActivity implements PriceUpdateListener {
    static ArrayList<ItemOrder> cartList = new ArrayList<>();
    Button place_order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        CartItemAdapter adapter = new CartItemAdapter(this, cartList);
        adapter.setPriceUpdateListener(this);
        recyclerView.setAdapter(adapter);

        place_order = findViewById(R.id.placeOrder);
        place_order.setOnClickListener(v -> {
            // logic for printing
        });

        ImageView goBack = findViewById(R.id.back);
        goBack.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());

        onPriceUpdate();
    }


    @Override
    public void onPriceUpdate() {
        float totalPrice = 0;
        for (ItemOrder order : cartList) {
            totalPrice += order.calculatePrice();
        }
        place_order.setText(String.format("Place Order - ₱%.2f", totalPrice));
    }
}