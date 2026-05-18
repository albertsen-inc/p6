package com.albertsen.core.handlers;

import com.albertsen.core.dataObjs.Peer;

import com.albertsen.core.utilFunctions.Logging;
import com.albertsen.core.utilFunctions.State.*;

import java.io.IOException;

import java.util.ArrayList;

public class ConnectionHandler {

    private PeerHandler peerHandler;
    private ConnectionStore connectionStore;

    private static boolean serverStarted = false;

    private TCPHandler tcpHandler;

    public ConnectionHandler(){
        peerHandler = new PeerHandler();
        connectionStore = new ConnectionStore();
        tcpHandler = new TCPHandler(peerHandler,connectionStore);
    }



    public void peerinit(String userName, PeerHandler.InitCallback callback){
        peerHandler.init(userName, callback);
    }

    public ArrayList<Peer> getPeers(){
        return peerHandler.getPeers();
    }

    public void broadCastMsg(){
        try {
            peerHandler.broadcastMsg();
        } catch (IOException e) {
            System.out.println("broadcast msg failed in connectionhandler function");
            throw new RuntimeException(e);
        }
    }

    public void startListnerForBroadcast(){
        try {
            peerHandler.startListner();
        } catch (IOException e) {
            System.out.println("broadcast msg failed in connectionhandler function");
            throw new RuntimeException(e);
        }
    }

    public synchronized void startTcpServer() {

        if (serverStarted) {
            System.out.println("Server already running - ignoring start");
            return;
        }

        serverStarted = true;

        tcpHandler.startTCPServer();
    }

    public synchronized void stopTcpServer() {

        tcpHandler.stopTCPServer();

        serverStarted = false;
    }

    public void joinTcpServer(Peer peer){
        try{
            tcpHandler.joinTCPServer(peer);
        } catch (Exception e) {
            Logging.log("Failed to join TCP server", Logging.LogLevel.error);
            throw new RuntimeException(e);
        }
    }

}
