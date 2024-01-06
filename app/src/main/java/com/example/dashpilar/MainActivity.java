package com.example.dashpilar;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

                    Button button = findViewById(R.id.button);
                    button.setOnClickListener(v -> startActivity(new Intent(this, MainOrderMenu.class)));
            });
    }
}
