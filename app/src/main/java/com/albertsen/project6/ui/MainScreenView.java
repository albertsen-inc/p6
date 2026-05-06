package com.albertsen.project6.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.albertsen.core.dataObjs.Peer;
import com.albertsen.project6.R;

import java.util.ArrayList;

public class MainScreenView extends ScrollView {

    private final Context context;

    private LinearLayout root;
    private LinearLayout connectedDevicesContainer;
    private TextView connectedDevicesTitle;
    private Button sendFilesButton;

    private Runnable onFindDevicesClick;
    private Runnable onOpenFileManagerClick;
    private Runnable onSendFilesClick;

    private int selectedFileCount = 0;

    private final ArrayList<Peer> connectedDevices = new ArrayList<>();

    public MainScreenView(Context context) {
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
        header.setOrientation(LinearLayout.VERTICAL);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(dp(20), dp(28), dp(20), dp(20));
        header.setBackgroundColor(Color.parseColor("#0D4EDB"));

        LayoutParams headerParams = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                dp(104)
        );
        header.setLayoutParams(headerParams);

        TextView title = new TextView(context);
        title.setText(R.string.title_name);
        title.setTextColor(Color.WHITE);
        title.setTextSize(24);
        title.setTypeface(Typeface.DEFAULT_BOLD);

        TextView subtitle = new TextView(context);
        subtitle.setText(R.string.subtitle_name);
        subtitle.setTextColor(Color.WHITE);
        subtitle.setTextSize(16);

        header.addView(title);
        header.addView(subtitle);

        return header;
    }

    private LinearLayout createContent() {
        LinearLayout content = new LinearLayout(context);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(16), dp(16), dp(16), dp(16));

        content.addView(createFindDevicesButton());
        content.addView(createConnectedDevicesCard());
        content.addView(createOpenFileManagerButton());
        content.addView(createSendFilesButton());

        return content;
    }

    private Button createFindDevicesButton() {
        Button button = new Button(context);
        button.setText(R.string.button_find_connect_text);
        button.setTextSize(18);
        button.setTypeface(Typeface.DEFAULT_BOLD);
        button.setTextColor(Color.WHITE);
        button.setAllCaps(false);
        button.setBackgroundColor(Color.parseColor("#191B1D"));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(70)
        );
        params.setMargins(0, 0, 0, dp(16));
        button.setLayoutParams(params);

        button.setOnClickListener(v -> {
            if (onFindDevicesClick != null) {
                onFindDevicesClick.run();
            }
        });

        return button;
    }

    private LinearLayout createConnectedDevicesCard() {
        LinearLayout card = new LinearLayout(context);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(30), dp(26), dp(30), dp(30));
        card.setBackgroundColor(Color.parseColor("#1B1D1F"));

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, dp(26));
        card.setLayoutParams(cardParams);

        connectedDevicesTitle = new TextView(context);
        connectedDevicesTitle.setTextColor(Color.WHITE);
        connectedDevicesTitle.setTextSize(20);
        connectedDevicesTitle.setTypeface(Typeface.DEFAULT_BOLD);

        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        titleParams.setMargins(0, 0, 0, dp(35));
        connectedDevicesTitle.setLayoutParams(titleParams);

        connectedDevicesContainer = new LinearLayout(context);
        connectedDevicesContainer.setOrientation(LinearLayout.VERTICAL);

        card.addView(connectedDevicesTitle);
        card.addView(connectedDevicesContainer);

        updateConnectedDevicesTitle();

        return card;
    }

    private Button createOpenFileManagerButton() {
        Button button = new Button(context);
        button.setText(R.string.button_file_manager_text);
        button.setTextSize(18);
        button.setTypeface(Typeface.DEFAULT_BOLD);
        button.setTextColor(Color.WHITE);
        button.setAllCaps(false);
        button.setBackgroundColor(Color.parseColor("#1B1D1F"));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(70)
        );
        params.setMargins(0, 0, 0, dp(18));
        button.setLayoutParams(params);

        button.setOnClickListener(v -> {
            if (onOpenFileManagerClick != null) {
                onOpenFileManagerClick.run();
            }
        });

        return button;
    }

    private Button createSendFilesButton() {
        sendFilesButton = new Button(context);
        sendFilesButton.setTextSize(18);
        sendFilesButton.setTypeface(Typeface.DEFAULT_BOLD);
        sendFilesButton.setAllCaps(false);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(70)
        );
        sendFilesButton.setLayoutParams(params);

        sendFilesButton.setOnClickListener(v -> {
            if (onSendFilesClick != null) {
                onSendFilesClick.run();
            }
        });

        updateSendFilesButton();

        return sendFilesButton;
    }

    public void addDevice(Peer peer) {
        if (isDeviceAlreadyConnected(peer)) {
            return;
        }

        connectedDevices.add(peer);

        DeviceCardView deviceCardView = new DeviceCardView(
                context,
                peer,
                () -> removeDevice(peer)
        );

        connectedDevicesContainer.addView(deviceCardView);
        updateConnectedDevicesTitle();
    }

    public void removeDevice(Peer peer) {
        connectedDevices.remove(peer);
        redrawDeviceCards();
        updateConnectedDevicesTitle();
    }

    private void redrawDeviceCards() {
        connectedDevicesContainer.removeAllViews();

        for (Peer peer : connectedDevices) {
            DeviceCardView deviceCardView = new DeviceCardView(
                    context,
                    peer,
                    () -> removeDevice(peer)
            );

            connectedDevicesContainer.addView(deviceCardView);
        }
    }

    private boolean isDeviceAlreadyConnected(Peer newPeer) {
        for (Peer peer : connectedDevices) {
            if (peer.getID().equals(newPeer.getID())) {
                return true;
            }
        }

        return false;
    }

    public void setSelectedFileCount(int selectedFileCount) {
        this.selectedFileCount = selectedFileCount;
        updateSendFilesButton();
    }

    private void updateConnectedDevicesTitle() {
        connectedDevicesTitle.setText(
                context.getString(R.string.connected_devices_format, connectedDevices.size())
        );
    }

    private void updateSendFilesButton() {
        sendFilesButton.setText(context.getString(R.string.send_files_format, selectedFileCount));

        if (selectedFileCount > 0) {
            sendFilesButton.setEnabled(true);
            sendFilesButton.setTextColor(Color.WHITE);
            sendFilesButton.setBackgroundColor(Color.parseColor("#0D4EDB"));
        } else {
            sendFilesButton.setEnabled(false);
            sendFilesButton.setTextColor(Color.parseColor("#888888"));
            sendFilesButton.setBackgroundColor(Color.parseColor("#1B1D1F"));
        }
    }

    public void setOnFindDevicesClick(Runnable onFindDevicesClick) {
        this.onFindDevicesClick = onFindDevicesClick;
    }

    public void setOnOpenFileManagerClick(Runnable onOpenFileManagerClick) {
        this.onOpenFileManagerClick = onOpenFileManagerClick;
    }

    public void setOnSendFilesClick(Runnable onSendFilesClick) {
        this.onSendFilesClick = onSendFilesClick;
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }
}
