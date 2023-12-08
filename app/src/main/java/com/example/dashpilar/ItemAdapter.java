package com.example.dashpilar;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {
    Context context;
    ArrayList<Item> items;

    public ItemAdapter(Context context, ArrayList<Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        holder.image.setImageResource(items.get(position).getImageResource());
        holder.name.setText(items.get(position).getName());
        holder.price.setText(String.format("₱%.2f", items.get(position).getPrice()));
        holder.materialCardView.setOnClickListener(v -> {
            AddItem.selectedItem = items.get(position);
            context.startActivity(new Intent(context, AddItem.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
