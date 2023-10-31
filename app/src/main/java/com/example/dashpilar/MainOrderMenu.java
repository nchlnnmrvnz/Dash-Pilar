package com.example.dashpilar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainOrderMenu extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private Button dropDownMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_order_menu);

        dropDownMenu = findViewById(R.id.dropDownMenu);
        findViewById(R.id.right_menu);

        dropDownMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(MainOrderMenu.this, v);
            popup.setOnMenuItemClickListener(MainOrderMenu.this);
            popup.getMenu().add("All");
            popup.getMenu().add("Coffee");
            popup.getMenu().add("Milktea");
            popup.getMenu().add("Dessert");
            popup.getMenu().add("Frappe");
            popup.getMenu().add("Hot Drinks");
            popup.getMenu().add("Croffles");
            popup.show();
        });
    }

    private void createScrollViewForCategory(String[] items) {
        LinearLayout itemLayout = findViewById(R.id.itemLayout);

        // Clear the itemLayout to remove previous items
        itemLayout.removeAllViews();

        for (String item : items) {
            TextView itemTextView = new TextView(this);
            itemTextView.setText(item);
            itemTextView.setTextSize(16);
            itemTextView.setPadding(16, 8, 16, 8);
            // Customize the TextView as needed
            itemLayout.addView(itemTextView);
        }
    }

    private void showScrollViewForCategory(String category) {
        // Define your items for different categories
        String[] coffeeItems = {"Dash Latte", "Cloud Latte", "Cloud Seasalt", "Spanish Latte", "Macchiato Coffee", "Sweet Americano", "Dark Mocha"};
        String[] milkteaItems = {"Tokyo", "Sapporo", "Hokkaido", "Okinawa", "Nagoya", "Kyoto", "Osaka"};
        String[] dessertItems = {"Forrest Cake", "Matcha Cream", "Choco Nutty", "Mango Dream", "Dark Chocolate", "Velvet Cake"};
        String[] frappeItems = {"Dark Forrest", "Taro Cream", "Red Chocolate", "Vanilla Bean", "Oreo Cream", "Nutty Choco"};
        String[] hotDrinkItems = {"Dash Latte", "Spanish Latte", "Caramel Macchiato", "Matcha Latte", "Dark Chocolate", "Hazelnut"};
        String[] croffleItems = {"Sugar", "Chocolate", "Biscoff"};
        String[] allItems = mergeArrays(coffeeItems, milkteaItems, dessertItems, frappeItems, hotDrinkItems, croffleItems);

        // Decide which items to show based on the category
        String[] itemsToDisplay;

        switch (category) {
            case "Coffee":
                itemsToDisplay = coffeeItems;
                break;
            case "Milktea":
                itemsToDisplay = milkteaItems;
                break;
            case "Dessert":
                itemsToDisplay = dessertItems;
                break;
            case "Frappe":
                itemsToDisplay = frappeItems;
                break;
            case "Hot Drinks":
                itemsToDisplay = hotDrinkItems;
                break;
            case "Croffles":
                itemsToDisplay = croffleItems;
                break;
            default:
                itemsToDisplay = allItems;
                break;
        }

        createScrollViewForCategory(itemsToDisplay);
        dropDownMenu.setText(category);
    }

    private String[] mergeArrays(String[]... arrays) {
        int totalLength = 0;
        for (String[] array : arrays) {
            totalLength += array.length;
        }

        String[] merged = new String[totalLength];
        int destPos = 0;
        for (String[] array : arrays) {
            System.arraycopy(array, 0, merged, destPos, array.length);
            destPos += array.length;
        }

        return merged;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        showScrollViewForCategory(menuItem.getTitle().toString());
        return true;
    }
}
