package com.example.dashpilar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class SoldOutItemAdapter extends RecyclerView.Adapter<SoldOutItemHolder> {
    Context context;
    ArrayList<String> unavailableItems;
    ArrayList<String> displayedItems;

    public SoldOutItemAdapter(Context context, ArrayList<String> unavailableItems) {
        this.context = context;
        this.unavailableItems = unavailableItems;
        displayedItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public SoldOutItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SoldOutItemHolder(LayoutInflater.from(context).inflate(R.layout.not_available_menu_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SoldOutItemHolder holder, int position) {
        for(Item item : Constants.allItemsCollection) {
            String itemName = item.getName().toUpperCase();
            if(unavailableItems.contains(itemName) && !displayedItems.contains(itemName)) {
                displayedItems.add(itemName);
                holder.itemImage.setImageResource(item.getImageResource());
                holder.name.setText(item.getName());
                holder.price.setText(String.format(Locale.getDefault(), "₱%.2f", item.getPrice()));
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        int size = 0;

        for(Item item : Constants.allItemsCollection) {
            if(unavailableItems.contains(item.getName().toUpperCase())) {
                size++;
            }
        }

        return size;
    }
}