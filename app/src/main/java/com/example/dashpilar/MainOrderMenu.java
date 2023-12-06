package com.example.dashpilar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainOrderMenu extends AppCompatActivity {
    RecyclerView lastRecyclerView = null;
    TextView lastTextView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_order_menu);

        createScrollViewForCategory("Specialty Coffee", Constants.coffeeCollection);
        loadDataAndCreateViews();

        FloatingActionButton cart = findViewById(R.id.cart);
        cart.setOnClickListener(v -> startActivity(new Intent(this, Cart.class)));

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }

    private void createScrollViewForCategory(String category, ArrayList<Item> items) {
        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayoutForRecyclerView);

        TextView textView = new TextView(this);
        textView.setId(View.generateViewId());
        textView.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        ));
        textView.setTextSize(25);
        textView.setText(category);

        RecyclerView recyclerView = new RecyclerView(getApplicationContext());
        recyclerView.setId(View.generateViewId());
        int numberOfColumns = 3;
        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new ItemAdapter(getApplicationContext(), items));

        constraintLayout.addView(textView);
        constraintLayout.addView(recyclerView);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        // Set constraints using ConstraintSet
        constraintSet.connect(textView.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        if (lastTextView != null) {
            constraintSet.connect(textView.getId(), ConstraintSet.TOP, lastRecyclerView.getId(), ConstraintSet.BOTTOM);
        }

        constraintSet.connect(recyclerView.getId(), ConstraintSet.TOP, textView.getId(), ConstraintSet.BOTTOM);

        lastTextView = textView;
        lastRecyclerView = recyclerView;

        constraintSet.applyTo(constraintLayout);

        NestedScrollView nestedScrollView = findViewById(R.id.nestedScrollView);
        nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, 0));
    }

    private class LoadDataAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            createScrollViewForCategory("Milktea with Pearls", Constants.milkteaCollection);
            createScrollViewForCategory("Dessert with Salty Cream", Constants.dessertCollection);
            createScrollViewForCategory("Blended Frappe", Constants.frappeCollection);
            createScrollViewForCategory("Hot Drinks", Constants.hotDrinksCollection);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Update UI if needed
        }
    }

    // Call this method in your onCreate
    private void loadDataAndCreateViews() {
        new LoadDataAsyncTask().execute();
    }
}
