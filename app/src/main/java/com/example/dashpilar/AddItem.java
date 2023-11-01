package com.example.dashpilar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

public class AddItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // Find the ScrollView in your layout
        ScrollView scrollView = findViewById(R.id.scrollView);

        // Create a LinearLayout to hold the content vertically
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        // Retrieve the selected item name from the Intent's extras
        String selectedItemName = getIntent().getStringExtra("selectedItemName");
        Log.d("SelectedItemName", selectedItemName);

        // Create and add a TextView for item name dynamically based on the selected item
        TextView itemName = new TextView(this);
        itemName.setText(selectedItemName); // Set the selected item name
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

        if (Constants.allItemsCollection.containsKey(selectedItemName)) {
            float selectedPrice = Constants.allItemsCollection.get(selectedItemName);
            itemPrice.setText("₱" + selectedPrice);
        } else
            itemPrice.setText("Item price not found");
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
        addOnsText.setText("Add ons");
        addOnsText.setTextSize(20);
        addOnsText.setTextColor(getResources().getColor(R.color.black));
        linearLayout.addView(addOnsText);

        // Create and add checkboxes with their text and prices from the addOnsCollection
        for (Map.Entry<String, Float> entry : Constants.addOnsCollection.entrySet()) {
            String addOnName = entry.getKey();
            float addOnPrice = entry.getValue();
            addCheckBox(linearLayout, addOnName, "+ ₱" + addOnPrice);
        }

        // Handle the back button press when imageView_back is clicked
        ImageView imageViewBack = findViewById(R.id.imageView_back);
        imageViewBack.setOnClickListener(v -> onBackPressed());

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
        checkBoxLayout.addView(checkBox);

        TextView priceTextView = new TextView(this);
        priceTextView.setText(price);
        priceTextView.setTextSize(15);
        priceTextView.setTextColor(getResources().getColor(R.color.black));
        priceTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        checkBoxLayout.addView(priceTextView);

        layout.addView(checkBoxLayout);
    }
}
