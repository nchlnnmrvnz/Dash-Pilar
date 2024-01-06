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
    ArrayList<String> displayedItems;

    public ItemAdapter(Context context, ArrayList<Item> items) {
        this.context = context;
        this.items = items;
        displayedItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.menu_item_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        for(Item item : items) {
            if (!items.get(position).isAvailable() || displayedItems.contains(items.get(position).getName()))
                position++;
            else
                break;
        }
        displayedItems.add(items.get(position).getName());

        Picasso.get().load(items.get(position).getImageResource()).into(holder.image);

        holder.name.setText(items.get(position).getName());
        holder.price.setText(String.format(Locale.getDefault(),"₱%.2f", items.get(position).getPrice()));
        int finalPosition = position;
        holder.materialCardView.setOnClickListener(v -> {
            AddItem.selectedItem = items.get(finalPosition);
            context.startActivity(new Intent(context, AddItem.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        });
    }

    @Override
    public int getItemCount() {
        int size = 0;

        for(Item item : items) {
            if(item.isAvailable()) {
                size++;
            }
        }

        return size;
    }
}
