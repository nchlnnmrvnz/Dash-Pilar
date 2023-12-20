package com.example.dashpilar;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AddOnsHolder extends RecyclerView.ViewHolder {
    CheckBox checkBox;
    TextView price;

    public AddOnsHolder(@NonNull View itemView) {
        super(itemView);
        checkBox = itemView.findViewById(R.id.checkbox);
        price = itemView.findViewById(R.id.price);
    }
}
