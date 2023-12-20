package com.example.dashpilar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {
    private final Context context;
    private final ArrayList<Item> items;

    public ItemAdapter(Context context, ArrayList<Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.menu_item_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Picasso.get().load(items.get(position).getImageResource()).into(holder.image);

        holder.name.setText(items.get(position).getName());
        holder.price.setText(String.format(Locale.getDefault(),"₱%.2f", items.get(position).getPrice()));
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
