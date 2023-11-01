package com.example.dashpilar;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.View;
import android.widget.CheckBox;
import androidx.appcompat.app.AppCompatActivity;

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

        // Create a horizontal LinearLayout for the item name and price
        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);

        // Create and add a TextView for item name
        TextView itemName = new TextView(this);
        itemName.setText("Tokyo");
        itemName.setTextSize(30);
        itemName.setTextColor(getResources().getColor(R.color.green));
        itemName.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        ));
        itemLayout.addView(itemName);

        // Create and add a TextView for item price
        TextView itemPrice = new TextView(this);
        itemPrice.setText("from PHP 39.00");
        itemPrice.setTextSize(15);
        itemPrice.setTextColor(getResources().getColor(R.color.black));
        itemPrice.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        itemLayout.addView(itemPrice);

        // Add the itemLayout to the main content layout
        linearLayout.addView(itemLayout);

        // Create and add a TextView for item description
        TextView itemDescription = new TextView(this);
        itemDescription.setText("Roasted brown sugar milktea flavor. It comes with pearls.");
        itemDescription.setTextSize(15);
        itemDescription.setTextColor(getResources().getColor(R.color.black));
        itemLayout.setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.divider_height));
        linearLayout.addView(itemDescription);

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
        addOnsText.setTextColor(getResources().getColor(R.color.green));
        linearLayout.addView(addOnsText);

        // Create and add checkboxes with their text and prices
        addCheckBox(linearLayout, "Pearls", "+ PHP 9.00");
        addCheckBox(linearLayout, "Salty Cream", "+ PHP 9.00");
        addCheckBox(linearLayout, "Crushed Oreo", "+ PHP 9.00");
        addCheckBox(linearLayout, "Coffee Shot", "+ PHP 9.00");

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
