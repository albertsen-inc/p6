package com.albertsen.project6;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.albertsen.core.dataObjs.Folder;
import com.albertsen.core.run.OurMain;
import com.albertsen.core.dataObjs.Peer;
import com.albertsen.project6.helpers.FileSetupHelper;
import com.albertsen.project6.ui.MainScreenView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private OurMain ourMain;
    private MainScreenView mainScreenView;

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

        mainScreenView = new MainScreenView(this);

        mainScreenView.setOnFindDevicesClick(() -> {
            // Mock devices for now.
            // Later replace this with real discovery logic.
            mainScreenView.addDevice(new Peer(
                    "192.168.1.101",
                    "Living Room TV",
                    "mock-key-1"
            ));

            mainScreenView.addDevice(new Peer(
                    "192.168.1.102",
                    "Office Desktop",
                    "mock-key-2"
            ));
        });

        mainScreenView.setOnOpenFileManagerClick(this::openAndroidFilePicker);

        mainScreenView.setOnSendFilesClick(() -> {
            if (selectedFiles.isEmpty()) {
                return;
            }

            // Later connect this to your real send logic.
            // Example:
            // ourMain.sendFiles(selectedFiles);
        });

        mainScreenView.addDevice(new Peer(
                "192.168.1.101",
                "Living Room TV",
                "mock-key-1"
        ));

        mainScreenView.addDevice(new Peer(
                "192.168.1.102",
                "Office Desktop",
                "mock-key-2"
        ));

        setContentView(mainScreenView);
    }

    private void openAndroidFilePicker() {
        filePickerLauncher.launch(new String[]{"*/*"});
    }
}
