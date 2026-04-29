package com.albertsen.core.dataObjs;

import com.albertsen.core.utilFunctions.Logging;

import java.io.File;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Folder {
    private ConcurrentHashMap<String,File> files;
    private String Name;
    private UUID ID;
    private String Path;
    private LocalDateTime version;

    public Folder(String Name, String Path) {
        this.Name = Name;
        this.Path = Path;
        files = new ConcurrentHashMap<>();
        ID = UUID.randomUUID();
        updateFiles();

    }

    public void updateFiles(){
        files.clear();
        scanFiles(Path);
        version = LocalDateTime.ofInstant(Instant.ofEpochMilli(new File(Path).lastModified()),
                TimeZone.getDefault().toZoneId());
    }

    private void scanFiles(String path){
        File directory = new File(path);

        String [] directoryContents = directory.list();

        for(String fileName: directoryContents) {
            File temp = new File(String.valueOf(directory),fileName);
            if (temp.isDirectory()) {
                scanFiles(temp.getAbsolutePath());
            }else if (Files.isSymbolicLink(temp.toPath())) {
                Logging.log("SymbolicLink not supported", Logging.LogLevel.info);
                    continue;
            } else{
                files.put(temp.getPath(),temp);
            }
        }
    }

    public ArrayList<File> getFiles(){
        return new ArrayList<>(files.values());
    }
    public String getName(){
        return Name;
    }
    public UUID getID(){
        return ID;
    }
    public String getPath(){
        return Path;
    }
    public String getVersion(){
        return version.toString();
    }


}
