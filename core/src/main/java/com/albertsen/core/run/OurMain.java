package com.albertsen.core.run;

import com.albertsen.core.dataObjs.Folder;
import com.albertsen.core.dataObjs.Peer;
import com.albertsen.core.frontend.CommonInterface;
import com.albertsen.core.handlers.ConnectionHandler;
import com.albertsen.core.handlers.FileHandler;
import com.albertsen.core.handlers.PeerHandler;
import com.albertsen.core.peerdiscovery.Broadcast;
import com.albertsen.core.peerdiscovery.Listner;

import java.io.IOException;
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
    public static void main(String[] args) throws Exception {
        OurMain main = new OurMain();
        CommonInterface commonInterface = new CommonInterface(main);

        Peer peer = new Peer("localhost","Mathias");

        main.startTCPListenerForREQ();

        main.joinConnection(peer);
    }

    public ArrayList<Peer> getPeers(){
        return connectionHandler.getPeers();
    }

    public void stopListningTCP(){
        connectionHandler.tcpStopListner();
    }
    public void startConnectionServer(){
        connectionHandler.tcpServerStarter();
    }
    public void stopConnectionServer(){
        connectionHandler.tcpServerStopper();
    }

    public void peerInit(String userName, PeerHandler.InitCallback callback){
        connectionHandler.peerinit(userName, callback);
    }

    public void startTCPListenerForREQ() throws Exception{
        connectionHandler.tcpStartListenerForREQ();
    }
    public void joinConnection(Peer peer) throws Exception{
        connectionHandler.tcpJoinAlreadyExsistingServer(peer);
    }

    public void startListningForBroadCast(){
        connectionHandler.startListnerForBroadcast();
    }

    public void sendBroadcast(){
        connectionHandler.broadCastMsg();
    }


    public Folder getFolder(UUID id){
        return fileHandler.getFolder(id);
    }

    public ArrayList<Folder> getFolders(){
        return fileHandler.getFolders();
    }

    public void addFolder(Folder folder){
        fileHandler.addFolder(folder);
    }

}
