package com.example.dashpilar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SoldOutItemHolder extends RecyclerView.ViewHolder {
    ImageView itemImage;
    TextView name;
    TextView price;

    public SoldOutItemHolder(@NonNull View itemView) {
        super(itemView);
        itemImage = itemView.findViewById(R.id.image);
        name = itemView.findViewById(R.id.name);
        price = itemView.findViewById(R.id.price);
    }
}