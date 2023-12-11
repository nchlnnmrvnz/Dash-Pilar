package com.example.dashpilar;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;
import com.dantsu.escposprinter.connection.usb.UsbConnection;
import com.dantsu.escposprinter.connection.usb.UsbPrintersConnections;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.example.dashpilar.printerconnection.AsyncBluetoothEscPosPrint;
import com.example.dashpilar.printerconnection.AsyncEscPosPrint;
import com.example.dashpilar.printerconnection.AsyncEscPosPrinter;
import com.example.dashpilar.printerconnection.AsyncUsbEscPosPrint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class Cart extends AppCompatActivity implements PriceUpdateListener {
    static ArrayList<ItemOrder> cartList = new ArrayList<>();
    static String orderNumber = "";
    Button place_order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        CartItemAdapter adapter = new CartItemAdapter(this, cartList);
        adapter.setPriceUpdateListener(this);
        recyclerView.setAdapter(adapter);

        place_order = findViewById(R.id.placeOrder);
        place_order.setOnClickListener(v -> showPrintOptionDialog());

        ImageView goBack = findViewById(R.id.back);
        goBack.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());

        onPriceUpdate();
    }

    private void showPrintOptionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Print Option");

        // Create radio buttons
        String[] options = {"USB", "Bluetooth"};
        int checkedItem = 0; // Default selected option
        builder.setSingleChoiceItems(options, checkedItem, (dialog, which) -> {
            // Handle radio button selection
            switch (which) {
                case 0:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        printUsb();
                    }
                    break;
                case 1:
                    printBluetooth();
                    break;
            }
            dialog.dismiss(); // Dismiss the dialog after selection
        });

        // Add Cancel button
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
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


    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Cart.ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
                    UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (usbManager != null && usbDevice != null) {
                            new AsyncUsbEscPosPrint(
                                    context,
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
                                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, getAsyncEscPosPrinter(new UsbConnection(usbManager, usbDevice)));
                        }
                    }
                }
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public void printUsb() {
        UsbConnection usbConnection = UsbPrintersConnections.selectFirstConnected(this);
        UsbManager usbManager = (UsbManager) this.getSystemService(Context.USB_SERVICE);

        if (usbConnection == null || usbManager == null) {
            new AlertDialog.Builder(this)
                    .setTitle("USB Connection")
                    .setMessage("No USB printer found.")
                    .show();
            return;
        }

        PendingIntent permissionIntent = PendingIntent.getBroadcast(
                this,
                0,
                new Intent(Cart.ACTION_USB_PERMISSION),
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ? PendingIntent.FLAG_MUTABLE : 0
        );
        IntentFilter filter = new IntentFilter(Cart.ACTION_USB_PERMISSION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(this.usbReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
        }
        usbManager.requestPermission(usbConnection.getDevice(), permissionIntent);
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
            itemList.append("[L]").append(cartList.get(i).getName());
            itemList.append("[R]").append(cartList.get(i).calculatePrice()).append("\n");
            itemList.append("[L]   ").append(cartList.get(i).getAddOnString()).append("\n");
            subTotal += cartList.get(i).calculatePrice();
        }

        float total = subTotal;

        return printer.addTextToPrint(
                "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.logo_with_green_text, DisplayMetrics.DENSITY_MEDIUM))+"</img>\n" +
                "[C]Dash Coffee Pilar - Las Pinas\n" +
                "[C]19 St. Joseph St., Almanza Uno,\n" +
                "[C]Las Pinas, 1750 Metro Manila\n" +
            "[L]\n" +
            "[L]Date: " + formattedDate + "\n" +
            "[L]Time: " + formattedTime + "\n" +
            "[L]Service Mode: Dine in\n" +
                "[C]\n" +
            "[L]Order No.: " + orderNumber + "\n" +
            itemList +
                "[C]\n" +
                    "[R]Subtotal: [R]₱" + subTotal + "\n" +
                    "[R]TOTAL: [R]₱" + total + "\n" +
                    "[R]Payment Method: [R]Cash\n" +
                "[C]\n" +
                "[C]Thank you for ordering!\n" +
                "[C]\n" +
                "[C]This is not an official receipt" +
                "[C]\n" +
                "[C]\n"
        );
    }


    @Override
    public void onPriceUpdate() {
        float totalPrice = 0;
        for (ItemOrder order : cartList) {
            totalPrice += order.calculatePrice();
        }
        place_order.setText(String.format(Locale.getDefault(), "Place Order - ₱%.2f", totalPrice));
    }
}