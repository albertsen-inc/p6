package com.albertsen.project6;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.albertsen.project6.dataObjs.Folder;
import com.albertsen.project6.run.OurMain;
import com.albertsen.project6.utilFunctions.Logging;

import java.io.File;

//STARTS IT ALL ON ANDROID
public class MainActivity extends AppCompatActivity {

    private OurMain ourMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ourMain = new OurMain();

        //makes sure there is a folder (external memory, meaning only app can acces still but more space and permanent until app is uninstalled)
        File newfolder = new File(getExternalFilesDir(null), "StartFolder");

        if (!newfolder.exists()) {
            boolean created = newfolder.mkdir();
        }

        //adds the folder to our active program
        String path = newfolder.getAbsolutePath();

        ourMain.addFolder(new Folder("StartFolder",path));


        //empty txt files to have some testing for display
        try {
            File file1 = new File(newfolder, "file1.txt");
            if (!file1.exists()) {
                file1.createNewFile();
            }

            File file2 = new File(newfolder, "file2.txt");
            if (!file2.exists()) {
                file2.createNewFile();
            }

            File file3 = new File(newfolder, "notes.txt");
            if (!file3.exists()) {
                file3.createNewFile();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        //sets up a layout
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        //space between all things
        layout.setPadding(50, 50, 50, 50);

        //button made
        Button button = new Button(this);
        button.setText("Load Files");

        //text list made
        TextView textView = new TextView(this);

        //make the button not be in the top since then in middle of camera
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(0, 200, 0, 0); // left, top, right, bottom

        button.setLayoutParams(params);

        //button listner
        button.setOnClickListener(v -> {

            if (ourMain.getFolders() == null || ourMain.getFolders().isEmpty()) {
                textView.setText("No folders found");
                return;
            }

            StringBuilder sb = new StringBuilder();

            for (Folder folder : ourMain.getFolders()) {
                for (File file : folder.getFiles()) {
                    sb.append(file.getName()).append("\n");
                }
            }

            textView.setText(sb.toString());
        });

        //add to layout
        layout.addView(button);
        layout.addView(textView);

        //show layout on screen
        setContentView(layout);
    }
}