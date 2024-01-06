package com.example.dashpilar;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MainOrderMenu extends AppCompatActivity {
    RecyclerView lastRecyclerView = null;
    TextView lastTextView = null;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_order_menu);

        TextView cartQuantity = findViewById(R.id.cartQuantity);

        AtomicInteger totalOrders = new AtomicInteger(0);
        Thread t = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                int numberOfOrders = 0;
                for (ItemOrder order : Cart.cartList) {
                    numberOfOrders += order.getQuantity();
                }

                totalOrders.set(numberOfOrders);

                runOnUiThread(() -> cartQuantity.setText(String.valueOf(totalOrders.get())));

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        t.start();

        populateAvailableMenu("Limited Edition", Constants.limitedEditionCollection);
        populateAvailableMenu("Specialty Coffee", Constants.coffeeCollection);
        populateAvailableMenu("Milktea with Pearls", Constants.milkteaCollection);
        populateAvailableMenu("Dessert with Salty Cream", Constants.dessertCollection);
        populateAvailableMenu("Blended Frappe", Constants.frappeCollection);
        populateAvailableMenu("Hot Drinks", Constants.hotDrinksCollection);
        populateAvailableMenu("Croffles", Constants.crofflesCollection);
        populateAvailableMenu("Plain Combo", Constants.plainCroffleComboCollection);
        populateSoldOutMenu(Constants.allItemsCollection);

        FrameLayout cart = findViewById(R.id.cart);
        cart.setOnClickListener(v -> {
            if (Cart.cartList.size() == 0)
                createToast();

            startActivity(new Intent(this, Cart.class));
        });

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }

    private void populateAvailableMenu(String category, ArrayList<Item> items) {
        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayoutForRecyclerView);
        NestedScrollView scrollView = findViewById(R.id.scrollView);

        TextView textView = new TextView(this);
        textView.setId(View.generateViewId());
        textView.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        ));
        textView.setTextSize(40);
        textView.setAllCaps(true);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setPadding(0, 16, 0, 0);
        textView.setTextColor(getResources().getColor(R.color.green));
        textView.setText(category);

        RecyclerView recyclerView = new RecyclerView(getApplicationContext());
        recyclerView.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        ));
        recyclerView.setId(View.generateViewId());
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(new ItemAdapter(getApplicationContext(), items));

        constraintLayout.addView(textView);
        constraintLayout.addView(recyclerView);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        constraintSet.connect(textView.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        if (lastTextView != null)
            constraintSet.connect(textView.getId(), ConstraintSet.TOP, lastRecyclerView.getId(), ConstraintSet.BOTTOM);

        constraintSet.connect(recyclerView.getId(), ConstraintSet.TOP, textView.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(recyclerView.getId(), ConstraintSet.START, scrollView.getId(), ConstraintSet.START);
        constraintSet.connect(recyclerView.getId(), ConstraintSet.END, scrollView.getId(), ConstraintSet.END);

        lastTextView = textView;
        lastRecyclerView = recyclerView;

        constraintSet.applyTo(constraintLayout);

        scrollView.post(() -> scrollView.smoothScrollTo(0, 0));
    }

    private void populateSoldOutMenu(ArrayList<Item> items) {
        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayoutForRecyclerView);
        NestedScrollView scrollView = findViewById(R.id.scrollView);

        TextView textView = new TextView(this);
        textView.setId(View.generateViewId());
        textView.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        ));
        textView.setTextSize(40);
        textView.setAllCaps(true);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setPadding(0, 16, 0, 0);
        textView.setTextColor(getResources().getColor(R.color.green));
        textView.setText(R.string.unavailable_items);

        RecyclerView recyclerView = new RecyclerView(getApplicationContext());
        recyclerView.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        ));
        recyclerView.setId(View.generateViewId());
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.setAdapter(new SoldOutItemAdapter(getApplicationContext(), Constants.unavailableItems));

        constraintLayout.addView(textView);
        constraintLayout.addView(recyclerView);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        constraintSet.connect(textView.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        if (lastTextView != null)
            constraintSet.connect(textView.getId(), ConstraintSet.TOP, lastRecyclerView.getId(), ConstraintSet.BOTTOM);

        constraintSet.connect(recyclerView.getId(), ConstraintSet.TOP, textView.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(recyclerView.getId(), ConstraintSet.START, scrollView.getId(), ConstraintSet.START);
        constraintSet.connect(recyclerView.getId(), ConstraintSet.END, scrollView.getId(), ConstraintSet.END);

        lastTextView = textView;
        lastRecyclerView = recyclerView;

        constraintSet.applyTo(constraintLayout);

        scrollView.post(() -> scrollView.smoothScrollTo(0, 0));
    }

    void createToast() {
        if (toast != null) {
            toast.cancel();
        }

        toast = Toast.makeText(this, "Cart is empty!", Toast.LENGTH_SHORT);
        toast.show();
    }
}
