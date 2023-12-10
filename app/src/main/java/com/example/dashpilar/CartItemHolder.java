package com.example.dashpilar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartItemHolder extends RecyclerView.ViewHolder {
    ImageView increment;
    ImageView itemImage;
    ImageView decrement;
    TextView name;
    TextView quantity;
    TextView price;
    TextView addOnItems;
    ImageView editButton;

    public CartItemHolder(@NonNull View itemView) {
        super(itemView);
        increment = itemView.findViewById(R.id.incrementQuantity);
        itemImage = itemView.findViewById(R.id.itemImage);
        decrement = itemView.findViewById(R.id.decrementQuantity);
        name = itemView.findViewById(R.id.itemName);
        quantity = itemView.findViewById(R.id.quantity);
        price = itemView.findViewById(R.id.itemPrice);
        addOnItems = itemView.findViewById(R.id.addOnItems);
        editButton = itemView.findViewById(R.id.editButton);
    }
}
