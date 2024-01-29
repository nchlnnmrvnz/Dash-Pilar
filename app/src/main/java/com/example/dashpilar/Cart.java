package com.example.dashpilar;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

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
import androidx.appcompat.app.AlertDialog;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;


public class Cart extends AppCompatActivity implements PriceUpdateListener {
    static ArrayList<ItemOrder> cartList = new ArrayList<>();
    ArrayList<String> unavailableItems = new ArrayList<>();
    Button place_order;
    String currentOrderNumber;
    String serviceMode = null;
    String paymentMethod = null;
    CartItemAdapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    private Toast toast;
    public static Semaphore printDoneSemaphore;
    public static boolean errorFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //printDoneSemaphore = new Semaphore(1);

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

                    populateCart();
                });

        recyclerView = findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        populateCart();

        try {
            getCurrentOrderNumber();
        } catch (ExecutionException | InterruptedException e) {
            createToast("ERROR!");

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("No internet connection!");
            alertDialogBuilder.setMessage("Please check your internet connection");

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

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
                if(!allItemsAvailable()) {
                    createToast("ERROR!");

                    StringBuilder message = new StringBuilder("The following items from your cart are now out of stock:\n");

                    for(String itemName : unavailableItems)
                        message.append(" - ").append(itemName).append("\n");

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Out of Stock!");
                    builder.setMessage(message);
                    builder.setPositiveButton("Remove Unavailable Items", (dialog, which) -> {
                        removeUnavailableItems();
                        populateCart();
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    return;
                }

                if(currentOrderNumber == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Order Error!");
                    builder.setMessage("Cannot determine the last order number.");
                    builder.setPositiveButton("Retry", (dialog, which) -> place_order.performClick());

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    return;
                }

                /*try {
                    //printDoneSemaphore.acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                //printBluetooth();

                try {
                    //printDoneSemaphore.acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }*/

                if(errorFound) {
                    createToast("ERROR!");

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setTitle("Printing failed!");
                    alertDialogBuilder.setMessage("Would you like to try again or discard?");

                    alertDialogBuilder.setPositiveButton("Retry", (dialogInterface, i) -> place_order.performClick());

                    alertDialogBuilder.setNegativeButton("Discard", (dialogInterface, i) -> {
                        FirestoreSalesWriter fsw = new FirestoreSalesWriter();
                        fsw.writeToSalesCollection(currentOrderNumber, paymentMethod, serviceMode, "unprinted-sales", cartList);

                        Cart.cartList = new ArrayList<>();
                        generateNewOrderNumber();
                        getOnBackPressedDispatcher().onBackPressed();
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    //printDoneSemaphore.release();
                }
                else {

                    FirestoreSalesWriter fsw = new FirestoreSalesWriter();
                    fsw.writeToSalesCollection(currentOrderNumber, paymentMethod, serviceMode, "sales", cartList);

                    Cart.cartList = new ArrayList<>();
                    generateNewOrderNumber();
                    //printDoneSemaphore.release();
                }
            }
            else
                createToast("Cart is empty!");
        });

        ImageView goBack = findViewById(R.id.back);
        goBack.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());

        onPriceUpdate();
    }

    public void removeUnavailableItems() {
        unavailableItems = new ArrayList<>();
        for(int i = 0; i < cartList.size(); i++) {
            ItemOrder item = cartList.get(i);
            if(!Constants.unavailableItems.contains(item.getName().toUpperCase())) {

                for(int j = 0; item.getCheckedAddOns() != null &&
                        j < item.getCheckedAddOns().size(); j++) {
                    AddOn addOn = item.getCheckedAddOns().get(j);
                    if(Constants.unavailableItems
                            .contains(
                                    item.getCheckedAddOns().get(j).getName().toUpperCase())) {
                        item.getCheckedAddOns().remove(addOn);
                        --j;
                    }
                }

            }
            else {
                cartList.remove(item);
                --i;
            }

        }

        adapter.notifyDataSetChanged();
    }

    void populateCart() {
        recyclerView.removeAllViews();

        adapter = new CartItemAdapter(this, cartList);
        adapter.setPriceUpdateListener(this);
        recyclerView.setAdapter(adapter);
    }

    public boolean allItemsAvailable() {
        boolean allItemsAvailable = true;
        for(ItemOrder item : cartList) {

            if(!Constants.unavailableItems.contains(item.getName().toUpperCase())) {

                if(item.getCheckedAddOns() != null) {
                    for (AddOn addOn : item.getCheckedAddOns()) {
                        if (Constants.unavailableItems.contains(addOn.getName().toUpperCase())) {
                            allItemsAvailable = false;
                            unavailableItems.add(addOn.getName());
                        }
                    }
                }

            }
            else {
                allItemsAvailable = false;
                unavailableItems.add(item.getName());
            }

        }

        return allItemsAvailable;
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
            if(cartList.get(i).getQuantity() == 0)
                continue;

            if (cartList.get(i).getChosenDrink() != null) {
                itemList.append("[L]").append(cartList.get(i).getQuantity()).append("x ").append("Plain Combo");
            }
            else
                itemList.append("[L]").append(cartList.get(i).getQuantity()).append("x ").append(cartList.get(i).getName());

            itemList.append("[R]").append(String.format(Locale.getDefault(), "%.2f\n", cartList.get(i).calculatePrice()));

            if (cartList.get(i).getChosenDrink() != null)
                itemList.append("[L]   -").append(cartList.get(i).getChosenDrink()).append("\n");

            if (!(cartList.get(i).getSugarLevel() == -1))
                itemList.append("[L]   -").append(cartList.get(i).getSugarLevel()).append("% sugar\n");

            if (cartList.get(i).isSugarLevelSelectable())
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
                itemList + "\n" +
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

    public void getCurrentOrderNumber() throws ExecutionException, InterruptedException {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("sales")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        currentOrderNumber = task.getResult().getDocuments().get(task.getResult().getDocuments().size() - 1).getId();
                        currentOrderNumber = incrementOrderNumber(currentOrderNumber);

                        TextView orderNumberTextView = findViewById(R.id.orderNumber);
                        orderNumberTextView.setText(String.format(getResources().getString(R.string.order_no) + " %s", currentOrderNumber));
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    public String incrementOrderNumber(String currentOrderNumber) {
        String[] parts = currentOrderNumber.split("-");
        if (parts.length != 2) {
            return currentOrderNumber;
        }

        String year = parts[0];
        String orderNumber = parts[1];

        int orderNum = Integer.parseInt(orderNumber);
        orderNum++;

        String newOrderNumber = String.format("%0" + orderNumber.length() + "d", orderNum);

        return year + "-" + newOrderNumber;
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