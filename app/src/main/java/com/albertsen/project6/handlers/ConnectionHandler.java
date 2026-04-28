package com.albertsen.project6.handlers;

import com.albertsen.project6.dataObjs.Connection;
import com.albertsen.project6.dataObjs.Peer;

import java.util.ArrayList;

public class ConnectionHandler {
    private PeerHandler peerHandler = new PeerHandler();
    private ArrayList<Connection> connections = new ArrayList<>();


    public ConnectionHandler() {
    }


    public void startConnection(Peer peer){
        //todo make a connection that use authentication + connect client or host
        //connections.add(new Connection(peer));
    }

    public ArrayList<Peer> getPeers (){
        return peerHandler.getPeers();
    }



}
