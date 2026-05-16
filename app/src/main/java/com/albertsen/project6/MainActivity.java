package com.albertsen.project6;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.albertsen.core.dataObjs.Folder;
import com.albertsen.core.handlers.ConnectionHandler;
import com.albertsen.core.handlers.PeerHandler;
import com.albertsen.core.run.OurMain;
import com.albertsen.core.dataObjs.Peer;
import com.albertsen.core.utilFunctions.Logging;
import com.albertsen.core.utilFunctions.ConnectionStateHandler;
import com.albertsen.project6.helpers.FileSetupHelper;
import com.albertsen.core.utilFunctions.State;
import com.albertsen.project6.ui.ConnectScreenView;
import com.albertsen.project6.ui.MainScreenView;
import com.albertsen.project6.ui.PopupConnectionUI;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private OurMain ourMain;
    private MainScreenView mainScreenView;
    private ConnectScreenView connectScreenView;

    private final ArrayList<Uri> selectedFiles = new ArrayList<>();

    private final ActivityResultLauncher<String[]> filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.OpenMultipleDocuments(),
            uris -> {
                selectedFiles.clear();
                if (uris != null) {
                    selectedFiles.addAll(uris);
                }
                mainScreenView.setSelectedFileCount(selectedFiles.size());
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ourMain = new OurMain();




        File startFolder = FileSetupHelper.createStartFolder(this);
        FileSetupHelper.createTestFiles(startFolder);

        ourMain.addFolder(new Folder("StartFolder", startFolder.getAbsolutePath()));


        ourMain.peerInit("per", new PeerHandler.InitCallback() {
            @Override
            public void onSuccess(Peer profile) {
                runOnUiThread(() -> {
                    initMainScreen();
                    initConnectScreen();

                    setContentView(mainScreenView);
                });

                ourMain.startListningForBroadCast();
            }

            @Override
            public void onError(Exception e) {
                Logging.log("failed to init peer handler", Logging.LogLevel.error);
            }
        });
    }

    private void initMainScreen() {
        mainScreenView = new MainScreenView(this);

        mainScreenView.setOnFindDevicesClick(() -> {
            setContentView(connectScreenView);
        });

        mainScreenView.setOnOpenFileManagerClick(this::openAndroidFilePicker);

        mainScreenView.setOnSendFilesClick(() -> {
            if (selectedFiles.isEmpty()) {
                return;
            }
            // Real send logic later
        });

        // Some initial connected devices as mocks
        mainScreenView.addDevice(new Peer("192.168.1.101", "Living Room TV"));
        mainScreenView.addDevice(new Peer("192.168.1.102", "Office Desktop"));
    }

    private void initConnectScreen() {
        connectScreenView = new ConnectScreenView(this);
        boolean tcpListener = false;
        connectScreenView.setOnBackClick(() -> {
            setContentView(mainScreenView);
        });

        ourMain.startConnectionServer();

        connectScreenView.setOnScanClick(() -> {

        });

        connectScreenView.setBroadcastClick(() -> {
            System.out.println("before broadcast msg"+ourMain.getPeers().size());
            ourMain.sendBroadcast();
            System.out.println("after broadcast msg"+ourMain.getPeers().size());

        });

        connectScreenView.setJoinServerClick(() -> {

        });

        connectScreenView.setStartServerClick(() -> {

        });

        connectScreenView.setOnStartListenerClick(() -> {
            ourMain.startListningForBroadCast();
        });

        connectScreenView.setOnDeviceConnectListener(peer -> {
            PopupConnectionUI.showConnectionPopup(this, ConnectionStateHandler.getFingerprint(), new PopupConnectionUI.OnActionListener() {
                @Override
                public void onAccept() {
                    mainScreenView.addDevice(peer);
                    ConnectionStateHandler.setPopupState(State.ACCEPT);
                    setContentView(mainScreenView);
                }

                @Override
                public void onDenied() {
                    ConnectionStateHandler.setPopupState(State.DENIED);
                }
            });
        });
    }

    private void openAndroidFilePicker() {
        filePickerLauncher.launch(new String[]{"*/*"});
    }
}
