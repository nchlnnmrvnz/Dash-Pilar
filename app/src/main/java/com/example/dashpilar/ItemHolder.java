package com.example.dashpilar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

public class ItemHolder extends RecyclerView.ViewHolder {
    MaterialCardView materialCardView;
    ImageView image;
    TextView name;
    TextView price;

    public ItemHolder(@NonNull View itemView) {
        super(itemView);
        materialCardView = itemView.findViewById(R.id.cardView);
        image = itemView.findViewById(R.id.image);
        name = itemView.findViewById(R.id.name);
        price = itemView.findViewById(R.id.price);
    }
}
