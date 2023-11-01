package com.example.dashpilar;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import androidx.core.content.res.ResourcesCompat;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedHashMap;

public class MainOrderMenu extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private Button dropDownMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_order_menu);

        dropDownMenu = findViewById(R.id.dropDownMenu);
        dropDownMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(MainOrderMenu.this, v);
            popup.setOnMenuItemClickListener(this);
            popup.getMenu().add("All");
            popup.getMenu().add("Specialty Coffee");
            popup.getMenu().add("Milktea with Pearls");
            popup.getMenu().add("Dessert with Salty Cream");
            popup.getMenu().add("Blended Frappe");
            popup.getMenu().add("Hot Drinks");
            popup.show();
        });

        ImageView cart = findViewById(R.id.imageView_cart);
        cart.setOnClickListener(v -> startActivity(new Intent(this, Cart.class)));
    }

    private void createScrollViewForCategory(String[] items, LinkedHashMap<String, Float> prices) {
        LinearLayout itemLayout = findViewById(R.id.itemLayout);
        itemLayout.removeAllViews();

        for (String item : items) {
            // Create the main item TextView
            TextView itemTextView = new TextView(this);
            itemTextView.setText(item);
            itemTextView.setTextSize(20);
            itemTextView.setTextColor(getResources().getColor(R.color.black));
            itemTextView.setPadding(8, 0, 8, 0);

            // Add a drawable to the TextView
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.add_item, null);
            itemTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);

            // Get the price for the current item
            Float price = prices.get(item);

            // Create the price TextView with the fetched price
            TextView priceTextView = new TextView(this);
            priceTextView.setText(getString(R.string.price_format, price));
            priceTextView.setTextSize(15);
            priceTextView.setTextColor(getResources().getColor(R.color.black));
            priceTextView.setPadding(24, 0, 24, 0);

            // Create the divider View with a fixed height in pixels
            View dividerView = new View(this);
            dividerView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    getResources().getDimensionPixelSize(R.dimen.divider_height)
            ));
            dividerView.setBackgroundColor(getResources().getColor(R.color.green));

            // Add the main item TextView, price TextView, and the divider View to a new LinearLayout (vertical arrangement)
            LinearLayout verticalLayout = new LinearLayout(this);
            verticalLayout.setOrientation(LinearLayout.VERTICAL);
            verticalLayout.addView(itemTextView);
            verticalLayout.addView(priceTextView);
            verticalLayout.addView(dividerView);

            // Add the verticalLayout to the itemLayout
            itemLayout.addView(verticalLayout);

            // Start the activity_add_item when the itemTextView is clicked
            itemTextView.setOnClickListener(view -> startActivity(new Intent(this, AddItem.class)));
        }
    }

    private void showScrollViewForCategory(String category) {
        LinkedHashMap<String, Float> itemsToDisplay;

        switch (category) {
            case "Specialty Coffee":
                itemsToDisplay = Constants.coffeeCollection;
                break;
            case "Milktea with Pearls":
                itemsToDisplay = Constants.milkteaCollection;
                break;
            case "Dessert with Salty Cream":
                itemsToDisplay = Constants.dessertCollection;
                break;
            case "Blended Frappe":
                itemsToDisplay = Constants.frappeCollection;
                break;
            case "Hot Drinks":
                itemsToDisplay = Constants.hotDrinksCollection;
                break;
            default:
                itemsToDisplay = Constants.allItemsCollection;
                break;
        }

        createScrollViewForCategory(itemsToDisplay.keySet().toArray(new String[0]), itemsToDisplay);
        dropDownMenu.setText(category);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        showScrollViewForCategory(menuItem.getTitle().toString());
        return true;
    }
}
