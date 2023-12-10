package com.example.dashpilar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class AddItem extends AppCompatActivity {
    static Item selectedItem;
    static ArrayList<CheckBox> checkBoxes;
    private Toast toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        checkBoxes = new ArrayList<>();
        checkBoxes.add(findViewById(R.id.addOnPearlsOption));
        checkBoxes.get(0).setOnClickListener(v -> updatePrice());
        checkBoxes.add(findViewById(R.id.addOnSaltyCreamOption));
        checkBoxes.get(1).setOnClickListener(v -> updatePrice());
        checkBoxes.add(findViewById(R.id.addOnCrushedOreoOption));
        checkBoxes.get(2).setOnClickListener(v -> updatePrice());
        checkBoxes.add(findViewById(R.id.addOnCoffeeShotOption));
        checkBoxes.get(3).setOnClickListener(v -> updatePrice());
        updatePrice();

        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageResource(selectedItem.getImageResource());

        TextView itemName = findViewById(R.id.itemName);
        itemName.setText(selectedItem.getName());

        TextView itemPrice = findViewById(R.id.itemPrice);
        String priceFormat = "starts at ₱%.2f";
        itemPrice.setText(String.format(Locale.getDefault(), priceFormat, selectedItem.getPrice()));

        TextView itemDescription = findViewById(R.id.itemDescription);
        itemDescription.setText(selectedItem.getDescription());

        TextView quantity = findViewById(R.id.editText);
        AtomicInteger quan = new AtomicInteger(1);
        quantity.setText(String.valueOf(quan.get()));

        ImageView incrementQuantity = findViewById(R.id.addQuantity);
        incrementQuantity.setOnClickListener(view -> {
            quantity.setText(String.valueOf(quan.incrementAndGet()));
            updatePrice();
        });

        Button addOrderButton = findViewById(R.id.addOrder);
        addOrderButton.setOnClickListener(view -> {
            RadioGroup radioGroup = findViewById(R.id.radioGroup);

            if(radioGroup.getCheckedRadioButtonId() == -1)
                createToast("Please select sugar level");

            else {
                LinkedHashMap<String, Float> addOns = new LinkedHashMap<>();
                for (CheckBox checkBox : checkBoxes) {
                    if (checkBox.isChecked()) {
                        String addOnName = checkBox.getText().toString();
                        float price = selectedItem.getAddOns().get(addOnName);
                        addOns.put(addOnName, price);
                    }
                }
                ItemOrder order = new ItemOrder(selectedItem.getName(), selectedItem.getPrice(),
                        selectedItem.getDescription(), selectedItem.getImageResource(), addOns, quan.get());
                Cart.cartList.add(order);
                createToast("Successfully added order!");
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        ImageView decrementQuantity = findViewById(R.id.minusQuantity);
        decrementQuantity.setOnClickListener(view -> {
            if(quan.get() > 1)
                quantity.setText(String.valueOf(quan.decrementAndGet()));
            else
                createToast("Quantity cannot be less than 1");

            updatePrice();
        });

        // Create a LinearLayout to hold the content vertically
//        LinearLayout linearLayout = new LinearLayout(this);
//        linearLayout.setOrientation(LinearLayout.VERTICAL);
//
//        // Create and add a TextView for item name dynamically based on the selected item
//        TextView itemName = new TextView(this);
//        itemName.setText(selectedItem.getName()); // Set the selected item name
//        itemName.setTextSize(30);
//        itemName.setTextColor(getResources().getColor(R.color.green));
//        itemName.setLayoutParams(new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                1.0f
//        ));
//        linearLayout.addView(itemName);
//
//        // Create and add a TextView for item price based on the selected item
//        TextView itemPrice = new TextView(this);
//        itemPrice.setTextSize(15);
//        itemPrice.setTextColor(getResources().getColor(R.color.black));
//        itemPrice.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
//
//        float selectedPrice = selectedItem.getPrice();
//        itemPrice.setText(getString(R.string.item_price_format, selectedPrice));
//        linearLayout.addView(itemPrice);
//
//        // Create and add a divider View with top and bottom margins of 16dp
//        View dividerView = new View(this);
//        LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                getResources().getDimensionPixelSize(R.dimen.divider_height)
//        );
//        dividerParams.setMargins(0, getResources().getDimensionPixelSize(R.dimen.divider_margin), 0, getResources().getDimensionPixelSize(R.dimen.divider_margin));
//        dividerView.setLayoutParams(dividerParams);
//        dividerView.setBackgroundColor(getResources().getColor(R.color.green));
//        linearLayout.addView(dividerView);
//
//        TextView addOnsText = new TextView(this);
//        addOnsText.setText(getString(R.string.add_ons));
//        addOnsText.setTextSize(20);
//        addOnsText.setTextColor(getResources().getColor(R.color.black));
//        linearLayout.addView(addOnsText);
//
//        // Create and add checkboxes with their text and prices from the addOnsCollection
//        for (Map.Entry<String, Float> entry : selectedItem.getAddOns().entrySet()) {
//            String addOnName = entry.getKey();
//            float addOnPrice = entry.getValue();
//            addCheckBox(linearLayout, addOnName, getString(R.string.addon_price_format, addOnPrice));
//        }
//
//        // Handle the back button press when imageView_back is clicked
//        ImageView imageViewBack = findViewById(R.id.back);
//        imageViewBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
//
//        // Add the main content layout to the ScrollView
//        scrollView.addView(linearLayout);
    }

    void updatePrice() {
        Button addOrderButton = findViewById(R.id.addOrder);
        String addOrderFormat = "ADD TO CART - ₱%.2f";
        float itemTotal = selectedItem.getPrice();
        for(CheckBox checkBox : checkBoxes) {
            if(checkBox.isChecked()) {
                itemTotal += selectedItem.getAddOns().get(checkBox.getText().toString());
            }
        }
        TextView quantity = findViewById(R.id.editText);
        addOrderButton.setText(String.format(Locale.getDefault(), addOrderFormat, itemTotal * Integer.parseInt(quantity.getText().toString())));
    }


    void createToast(String message) {
        if (toast != null) {
            toast.cancel();
        }

        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}