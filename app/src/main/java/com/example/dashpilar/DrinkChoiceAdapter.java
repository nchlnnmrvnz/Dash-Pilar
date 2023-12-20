package com.example.dashpilar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class DrinkChoiceAdapter extends RecyclerView.Adapter<DrinkChoiceHolder> {
    private final Context context;
    private final ArrayList<Item> items;

    public DrinkChoiceAdapter(Context context, ArrayList<Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public DrinkChoiceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DrinkChoiceHolder(LayoutInflater.from(context).inflate(R.layout.drink_choice_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DrinkChoiceHolder holder, int position) {
        holder.radioButton.setText(items.get(position).getName());
        //AddItem.drinkChoiceRadioGroup.addView(holder.radioButton);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
