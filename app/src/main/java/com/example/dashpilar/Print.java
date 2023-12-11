package com.example.dashpilar;

import android.util.DisplayMetrics;

import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.dantsu.escposprinter.connection.usb.UsbConnection;
import com.dantsu.escposprinter.exceptions.EscPosConnectionException;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;

import java.util.ArrayList;

public class Print {

    public void printFormatting(EscPosPrinter printer, String orderNumber, ArrayList<ItemOrder> cartList){
        String itemList = "";
        float subTotal = 0f;
        float total = subTotal;

        for (int i = 0; i < cartList.size(); i++){
            itemList += "[L]" + cartList.get(i).getName() + "\n";
            itemList += "[L]Add ons: " + cartList.get(i).getAddOnString() + "\n";
            itemList += "[R]" + cartList.get(i).calculatePrice() + "\n";
            subTotal += cartList.get(i).calculatePrice();
        }

        printer.printFormattedText(
                "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.logo_with_green_text, DisplayMetrics.DENSITY_MEDIUM))+"</img>\n" +
                        "[L]\n" +
                        "[C]<b>Dash Coffee Pilar - Las Piñas</font></u>\n" +
                        "[C]19 St Joseph St, Almanza Uno, Las Piñas, 1750 Metro Manila" +
                        "[L]\n" +
                        "[L]\n" +
                        "[L]Date: \n" +
                        "[L]Time: \n" +
                        "[L]Service Mode: Dine in\n" +
                        "[C]\n" +
                        "[L]<b>Order No.: " + orderNumber + "\n" +
                        itemList +
                        "[C]\n" +
                        "[R]Subtotal: [R]₱" + subTotal + "\n" +
                        "[R]TOTAL: [R]₱" + total + "\n" +
                        "[R]Payment Method: [R]Cash\n" +
                        "[C]\n" +
                        "[C]Thank you for ordering\n" +
                        "[C]\n" +
                        "[C]This is not an official receipt\n"
        );
    }
    public void setConnectionType(String connectionType, String orderNumber, ArrayList<ItemOrder> cartList) throws EscPosConnectionException {

        EscPosPrinter printer;
        if (connectionType == "bluetooth"){
            printer = new EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 48f, 32);

        } else {
            printer = new EscPosPrinter(new UsbConnection(usbManager, usbDevice), 203, 48f, 32);
        }
        printFormatting(printer, orderNumber, cartList);
    }
}
