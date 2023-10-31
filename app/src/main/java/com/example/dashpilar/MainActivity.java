package com.example.dashpilar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button btnDineIn, btnTakeOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDineIn = findViewById(R.id.btnDineIn);
        btnTakeOut = findViewById(R.id.btnTakeOut);

        btnDineIn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MainOrderMenu.class)));

        btnTakeOut.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MainOrderMenu.class)));
    }
}
