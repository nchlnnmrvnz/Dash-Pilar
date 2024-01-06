package com.example.dashpilar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class AddOnsAdapter extends RecyclerView.Adapter<AddOnsHolder> {
    private final Context context;
    private final ArrayList<AddOn> items;

    public AddOnsAdapter(Context context, ArrayList<AddOn> items) {
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
        holder.checkBox.setText(items.get(position).getName());
        AddItem.checkBoxes.add(holder.checkBox);
        holder.price.setText(String.format(Locale.getDefault(), "+₱%.2f", items.get(position).getPrice()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
