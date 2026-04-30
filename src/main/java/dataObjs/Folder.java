package dataObjs;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class Folder {
    private ArrayList<File> files;
    private String Name;
    private UUID ID; //Todo why is this here?
    private String Path;
    private LocalDateTime version;

    public Folder(String Name, String Path) {
        this.Name = Name;
        this.Path = Path;
        updateFiles();
        ID = UUID.randomUUID();

    }

    private void updateFiles(){
        //Todo finish function, make it update "files" so that it can see all files in folder

        //Todo update version if it changes.
    }
    public ArrayList<File> getFiles(){
        return files;
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
