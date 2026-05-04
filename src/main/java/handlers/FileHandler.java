package handlers;

import dataObjs.Folder;
import utilFunctions.Logging;

import java.util.ArrayList;
import java.util.UUID;

public class FileHandler {
    private ArrayList<Folder> folders;

    public FileHandler() {
        //TODO get all folders
        folders = new ArrayList<>();


    }

    public void addFolder(Folder folder) {
        folders.add(folder);
    }

    public void removeFolder(Folder folder) {
        folders.remove(folder);
    }

    public void updateFolder(Folder folder) {
        folder.updateFiles();

    }

    public void updateAllFolder() {
        for(Folder f : folders){
            updateFolder(f);
        }
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
