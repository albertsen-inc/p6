package run;

import dataObjs.Folder;
import dataObjs.Peer;
import frontend.CommonInterface;
import handlers.ConnectionHandler;
import handlers.FileHandler;

import java.util.ArrayList;
import java.util.UUID;

public class OurMain {
    private ConnectionHandler connectionHandler;
    private FileHandler fileHandler;

    public OurMain() {
        connectionHandler = new ConnectionHandler();
        fileHandler = new FileHandler();
    }

    //interface
    public static void main(String[] args) {
        OurMain main = new OurMain();
        CommonInterface commonInterface = new CommonInterface(main);
    }

    public ArrayList<Peer> getPeers(){
        return connectionHandler.getPeers();
    }

    public void startConnection(Peer peer){
        connectionHandler.startConnection(peer);
    }

    public Folder getFolder(UUID id){
        return fileHandler.getFolder(id);
    }

    public ArrayList<Folder> getFolders(){
        return fileHandler.getFolders();
    }

}
