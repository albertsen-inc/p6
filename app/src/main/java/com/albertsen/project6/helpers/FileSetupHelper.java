package com.albertsen.project6.helpers;

import android.content.Context;

import java.io.File;

public class FileSetupHelper {

    public static File createStartFolder(Context context) {
        File folder = new File(context.getExternalFilesDir(null), "StartFolder");

        if (!folder.exists()) {
            boolean created = folder.mkdir();

            if (!created) {
                System.out.println("Could not create StartFolder");
            }
        }

        return folder;
    }

    public static void createTestFiles(File folder) {
        try {
            createFileIfMissing(folder, "file1.txt");
            createFileIfMissing(folder, "file2.txt");
            createFileIfMissing(folder, "notes.txt");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createFileIfMissing(File folder, String fileName) throws Exception {
        File file = new File(folder, fileName);

        if (!file.exists()) {
            boolean created = file.createNewFile();

            if (!created) {
                System.out.println("Could not create file: " + fileName);
            }
        }
    }
}