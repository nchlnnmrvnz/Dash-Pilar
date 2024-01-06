package com.example.dashpilar;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AddItem extends AppCompatActivity {
    static Item selectedItem;
    RadioGroup drinkChoiceRadioGroup;
    static ArrayList<CheckBox> checkBoxes;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        checkBoxes = new ArrayList<>();
        TextView quantity = findViewById(R.id.editText);
        Button addOrderButton = findViewById(R.id.addOrder);

        LinearLayout drinkChoicesRectangleView = findViewById(R.id.drinkChoicesRectangleView);

        recyclerView = findViewById(R.id.addOnsRecyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        if(selectedItem.getDrinkChoices() == null)
            drinkChoicesRectangleView.setVisibility(View.GONE);
        else {
            drinkChoiceRadioGroup = new RadioGroup(this);
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.MATCH_PARENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT,
                    1f
            );
            params.setMargins(64, 0, 64, 64);
            drinkChoiceRadioGroup.setLayoutParams(params);
            drinkChoiceRadioGroup.setId(View.generateViewId());

            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            linearLayout.setPadding(0, 0, 64, 0);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setId(View.generateViewId());

            LinearLayout textViewLinearLayout = new LinearLayout(this);
            textViewLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            textViewLinearLayout.setGravity(View.TEXT_ALIGNMENT_VIEW_END);
            textViewLinearLayout.setOrientation(LinearLayout.VERTICAL);
            textViewLinearLayout.setId(View.generateViewId());

            boolean firstItem = true;
            for (Item drink : selectedItem.getDrinkChoices()) {
                if(!drink.isAvailable())
                    continue;

                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(drink.getName());
                radioButton.setTextSize(15);
                radioButton.setId(View.generateViewId());

                TextView textView = new TextView(this);
                textView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                textView.setTextColor(Color.BLACK);
                textView.setText(String.format(Locale.getDefault(), "+₱%.2f", 0f));
                textView.setTextSize(15);

                if(firstItem)
                    textView.setPadding(0, 8, 0, 0);
                else
                    textView.setPadding(0, 24, 0, 0);
                firstItem = false;

                drinkChoiceRadioGroup.setLayoutParams(params);
                textView.setId(View.generateViewId());

                textViewLinearLayout.addView(textView);
                drinkChoiceRadioGroup.addView(radioButton);
            }
            linearLayout.addView(drinkChoiceRadioGroup);
            linearLayout.addView(textViewLinearLayout);
            drinkChoicesRectangleView.addView(linearLayout);
        }

        if(!selectedItem.isSugarLevelSelectable()) {
            View v = findViewById(R.id.sugarLevelRectangleView);
            v.setVisibility(View.GONE);

            v = findViewById(R.id.sugarLevel);
            v.setVisibility(View.GONE);

            v = findViewById(R.id.required);
            v.setVisibility(View.GONE);

            v = findViewById(R.id.radioGroup);
            v.setVisibility(View.GONE);

            v = findViewById(R.id.noSugarAddOnFee);
            v.setVisibility(View.GONE);

            v = findViewById(R.id.halfSugarAddOnFee);
            v.setVisibility(View.GONE);

            v = findViewById(R.id.normalSugarAddOnFee);
            v.setVisibility(View.GONE);
        }

        if(selectedItem.getAddOns() == null) {
            LinearLayout addOnsRectangle = findViewById(R.id.addOnsRectangleView);
            addOnsRectangle.setVisibility(View.GONE);
        }
        else {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("availability")
                    .addSnapshotListener((value, error) -> {
                        if (error != null) {
                            Log.w(TAG, "Listen failed.", error);
                            return;
                        }

                        Constants.unavailableItems.clear();

                        for (DocumentSnapshot snapshot : value.getDocuments()) {
                            for (Map.Entry<String, Object> entry : snapshot.getData().entrySet()) {
                                Object unavailableItems = entry.getValue();
                                if (unavailableItems instanceof List<?>) {
                                    List<?> list = (List<?>) unavailableItems;
                                    for (Object item : list) {
                                        if (item instanceof String) {
                                            Constants.unavailableItems.add(((String) item).toUpperCase());
                                        }
                                    }
                                }
                            }
                        }

                        for(Item item : Constants.allItemsCollection) {
                            item.setAvailable(!Constants.unavailableItems.contains(item.getName().toUpperCase()));
                        }

                        for(AddOn item : Constants.drinkAddOnsCollection) {
                            item.setAvailable(!Constants.unavailableItems.contains(item.getName().toUpperCase()));
                        }

                        populateAddOns();
                    });
        }

        Thread t = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                String addOrderFormat = "ADD TO CART - ₱%.2f";
                float itemTotal = selectedItem.getPrice();

                if(!(selectedItem.getAddOns() == null)) {
                    for (CheckBox checkBox : checkBoxes) {
                        if (checkBox.isChecked()) {
                            itemTotal += selectedItem.getAddOn(checkBox.getText().toString()).getPrice();
                        }
                    }
                }

                float finalItemTotal = itemTotal;
                runOnUiThread(() -> addOrderButton.setText(String.format(Locale.getDefault(), addOrderFormat,
                        finalItemTotal * Integer.parseInt(quantity.getText().toString()))));

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        t.start();


        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageResource(selectedItem.getImageResource());

        TextView itemName = findViewById(R.id.itemName);
        itemName.setText(selectedItem.getName());

        TextView itemPrice = findViewById(R.id.itemPrice);
        String priceFormat = "starts at ₱%.2f";
        itemPrice.setText(String.format(Locale.getDefault(), priceFormat, selectedItem.getPrice()));

        TextView itemDescription = findViewById(R.id.itemDescription);
        itemDescription.setText(selectedItem.getDescription());

        AtomicInteger quan = new AtomicInteger(1);
        quantity.setText(String.valueOf(quan.get()));

        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        ImageView incrementQuantity = findViewById(R.id.addQuantity);
        incrementQuantity.setOnClickListener(view -> quantity.setText(String.valueOf(quan.incrementAndGet())));

        recyclerView.post(() -> {
            boolean editItem = getIntent().getBooleanExtra("edit_item", false);
            int position = getIntent().getIntExtra("item_position", -1);
            if(editItem) {
                initializeOrder(Cart.cartList.get(position));
                Intent intent = new Intent(this, AddItem.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("edit_item", false);
                intent.putExtra("item_position", -1);
            }
            addOrderButton.setOnClickListener(view -> {
                ItemOrder order = getOrder(quan);

                if(!(order == null)) {
                    if(editItem) {
                        Cart.cartList.set(position, order);

                        Intent intent = new Intent(this, Cart.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else
                        Cart.cartList.add(order);

                    finish();
                }
            });
        });

        ImageView decrementQuantity = findViewById(R.id.minusQuantity);
        decrementQuantity.setOnClickListener(view -> {
            if(quan.get() > 1)
                quantity.setText(String.valueOf(quan.decrementAndGet()));
            else
                createToast("Quantity cannot be less than 1");
        });
    }

    void populateAddOns() {
        linearLayoutManager.removeAllViews();

        AddOnsAdapter adapter = new AddOnsAdapter(this, filterAddOns(selectedItem.getAddOns()));
        recyclerView.setAdapter(adapter);
    }

    ArrayList<AddOn> filterAddOns(ArrayList<AddOn> items) {
        ArrayList<AddOn> filteredItems = new ArrayList<>();

        if(!(items == null)) {
            for (AddOn item : items) {
                if (item.isAvailable())
                    filteredItems.add(item);
            }
        }

        return filteredItems;
    }

    void initializeOrder(ItemOrder order) {
        if (!(selectedItem.getDrinkChoices() == null)){

            for (int i = 0; i < drinkChoiceRadioGroup.getChildCount(); i++) {
                View view = drinkChoiceRadioGroup.getChildAt(i);

                if (view instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) view;
                    String text = radioButton.getText().toString();

                    if (text.equals(order.getChosenDrink()))
                        radioButton.setChecked(true);
                }
            }
        }

        if (order.isSugarLevelSelectable()) {
            RadioGroup radioGroup = findViewById(R.id.radioGroup);
            switch (order.getSugarLevel()) {
                case 0:
                    radioGroup.check(R.id.noSugarOption);
                    break;
                case 50:
                    radioGroup.check(R.id.halfSugarOption);
                    break;
                case 100:
                    radioGroup.check(R.id.normalSugarOption);
                    break;
                default:
                    createToast("Can't find sugar level!");
            }
        }

        for (CheckBox checkBox : checkBoxes) {
            for(AddOn addOn : order.getCheckedAddOns())
                if (addOn.getName().equals(checkBox.getText().toString())) {
                    checkBox.setChecked(true);
                }
        }

    }

    ItemOrder getOrder(AtomicInteger quan) {
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        int sugarLevel = -1;
        ArrayList<AddOn> addOns = new ArrayList<>();
        ItemOrder order = null;

        if (drinkChoiceRadioGroup != null && drinkChoiceRadioGroup.getCheckedRadioButtonId() == -1 && !(selectedItem.getDrinkChoices() == null)) {
            createToast("Please select a drink flavor");
        } else if (radioGroup.getCheckedRadioButtonId() == -1 && selectedItem.isSugarLevelSelectable())
            createToast("Please select sugar level");
        else {
            int checkedId = radioGroup.getCheckedRadioButtonId();
            if (checkedId == R.id.noSugarOption) {
                sugarLevel = 0;
            } else if (checkedId == R.id.halfSugarOption) {
                sugarLevel = 50;
            } else if (checkedId == R.id.normalSugarOption) {
                sugarLevel = 100;
            }

            for (CheckBox checkBox : checkBoxes) {
                if (checkBox.isChecked()) {
                    String addOnName = checkBox.getText().toString();
                    float price = selectedItem.getAddOn(addOnName).getPrice();
                    addOns.add(selectedItem.getAddOn(addOnName));
                }
            }
            order = new ItemOrder(selectedItem.getName(), selectedItem.getPrice(),
                    selectedItem.getDescription(), selectedItem.getImageResource(), selectedItem.isSugarLevelSelectable(),
                    selectedItem.getAddOns(), selectedItem.getDrinkChoices(), addOns, quan.get(), sugarLevel);

            if (!(selectedItem.getDrinkChoices() == null)){
                int radioButtonID = drinkChoiceRadioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) drinkChoiceRadioGroup.findViewById(radioButtonID);
                String drinkChoice = (String) radioButton.getText();
                order.setGetChosenDrink(drinkChoice);
            }
            createToast("Successfully added order!");
        }


        return order;
    }

    void createToast(String message) {
        if (toast != null) {
            toast.cancel();
        }

        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}