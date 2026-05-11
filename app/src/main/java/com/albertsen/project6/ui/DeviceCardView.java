package com.albertsen.project6.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.albertsen.core.dataObjs.Peer;
import com.albertsen.project6.R;

public class DeviceCardView extends LinearLayout {

    private final Peer peer;

    public DeviceCardView(Context context, Peer peer, Runnable onRemoveClick) {
        super(context);

        this.peer = peer;
// ... (omitting some unchanged lines for brevity in thought, but tool call needs targetContent match)

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

    public Peer getPeer() {
        return peer;
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
        name.setText(peer.getName());
        name.setTextColor(Color.WHITE);
        name.setTextSize(17);
        name.setTypeface(Typeface.DEFAULT_BOLD);

        TextView ip = new TextView(context);
        ip.setText(peer.getAddress());
        ip.setTextColor(Color.WHITE);
        ip.setTextSize(15);

        TextView id = new TextView(context);
        id.setText(context.getString(R.string.peer_id_format, peer.getID().toString()));
        id.setTextColor(Color.WHITE);
        id.setTextSize(14);

        textArea.addView(name);
        textArea.addView(ip);
        textArea.addView(id);

        return textArea;
    }

    private Button createRemoveButton(Context context, Runnable onRemoveClick) {
        Button button = new Button(context);
        button.setText(R.string.remove_button_text);
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