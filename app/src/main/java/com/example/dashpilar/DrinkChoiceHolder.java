package com.example.dashpilar;

import android.view.View;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DrinkChoiceHolder extends RecyclerView.ViewHolder {
    RadioButton radioButton;

    public DrinkChoiceHolder(@NonNull View itemView) {
        super(itemView);
        radioButton = itemView.findViewById(R.id.radioButton);
    }
}
