package com.albertsen.project6.handlers;

import com.albertsen.project6.dataObjs.Peer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PeerHandler {
    private List<Peer> peers = Collections.synchronizedList(new ArrayList<>());
    

    public PeerHandler(){
        //todo start broadcast + listner
    }

    public ArrayList<Peer> getPeers() {
        synchronized(peers) {
        return new ArrayList<>(peers);
        }
    }


}
