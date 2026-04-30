package com.albertsen.project6.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.albertsen.project6.data.Device;

public class DeviceCardView extends LinearLayout {

    private final Device device;

    public DeviceCardView(Context context, Device device, Runnable onRemoveClick) {
        super(context);

        this.device = device;

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setPadding(dp(16), dp(14), dp(16), dp(14));
        setBackgroundColor(Color.parseColor("#202225"));

        LayoutParams cardParams = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                dp(100)
        );
        cardParams.setMargins(0, 0, 0, dp(10));
        setLayoutParams(cardParams);

        addView(createTextArea(context));
        addView(createRemoveButton(context, onRemoveClick));
    }

    public Device getDevice() {
        return device;
    }

    private LinearLayout createTextArea(Context context) {
        LinearLayout textArea = new LinearLayout(context);
        textArea.setOrientation(VERTICAL);

        LayoutParams textAreaParams = new LayoutParams(
                0,
                LayoutParams.WRAP_CONTENT,
                1
        );
        textArea.setLayoutParams(textAreaParams);

        TextView name = new TextView(context);
        name.setText(device.getName());
        name.setTextColor(Color.WHITE);
        name.setTextSize(17);
        name.setTypeface(Typeface.DEFAULT_BOLD);

        TextView ip = new TextView(context);
        ip.setText(device.getIpAddress());
        ip.setTextColor(Color.WHITE);
        ip.setTextSize(15);

        TextView id = new TextView(context);
        id.setText("ID: " + device.getId());
        id.setTextColor(Color.WHITE);
        id.setTextSize(14);

        textArea.addView(name);
        textArea.addView(ip);
        textArea.addView(id);

        return textArea;
    }

    private Button createRemoveButton(Context context, Runnable onRemoveClick) {
        Button button = new Button(context);
        button.setText("×");
        button.setTextSize(24);
        button.setTextColor(Color.WHITE);
        button.setAllCaps(false);
        button.setBackgroundColor(Color.parseColor("#191B1D"));

        LayoutParams buttonParams = new LayoutParams(
                dp(46),
                dp(46)
        );
        button.setLayoutParams(buttonParams);

        button.setOnClickListener(v -> onRemoveClick.run());

        return button;
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }
}