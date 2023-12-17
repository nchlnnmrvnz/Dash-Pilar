package com.example.dashpilar;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.example.dashpilar.printerconnection.AsyncBluetoothEscPosPrint;
import com.example.dashpilar.printerconnection.AsyncEscPosPrint;
import com.example.dashpilar.printerconnection.AsyncEscPosPrinter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class Cart extends AppCompatActivity implements PriceUpdateListener {
    static ArrayList<ItemOrder> cartList = new ArrayList<>();
    Button place_order;
    String currentOrderNumber;
    String serviceMode = null;
    String paymentMethod = null;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        currentOrderNumber = getCurrentOrderNumber();
        TextView orderNumberTextView = findViewById(R.id.orderNumber);
        orderNumberTextView.setText("Order no. " + currentOrderNumber);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        CartItemAdapter adapter = new CartItemAdapter(this, cartList);
        adapter.setPriceUpdateListener(this);
        recyclerView.setAdapter(adapter);

        place_order = findViewById(R.id.placeOrder);
        place_order.setOnClickListener(v -> {
            RadioGroup serviceModeRadioGroup = findViewById(R.id.serviceModeRadioGroup);

            if(serviceModeRadioGroup.getCheckedRadioButtonId() == -1) {
                createToast("Select service mode!");
                return;
            }
            else {
                int checkedId = serviceModeRadioGroup.getCheckedRadioButtonId();

                if (checkedId == R.id.dineInServiceModeOption) {
                    serviceMode = "Dine In";
                } else if (checkedId == R.id.takeOutServiceModeOption) {
                    serviceMode = "Take out";
                }
            }

            RadioGroup radioGroup = findViewById(R.id.paymentMethodRadioGroup);

            if(radioGroup.getCheckedRadioButtonId() == -1) {
                createToast("Select payment method!");
                return;
            }
            else {
                int checkedId = radioGroup.getCheckedRadioButtonId();

                if (checkedId == R.id.cashPaymentMethodOption) {
                    paymentMethod = "Cash";
                } else if (checkedId == R.id.gcashPaymentMethodOption) {
                    paymentMethod = "GCash";
                }
            }

            if(!cartList.isEmpty()) {
                printBluetooth();
                Cart.cartList = new ArrayList<>();
                generateNewOrderNumber();
                getOnBackPressedDispatcher().onBackPressed();
            }
            else
                createToast("Cart is empty!");
        });

        ImageView goBack = findViewById(R.id.back);
        goBack.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());

        onPriceUpdate();
    }


    public interface OnBluetoothPermissionsGranted {
        void onPermissionsGranted();
    }

    public static final int PERMISSION_BLUETOOTH = 1;
    public static final int PERMISSION_BLUETOOTH_ADMIN = 2;
    public static final int PERMISSION_BLUETOOTH_CONNECT = 3;
    public static final int PERMISSION_BLUETOOTH_SCAN = 4;

    public OnBluetoothPermissionsGranted onBluetoothPermissionsGranted;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case Cart.PERMISSION_BLUETOOTH:
                case Cart.PERMISSION_BLUETOOTH_ADMIN:
                case Cart.PERMISSION_BLUETOOTH_CONNECT:
                case Cart.PERMISSION_BLUETOOTH_SCAN:
                    this.checkBluetoothPermissions(this.onBluetoothPermissionsGranted);
                    break;
            }
        }
    }

    public void checkBluetoothPermissions(OnBluetoothPermissionsGranted onBluetoothPermissionsGranted) {
        this.onBluetoothPermissionsGranted = onBluetoothPermissionsGranted;
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.BLUETOOTH}, Cart.PERMISSION_BLUETOOTH);
        } else if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.BLUETOOTH_ADMIN}, Cart.PERMISSION_BLUETOOTH_ADMIN);
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.BLUETOOTH_CONNECT}, Cart.PERMISSION_BLUETOOTH_CONNECT);
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, Cart.PERMISSION_BLUETOOTH_SCAN);
        } else {
            this.onBluetoothPermissionsGranted.onPermissionsGranted();
        }
    }

    private BluetoothConnection selectedDevice;

    public void printBluetooth() {
        this.checkBluetoothPermissions(() -> new AsyncBluetoothEscPosPrint(
                this,
                new AsyncEscPosPrint.OnPrintFinished() {
                    @Override
                    public void onError(AsyncEscPosPrinter asyncEscPosPrinter, int codeException) {
                        Log.e("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : An error occurred !");
                    }

                    @Override
                    public void onSuccess(AsyncEscPosPrinter asyncEscPosPrinter) {
                        Log.i("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : Print is finished !");
                    }
                }
        )
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, this.getAsyncEscPosPrinter(selectedDevice)));
    }

    public AsyncEscPosPrinter getAsyncEscPosPrinter(DeviceConnection printerConnection) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String formattedDate = dateFormat.format(Calendar.getInstance().getTime());
        String formattedTime = timeFormat.format(Calendar.getInstance().getTime());
        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 203, 48f, 32);

        StringBuilder itemList = new StringBuilder();
        float subTotal = 0.00f;

        for (int i = 0; i < cartList.size(); i++){
            itemList.append("[L]").append(cartList.get(i).getQuantity()).append("x ").append(cartList.get(i).getName());
            itemList.append("[R]").append(String.format(Locale.getDefault(), "%.2f\n", cartList.get(i).calculatePrice()));
            itemList.append("[L]   -").append(cartList.get(i).getSugarLevel()).append("% sugar\n");
            itemList.append("[L]   -").append(cartList.get(i).getCheckedAddOnString()).append("\n");
            subTotal += cartList.get(i).calculatePrice();
        }

        float total = subTotal;

        String textToPrint = "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.logo_with_green_text, DisplayMetrics.DENSITY_MEDIUM))+"</img>\n" +
                "[C]Dash Coffee Pilar - Las Pinas\n" +
                "[C]19 St. Joseph St., Almanza Uno,\n" +
                "[C]Las Pinas, 1750 Metro Manila\n" +
                "[L]\n" +
                "[L]Date: " + formattedDate + "\n" +
                "[L]Time: " + formattedTime + "\n" +
                "[L]Service Mode: " + serviceMode +"\n" +
                "[C]\n" +
                "[L]Order No.: " + currentOrderNumber + "\n" +
                itemList +
                "[C]\n" +
                String.format(Locale.getDefault(), "[R]Subtotal: [R]%.2f\n", subTotal) +
                String.format(Locale.getDefault(),"[R]TOTAL: [R]%.2f\n", total) +
                "[R]Payment Method: [R]" + paymentMethod + "\n" +
                "[C]\n" +
                "[C]Thank you for ordering!\n" +
                "[C]\n" +
                "[C]This is not an official receipt" +
                "[C]\n  " +
                "[C]\n  " +
                "[C]\n  ";

        System.out.println(textToPrint);
        return printer.addTextToPrint(textToPrint);
    }

    public String getCurrentOrderNumber() {
        String orderNumber;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String lastUsedYear = sharedPreferences.getString("last_used_year", "");

        if(lastUsedYear.equals("")) {
            DateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
            lastUsedYear = yearFormat.format(Calendar.getInstance().getTime());

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("last_used_year", lastUsedYear);
            editor.apply();
        }

        int currentOrderNumber = sharedPreferences.getInt("last_used_order_number", 1);

        orderNumber = lastUsedYear + "-" + String.format(Locale.getDefault(), "%05d", currentOrderNumber);
        return orderNumber;
    }

    public void generateNewOrderNumber() {
        DateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        String currentYear = yearFormat.format(Calendar.getInstance().getTime());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int lastUsedNumber = sharedPreferences.getInt("last_used_order_number", 0);

        int newOrderNumber;
        String lastUsedYear = sharedPreferences.getString("last_used_year", "");
        if (!lastUsedYear.equals(currentYear)) {
            newOrderNumber = 1;
        } else {
            newOrderNumber = lastUsedNumber + 1;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("last_used_year", currentYear);
        editor.putInt("last_used_order_number", newOrderNumber);
        editor.apply();
    }


    @Override
    public void onPriceUpdate() {
        float totalPrice = 0;
        for (ItemOrder order : cartList) {
            totalPrice += order.calculatePrice();
        }
        place_order.setText(String.format(Locale.getDefault(), "Place Order - ₱%.2f", totalPrice));
    }

    void createToast(String message) {
        if (toast != null) {
            toast.cancel();
        }

        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}