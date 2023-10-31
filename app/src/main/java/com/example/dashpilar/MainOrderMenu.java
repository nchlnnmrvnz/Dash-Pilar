package com.example.dashpilar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;

public class MainOrderMenu extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private Button dropDownMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_order_menu);

        LinearLayout dropDownContent = new LinearLayout(this);
        dropDownContent.setOrientation(LinearLayout.VERTICAL);
        dropDownMenu = findViewById(R.id.dropDownMenu);

        dropDownMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(MainOrderMenu.this, v);
            popup.setOnMenuItemClickListener(MainOrderMenu.this);
            popup.getMenu().add("All");
            popup.getMenu().add("Coffee");
            popup.getMenu().add("Milktea");
            popup.getMenu().add("Dessert");
            popup.getMenu().add("Frappe");
            popup.getMenu().add("Hot Drinks");
            popup.getMenu().add("Croffles");
            popup.show();
        });
    }

    private void handleItemSelected(String selectedItem) {
        dropDownMenu.setText(selectedItem);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        handleItemSelected(item.getTitle().toString());
        return true;
    }
}
