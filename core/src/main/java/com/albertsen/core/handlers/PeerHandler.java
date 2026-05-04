package com.albertsen.core.handlers;

import com.albertsen.core.dataObjs.Peer;
import com.albertsen.core.peerdiscovery.Broadcast;
import com.albertsen.core.peerdiscovery.Listner;
import com.albertsen.core.utilFunctions.Logging;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PeerHandler {
    private final List<Peer> peers = Collections.synchronizedList(new ArrayList<>());
    private Peer profile;
    private Listner listner;
    private Broadcast broadcast;
    

    public PeerHandler(){
        //todo start broadcast + listner
        makeProfile("per");
        listner = new Listner(this);
        broadcast = new Broadcast();
    }
    public ArrayList<Peer> getPeers() {
        synchronized(peers) {
        return new ArrayList<>(peers);
        }
    }

    public void addPeer(Peer peerToBeAdded){
        synchronized (peers){
            peers.add(peerToBeAdded);
        }
    }

    public void broadcastMsg() throws IOException {
        broadcast.sendBroadcast(getProfile());
    }


    public void startListner() throws IOException {
        if(profile!=null){
            listner.startListner();
        }
    }

    public void stopLisnter(){
        listner.stopLisnter();
    }


    public void makeProfile(String name) {
        try {
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            profile = new Peer(hostAddress,name);
        } catch (UnknownHostException e) {
            Logging.log("Failed to make profile", Logging.LogLevel.error);
            throw new RuntimeException(e);
        }
    }

    public Peer getProfile(){
        if(profile == null){
            Logging.log("profile dont exsist", Logging.LogLevel.error);
            return null;
        }
        return profile;
    }


}
