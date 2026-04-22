package handlers;

import dataObjs.Peer;

import java.util.ArrayList;

public class PeerHandler {
    private ArrayList<Peer> peers = new ArrayList();

    public PeerHandler(){
        //todo start broadcast + listner
    }

    public ArrayList<Peer> getPeers() {
        return peers;
    }


}
