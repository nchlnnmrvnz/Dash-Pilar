package com.example.dashpilar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AddItem extends AppCompatActivity {
    static Item selectedItem;
    static ArrayList<CheckBox> checkBoxes = new ArrayList<>();
    private Toast toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // Find the ScrollView in your layout
        ScrollView scrollView = findViewById(R.id.scrollView);

        checkBoxes = new ArrayList<>();

        TextView quantity = findViewById(R.id.textView_quantity);
        quantity.setTextColor(getResources().getColor(R.color.black));
        AtomicInteger quan = new AtomicInteger(1);
        quantity.setText(String.valueOf(quan.get()));

        ImageView incrementQuantity = findViewById(R.id.imageView2);
        incrementQuantity.setOnClickListener(view -> quantity.setText(String.valueOf(quan.incrementAndGet())));

        Button addOrderButton = findViewById(R.id.btn_addOrder);
        addOrderButton.setOnClickListener(view -> {
            LinkedHashMap<String, Float> addOns = new LinkedHashMap<>();
            for(CheckBox checkBox : checkBoxes) {
                if(checkBox.isChecked()) {
                    String addOnName = checkBox.getText().toString();
                    float price = selectedItem.getAddOns().get(addOnName);
                    addOns.put(addOnName, price);
                }
            }
            ItemOrder order = new ItemOrder(selectedItem.getName(), selectedItem.getPrice(),
                    quan.get(), addOns);
            Cart.cartList.add(order);
            createToast("Successfully added order!");
            getOnBackPressedDispatcher().onBackPressed();
        });

        ImageView decrementQuantity = findViewById(R.id.imageView);
        decrementQuantity.setOnClickListener(view -> {
            if(quan.get() > 1)
                quantity.setText(String.valueOf(quan.decrementAndGet()));
            else
                createToast("Quantity cannot be less than 1");
        });

        // Create a LinearLayout to hold the content vertically
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        // Create and add a TextView for item name dynamically based on the selected item
        TextView itemName = new TextView(this);
        itemName.setText(selectedItem.getName()); // Set the selected item name
        itemName.setTextSize(30);
        itemName.setTextColor(getResources().getColor(R.color.green));
        itemName.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        ));
        linearLayout.addView(itemName);

        // Create and add a TextView for item price based on the selected item
        TextView itemPrice = new TextView(this);
        itemPrice.setTextSize(15);
        itemPrice.setTextColor(getResources().getColor(R.color.black));
        itemPrice.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

        float selectedPrice = selectedItem.getPrice();
        itemPrice.setText(getString(R.string.item_price_format, selectedPrice));
        linearLayout.addView(itemPrice);

        // Create and add a divider View with top and bottom margins of 16dp
        View dividerView = new View(this);
        LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                getResources().getDimensionPixelSize(R.dimen.divider_height)
        );
        dividerParams.setMargins(0, getResources().getDimensionPixelSize(R.dimen.divider_margin), 0, getResources().getDimensionPixelSize(R.dimen.divider_margin));
        dividerView.setLayoutParams(dividerParams);
        dividerView.setBackgroundColor(getResources().getColor(R.color.green));
        linearLayout.addView(dividerView);

        TextView addOnsText = new TextView(this);
        addOnsText.setText(getString(R.string.add_ons));
        addOnsText.setTextSize(20);
        addOnsText.setTextColor(getResources().getColor(R.color.black));
        linearLayout.addView(addOnsText);

        // Create and add checkboxes with their text and prices from the addOnsCollection
        for (Map.Entry<String, Float> entry : selectedItem.getAddOns().entrySet()) {
            String addOnName = entry.getKey();
            float addOnPrice = entry.getValue();
            addCheckBox(linearLayout, addOnName, getString(R.string.addon_price_format, addOnPrice));
        }

        // Handle the back button press when imageView_back is clicked
        ImageView imageViewBack = findViewById(R.id.imageView_back);
        imageViewBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        // Add the main content layout to the ScrollView
        scrollView.addView(linearLayout);
    }

    // Helper function to create and add checkboxes with prices
    private void addCheckBox(LinearLayout layout, String text, String price) {
        LinearLayout checkBoxLayout = new LinearLayout(this);
        checkBoxLayout.setOrientation(LinearLayout.HORIZONTAL);

        CheckBox checkBox = new CheckBox(this);
        checkBox.setText(text);
        checkBox.setTextSize(15);
        checkBox.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        ));
        checkBoxes.add(checkBox);
        checkBoxLayout.addView(checkBox);

        TextView priceTextView = new TextView(this);
        priceTextView.setText(price);
        priceTextView.setTextSize(15);
        priceTextView.setTextColor(getResources().getColor(R.color.black));
        priceTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        checkBoxLayout.addView(priceTextView);

        layout.addView(checkBoxLayout);
    }

    void createToast(String message) {
        if (toast != null) {
            toast.cancel(); // Cancel the previous toast if it exists
        }

        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT); // Create a new Toast
        toast.show(); // Show the new Toast
    }
}