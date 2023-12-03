package com.example.dashpilar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainOrderMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_order_menu);

        createScrollViewForCategory("Specialty Coffee", Constants.coffeeCollection);
        createScrollViewForCategory("Milktea with Pearls", Constants.milkteaCollection);
        createScrollViewForCategory("Dessert with Salty Cream", Constants.dessertCollection);
        createScrollViewForCategory("Blended Frappe", Constants.frappeCollection);
        createScrollViewForCategory("Hot Drinks", Constants.hotDrinksCollection);

        FloatingActionButton cart = findViewById(R.id.cart);
        cart.setOnClickListener(v -> startActivity(new Intent(this, Cart.class)));

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }

    private void createScrollViewForCategory(String category, ArrayList<Item> items) {
        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);
        TextView textView = new TextView(this);
        {
            // Create a new TextView
            textView.setId(View.generateViewId());

            // Set TextView properties
            textView.setLayoutParams(new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            ));
            textView.setTextSize(25);
            textView.setText(category);

            // Set Constraints using ConstraintSet
            constraintLayout.addView(textView);

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);

            // Set constraints programmatically
            constraintSet.connect(textView.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            constraintSet.connect(textView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);

            constraintSet.applyTo(constraintLayout);
        }

        ArrayList<CardView> cardViewArrayList = new ArrayList<>();
        for (Item item : items) {
            // Create a new CardView for each item
            CardView cardView = new CardView(this);
            cardView.setId(View.generateViewId());

            // Set CardView properties
            cardView.setLayoutParams(new ConstraintLayout.LayoutParams(
                    0,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            ));
            cardView.setCardElevation(4);
            cardView.setUseCompatPadding(true);

            // Add the CardView to the list
            cardViewArrayList.add(cardView);

            // Add cardView to the constraintLayout
            constraintLayout.addView(cardView);
        }

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        for (int i = 0; i < cardViewArrayList.size(); i++) {
            CardView cardView = cardViewArrayList.get(i);

            int rowNumber = i / 3;
            int columnNumber = i % 3;

            constraintSet.connect(cardView.getId(), ConstraintSet.TOP,
                    rowNumber == 0 ? textView.getId() : cardViewArrayList.get(i - 3).getId(), rowNumber == 0 ? ConstraintSet.BOTTOM : ConstraintSet.TOP);

            if (columnNumber == 0) {
                constraintSet.connect(cardView.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
                if (i < cardViewArrayList.size() - 1) {
                    constraintSet.connect(cardView.getId(), ConstraintSet.END, cardViewArrayList.get(i + 1).getId(), ConstraintSet.START);
                    constraintSet.setHorizontalChainStyle(cardView.getId(), ConstraintSet.CHAIN_PACKED);
                }
            } else {
                constraintSet.connect(cardView.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
                constraintSet.connect(cardView.getId(), ConstraintSet.START, cardViewArrayList.get(i - 1).getId(), ConstraintSet.END);
            }

            if (rowNumber > 0) {
                constraintSet.connect(cardView.getId(), ConstraintSet.TOP, cardViewArrayList.get(i - 3).getId(), ConstraintSet.BOTTOM);
            }

            constraintSet.constrainWidth(cardView.getId(), ConstraintSet.MATCH_CONSTRAINT);
            constraintSet.setMargin(cardView.getId(), ConstraintSet.TOP, 1);
        }

        constraintSet.applyTo(constraintLayout);

        // Populate the CardViews with content
        for (int i = 0; i < items.size(); i++) {
            CardView currentCardView = cardViewArrayList.get(i);
            Item item = items.get(i);

            // Create a LinearLayout to hold the contents within the CardView
            LinearLayout cardLayout = new LinearLayout(this);
            cardLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            cardLayout.setOrientation(LinearLayout.VERTICAL);

            // Create an ImageView and set the image
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0, 1
            ));
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setMaxWidth(100);
            imageView.setImageResource(item.getImageResource());

            // Create TextViews for item name and price
            TextView itemNameTextView = new TextView(this);
            itemNameTextView.setText(item.getName());
            itemNameTextView.setTextSize(20);

            TextView itemPriceTextView = new TextView(this);
            itemPriceTextView.setText(getString(R.string.item_price_format, item.getPrice()));
            itemPriceTextView.setTextSize(15);

            // Add the views to the cardLayout
            cardLayout.addView(imageView);
            cardLayout.addView(itemNameTextView);
            cardLayout.addView(itemPriceTextView);

            // Add cardLayout to the CardView
            currentCardView.addView(cardLayout);
        }
    }
}
