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

public class AddOnsAdapter extends RecyclerView.Adapter<AddOnsHolder> {
    private final Context context;
    private final LinkedHashMap<String, Float> items;

    public AddOnsAdapter(Context context, LinkedHashMap<String, Float> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public AddOnsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddOnsHolder(LayoutInflater.from(context).inflate(R.layout.add_ons_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddOnsHolder holder, int position) {
        ArrayList<Map.Entry<String, Float>> itemList = new ArrayList<>(items.entrySet());
        Map.Entry<String, Float> currentItem = itemList.get(position);

        holder.checkBox.setText(currentItem.getKey());
        AddItem.checkBoxes.add(holder.checkBox);
        holder.price.setText(String.format(Locale.getDefault(), "₱%.2f", currentItem.getValue()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
