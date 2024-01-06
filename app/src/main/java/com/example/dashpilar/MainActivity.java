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
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> snapshots = task.getResult().getDocuments();

                    for (DocumentSnapshot snapshot : snapshots) {
                        for (Map.Entry<String, Object> entry : snapshot.getData().entrySet()) {
                            Object value = entry.getValue();
                            if (value instanceof List<?>) {
                                List<?> list = (List<?>) value;
                                for (Object item : list) {
                                    if (item instanceof String) {
                                        Constants.unavailableItems.add(((String) item).toUpperCase());
                                    }
                                }
                            }
                        }
                    }

                    for(Item item : Constants.allItemsCollection) {
                        if(Constants.unavailableItems.contains(item.getName().toUpperCase()))
                            item.setAvailable(false);
                    }

                    for(AddOn item : Constants.drinkAddOnsCollection) {
                        if(Constants.unavailableItems.contains(item.getName().toUpperCase()))
                            item.setAvailable(false);
                    }

                    Button button = findViewById(R.id.button);
                    button.setOnClickListener(v -> startActivity(new Intent(this, MainOrderMenu.class)));

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            });
    }
}
