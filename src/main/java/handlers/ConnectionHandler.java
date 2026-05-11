package handlers;

import dataObjs.Connection;
import dataObjs.Peer;

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
