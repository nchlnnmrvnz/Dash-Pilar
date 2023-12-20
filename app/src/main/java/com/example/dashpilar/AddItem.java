package com.example.dashpilar;

import android.content.Intent;
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

        //checkBoxes = new ArrayList<>();
        //checkBoxes.add(findViewById(R.id.addOnPearlsOption));
        //checkBoxes.get(0).setOnClickListener(v -> updatePrice());
        //checkBoxes.add(findViewById(R.id.addOnSaltyCreamOption));
//        checkBoxes.get(1).setOnClickListener(v -> updatePrice());
//        checkBoxes.add(findViewById(R.id.addOnCrushedOreoOption));
//        checkBoxes.get(2).setOnClickListener(v -> updatePrice());
//        checkBoxes.add(findViewById(R.id.addOnCoffeeShotOption));
//        checkBoxes.get(3).setOnClickListener(v -> updatePrice());
//        updatePrice();

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

        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        ImageView incrementQuantity = findViewById(R.id.addQuantity);
        incrementQuantity.setOnClickListener(view -> {
            quantity.setText(String.valueOf(quan.incrementAndGet()));
            updatePrice();
        });

        boolean editItem = getIntent().getBooleanExtra("edit_item", false);
        int position = getIntent().getIntExtra("item_position", -1);
        if(editItem)
            initializeOrder(Cart.cartList.get(position));

        Button addOrderButton = findViewById(R.id.addOrder);
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

        ImageView decrementQuantity = findViewById(R.id.minusQuantity);
        decrementQuantity.setOnClickListener(view -> {
            if(quan.get() > 1)
                quantity.setText(String.valueOf(quan.decrementAndGet()));
            else
                createToast("Quantity cannot be less than 1");

            updatePrice();
        });
    }

    void initializeOrder(ItemOrder order) {
        RadioGroup radioGroup = findViewById(R.id.radioGroup);

        switch(order.getSugarLevel()) {
            case 0:
                radioGroup.check(R.id.noSugarOption);
                break;
            case 50:
                radioGroup.check(R.id.halfSugarOption);
                break;
            case 100:
                radioGroup.check(R.id.normalSugarOption);
                break;
            default: createToast("Can't find sugar level!");
        }

        for (CheckBox checkBox : checkBoxes) {
            if (order.getCheckedAddOns().containsKey(checkBox.getText().toString())) {
                checkBox.setChecked(true);
            }
        }

    }

    ItemOrder getOrder(AtomicInteger quan) {
        RadioGroup radioGroup = findViewById(R.id.radioGroup);

        if(radioGroup.getCheckedRadioButtonId() == -1)
            createToast("Please select sugar level");

        else {
            int sugarLevel = -1;
            int checkedId = radioGroup.getCheckedRadioButtonId();

            if (checkedId == R.id.noSugarOption) {
                sugarLevel = 0;
            } else if (checkedId == R.id.halfSugarOption) {
                sugarLevel = 50;
            } else if (checkedId == R.id.normalSugarOption) {
                sugarLevel = 100;
            }

            LinkedHashMap<String, Float> addOns = new LinkedHashMap<>();
            for (CheckBox checkBox : checkBoxes) {
                if (checkBox.isChecked()) {
                    String addOnName = checkBox.getText().toString();
                    float price = selectedItem.getAddOns().get(addOnName);
                    addOns.put(addOnName, price);
                }
            }
            ItemOrder order = new ItemOrder(selectedItem.getName(), selectedItem.getPrice(),
                    selectedItem.getDescription(), selectedItem.getImageResource(), selectedItem.getAddOns(),
                    addOns, quan.get(), sugarLevel);

            createToast("Successfully added order!");
            return order;
        }
        return null;
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