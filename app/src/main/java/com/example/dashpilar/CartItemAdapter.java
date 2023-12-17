package com.example.dashpilar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemHolder> {
    Context context;
    ArrayList<ItemOrder> items;
    private PriceUpdateListener priceUpdateListener;

    public CartItemAdapter(Context context, ArrayList<ItemOrder> items) {
        this.context = context;
        this.items = items;

    }
    @NonNull
    @Override
    public CartItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartItemHolder(LayoutInflater.from(context).inflate(R.layout.cart_item_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemHolder holder, int position) {
        holder.decrement.setOnClickListener(v -> {
            if(items.get(position).getQuantity() > 0)
                items.get(position).setQuantity(items.get(position).getQuantity() - 1);

            holder.quantity.setText(String.valueOf(items.get(position).getQuantity()));
            holder.price.setText(String.format(Locale.getDefault(),"₱%.2f", items.get(position).calculatePrice()));
            if (priceUpdateListener != null) {
                priceUpdateListener.onPriceUpdate();
            }
        });

        holder.increment.setOnClickListener(v -> {
            items.get(position).setQuantity(items.get(position).getQuantity() + 1);

            holder.quantity.setText(String.valueOf(items.get(position).getQuantity()));
            holder.price.setText(String.format(Locale.getDefault(),"₱%.2f", items.get(position).calculatePrice()));
            if (priceUpdateListener != null) {
                priceUpdateListener.onPriceUpdate();
            }
        });

        holder.itemImage.setImageResource(items.get(position).getImageResource());
        holder.name.setText(items.get(position).getName());
        holder.price.setText(String.format(Locale.getDefault(),"₱%.2f", items.get(position).calculatePrice()));
        if (priceUpdateListener != null) {
            priceUpdateListener.onPriceUpdate();
        }
        holder.sugarLevel.setText(items.get(position).getSugarLevel() + "% sugar");
        holder.addOnItems.setText(items.get(position).getCheckedAddOnString());
        holder.quantity.setText(String.valueOf(items.get(position).getQuantity()));

        holder.editButton.setOnClickListener(v -> {
            AddItem.selectedItem = items.get(position);

            Intent intent = new Intent(context, AddItem.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("edit_item", true);
            intent.putExtra("item_position", position);
            context.startActivity(intent);
        });
    }

    public void setPriceUpdateListener(PriceUpdateListener listener) {
        this.priceUpdateListener = listener;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}