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
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class PeerHandler {
    private final List<Peer> peers = Collections.synchronizedList(new ArrayList<>());
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Peer profile;
    private Listner listner;
    private Broadcast broadcast;


    public void init(String userName, InitCallback callback){
        executorService.execute(() -> {
            try {
                String hostAddress = InetAddress.getLocalHost().getHostAddress();
                profile = new Peer(hostAddress, userName);

                listner = new Listner(this);
                broadcast = new Broadcast();

                Logging.log("Profile created", Logging.LogLevel.info);

                if (callback != null) {
                    callback.onSuccess(profile);
                }
            } catch (UnknownHostException e) {
                Logging.log("Failed to make profile", Logging.LogLevel.error);
                if (callback != null) {
                    callback.onError(e);
                }
            }


        });
    }

    public ArrayList<Peer> getPeers() {
        synchronized (peers) {
            return new ArrayList<>(peers);
        }
    }

    public void addPeer(Peer peerToBeAdded) {
        synchronized (peers) {
            peers.add(peerToBeAdded);
        }
    }

    public void removePeer(Peer peer) {
        synchronized (peers) {
            peers.remove(peer);
        }
    }

    public void broadcastMsg() throws IOException {
        broadcast.sendBroadcast(getProfile());
    }

    public void startListner() throws IOException {
        if (profile != null) {
            listner.startListner();
        }
    }

    public void stopLisnter() {
        listner.stopLisnter();
    }

    public Peer getProfile() {
        if (profile == null) {
            Logging.log("profile dont exsist", Logging.LogLevel.error);
            return null;
        }

        return profile;
    }

    public void shutdownExecutor(){
        executorService.shutdown();
    }

    public interface InitCallback {
        void onSuccess(Peer profile);
        void onError(Exception e);
    }
}
