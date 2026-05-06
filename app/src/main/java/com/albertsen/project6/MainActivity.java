package com.albertsen.project6;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.albertsen.core.dataObjs.Folder;
import com.albertsen.core.run.OurMain;
import com.albertsen.core.dataObjs.Peer;
import com.albertsen.project6.helpers.FileSetupHelper;
import com.albertsen.project6.ui.MainScreenView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int FILE_PICKER_REQUEST_CODE = 100;

    private OurMain ourMain;
    private MainScreenView mainScreenView;

    private final ArrayList<Uri> selectedFiles = new ArrayList<>();

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
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        //TODO need to be fixed
        startActivityForResult(intent, FILE_PICKER_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != FILE_PICKER_REQUEST_CODE) {
            return;
        }

        if (resultCode != RESULT_OK || data == null) {
            return;
        }

        selectedFiles.clear();

        if (data.getClipData() != null) {
            int fileCount = data.getClipData().getItemCount();

            for (int i = 0; i < fileCount; i++) {
                Uri uri = data.getClipData().getItemAt(i).getUri();
                selectedFiles.add(uri);
            }

        } else if (data.getData() != null) {
            selectedFiles.add(data.getData());
        }

        mainScreenView.setSelectedFileCount(selectedFiles.size());
    }
}