package handlers;

import dataObjs.Folder;
import utilFunctions.Logging;

import java.util.ArrayList;
import java.util.UUID;

public class FileHandler {
    private ArrayList<Folder> folders;

    public FileHandler() {
        //TODO get all folders

    }

    public void updateFolder(Folder folder) {
        folders.add(folder);
    }

    public ArrayList<Folder> getFolders() {
        return folders;
    }

    public Folder getFolder(UUID id) {
        for (Folder folder : folders) {
            if(folder.getID() == id){
                return folder;
            }
        }
        Logging.log("Could not update folder " + id, Logging.LogLevel.error);
        return null;
    }

}
