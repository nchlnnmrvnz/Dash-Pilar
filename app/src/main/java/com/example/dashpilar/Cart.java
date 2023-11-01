package com.example.dashpilar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class Cart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ImageView imageViewBack = findViewById(R.id.imageView_back);
        imageViewBack.setOnClickListener(v -> onBackPressed());
    }
}