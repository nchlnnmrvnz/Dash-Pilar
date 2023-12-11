package com.example.dashpilar;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dantsu.escposprinter.connection.usb.UsbConnection;
import com.dantsu.escposprinter.connection.usb.UsbPrintersConnections;

public class PrinterConnection {

    public void isBluetoothPermitted(){
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, MainActivity.PERMISSION_BLUETOOTH);
        } else if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, MainActivity.PERMISSION_BLUETOOTH_ADMIN);
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, MainActivity.PERMISSION_BLUETOOTH_CONNECT);
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, MainActivity.PERMISSION_BLUETOOTH_SCAN);
        } else {
            // Your code HERE
        }
    }

    public void isUSBPermitted() {
        final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
        final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (MainActivity.ACTION_USB_PERMISSION.equals(action)) {
                    synchronized (this) {
                        UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
                        UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                            if (usbManager != null && usbDevice != null) {
                                // YOUR PRINT CODE HERE
                            }
                        }
                    }
                }
            }

        };
    }

    public void isUSBPermitted(){
        final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
        final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (MainActivity.ACTION_USB_PERMISSION.equals(action)) {
                    synchronized (this) {
                        UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
                        UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                            if (usbManager != null && usbDevice != null) {
                                // YOUR PRINT CODE HERE
                            }
                        }
                    }
                }
            }
        };

        public void printUsb() {
            UsbConnection usbConnection = UsbPrintersConnections.selectFirstConnected(this);
            UsbManager usbManager = (UsbManager) this.getSystemService(Context.USB_SERVICE);
            if (usbConnection != null && usbManager != null) {
                PendingIntent permissionIntent = PendingIntent.getBroadcast(
                        this,
                        0,
                        new Intent(MainActivity.ACTION_USB_PERMISSION),
                        android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S ? PendingIntent.FLAG_MUTABLE : 0
                );
                IntentFilter filter = new IntentFilter(MainActivity.ACTION_USB_PERMISSION);
                registerReceiver(this.usbReceiver, filter);
                usbManager.requestPermission(usbConnection.getDevice(), permissionIntent);
            }
        }
}
