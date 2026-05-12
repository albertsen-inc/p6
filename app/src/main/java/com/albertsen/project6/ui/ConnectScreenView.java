package com.albertsen.project6.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.albertsen.core.dataObjs.Peer;
import com.albertsen.project6.R;

import java.util.ArrayList;

public class ConnectScreenView extends ScrollView {

    private final Context context;

    private LinearLayout root;
    private LinearLayout availableDevicesContainer;
    private TextView availableDevicesTitle;

    private Runnable onBackClick;
    private Runnable onScanClick;
    private OnDeviceConnectListener onDeviceConnectListener;

    private final ArrayList<Peer> availableDevices = new ArrayList<>();

    public interface OnDeviceConnectListener {
        void onConnect(Peer peer);
    }

    public ConnectScreenView(Context context) {
        super(context);
        this.context = context;

        setBackgroundColor(Color.parseColor("#1B1D1F"));
        buildLayout();
    }

    private void buildLayout() {
        root = new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.parseColor("#1B1D1F"));

        addView(root);

        root.addView(createHeader());
        root.addView(createContent());
    }

    private LinearLayout createHeader() {
        LinearLayout header = new LinearLayout(context);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(dp(16), dp(28), dp(16), dp(20));
        header.setBackgroundColor(Color.parseColor("#0D4EDB"));

        LayoutParams headerParams = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                dp(104)
        );
        header.setLayoutParams(headerParams);

        ImageButton backButton = new ImageButton(context);
        backButton.setImageResource(R.drawable.ic_back);
        backButton.setBackgroundColor(Color.parseColor("#202225"));
        backButton.setPadding(dp(12), dp(12), dp(12), dp(12));
        backButton.setScaleType(ImageButton.ScaleType.CENTER_INSIDE);
        
        LinearLayout.LayoutParams backParams = new LinearLayout.LayoutParams(dp(48), dp(48));
        backParams.setMargins(0, 0, dp(16), 0);
        backButton.setLayoutParams(backParams);
        backButton.setOnClickListener(v -> {
            if (onBackClick != null) onBackClick.run();
        });

        LinearLayout titleContainer = new LinearLayout(context);
        titleContainer.setOrientation(LinearLayout.VERTICAL);

        TextView title = new TextView(context);
        title.setText(R.string.connect_title);
        title.setTextColor(Color.WHITE);
        title.setTextSize(24);
        title.setTypeface(Typeface.DEFAULT_BOLD);

        TextView subtitle = new TextView(context);
        subtitle.setText(R.string.connect_subtitle);
        subtitle.setTextColor(Color.WHITE);
        subtitle.setTextSize(16);

        titleContainer.addView(title);
        titleContainer.addView(subtitle);

        header.addView(backButton);
        header.addView(titleContainer);

        return header;
    }

    private LinearLayout createContent() {
        LinearLayout content = new LinearLayout(context);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(16), dp(16), dp(16), dp(16));

        content.addView(createScanButton());
        content.addView(createAvailableDevicesCard());

        return content;
    }

    private Button createScanButton() {
        Button button = new Button(context);
        button.setText(R.string.scan_button_text);
        button.setTextSize(18);
        button.setTypeface(Typeface.DEFAULT_BOLD);
        button.setTextColor(Color.WHITE);
        button.setAllCaps(false);
        button.setBackgroundColor(Color.parseColor("#191B1D"));
        button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search, 0, 0, 0);
        button.setCompoundDrawablePadding(dp(12));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(70)
        );
        params.setMargins(0, 0, 0, dp(16));
        button.setLayoutParams(params);

        button.setOnClickListener(v -> {
            if (onScanClick != null) {
                onScanClick.run();
            }
        });

        return button;
    }

    private LinearLayout createAvailableDevicesCard() {
        LinearLayout card = new LinearLayout(context);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(30), dp(26), dp(30), dp(30));
        card.setBackgroundColor(Color.parseColor("#1B1D1F"));

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        card.setLayoutParams(cardParams);

        availableDevicesTitle = new TextView(context);
        availableDevicesTitle.setTextColor(Color.WHITE);
        availableDevicesTitle.setTextSize(20);
        availableDevicesTitle.setTypeface(Typeface.DEFAULT_BOLD);

        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        titleParams.setMargins(0, 0, 0, dp(35));
        availableDevicesTitle.setLayoutParams(titleParams);

        availableDevicesContainer = new LinearLayout(context);
        availableDevicesContainer.setOrientation(LinearLayout.VERTICAL);

        card.addView(availableDevicesTitle);
        card.addView(availableDevicesContainer);

        updateAvailableDevicesTitle();

        return card;
    }

    public void addAvailableDevice(Peer peer) {
        if (isAlreadyAvailable(peer)) return;
        
        availableDevices.add(peer);
        AvailableDeviceCardView card = new AvailableDeviceCardView(context, peer, () -> {
            if (onDeviceConnectListener != null) {
                onDeviceConnectListener.onConnect(peer);
            }
        });
        availableDevicesContainer.addView(card);
        updateAvailableDevicesTitle();
    }

    private boolean isAlreadyAvailable(Peer newPeer) {
        for (Peer p : availableDevices) {
            if (p.getID().equals(newPeer.getID())) return true;
        }
        return false;
    }

    private void updateAvailableDevicesTitle() {
        availableDevicesTitle.setText(context.getString(R.string.available_devices_format, availableDevices.size()));
    }

    public void setOnBackClick(Runnable onBackClick) {
        this.onBackClick = onBackClick;
    }

    public void setOnScanClick(Runnable onScanClick) {
        this.onScanClick = onScanClick;
    }

    public void setOnDeviceConnectListener(OnDeviceConnectListener listener) {
        this.onDeviceConnectListener = listener;
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }
}
