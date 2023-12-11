package com.example.dashpilar.printerconnection;

import com.dantsu.escposprinter.EscPosPrinterSize;
import com.dantsu.escposprinter.connection.DeviceConnection;

public class AsyncEscPosPrinter extends EscPosPrinterSize {
    private final DeviceConnection printerConnection;
    private String[] textsToPrint = new String[0];

    public AsyncEscPosPrinter(DeviceConnection printerConnection, int printerDpi, float printerWidthMM, int printerNbrCharactersPerLine) {
        super(printerDpi, printerWidthMM, printerNbrCharactersPerLine);
        this.printerConnection = printerConnection;
    }

    public DeviceConnection getPrinterConnection() {
        return this.printerConnection;
    }

    public void setTextsToPrint(String[] textsToPrint) {
        this.textsToPrint = textsToPrint;
    }

    public AsyncEscPosPrinter addTextToPrint(String textToPrint) {
        String[] tmp = new String[this.textsToPrint.length + 1];
        System.arraycopy(this.textsToPrint, 0, tmp, 0, this.textsToPrint.length);
        tmp[this.textsToPrint.length] = textToPrint;
        this.textsToPrint = tmp;
        return this;
    }

    public String[] getTextsToPrint() {
        return this.textsToPrint;
    }
}
