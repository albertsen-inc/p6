package com.albertsen.project6.ui;

import android.content.Context;
import androidx.appcompat.app.AlertDialog;

public class PopupConnectionUI {

    public interface OnActionListener {
        void onAccept();
        void onDenied();
    }

    public static void showConnectionPopup(Context context, String symbols, OnActionListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        
        builder.setTitle("Connection Code");
        builder.setMessage("Do you want to connect? \nSymbols: " + symbols);
        
        builder.setPositiveButton("Accept", (dialog, which) -> {
            if (listener != null) {
                listener.onAccept();
            }
        });
        
        builder.setNegativeButton("Denied", (dialog, which) -> {
            if (listener != null) {
                listener.onDenied();
            }
        });

        builder.setCancelable(false);
        builder.show();
    }
}
