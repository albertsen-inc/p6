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

    private final List<Peer> peers =
            Collections.synchronizedList(
                    new ArrayList<>()
            );

    private Peer profile;

    private Listner listner;
    private Broadcast broadcast;

    public void init(String userName,
                     InitCallback callback) {

        try {

            String hostAddress =
                    InetAddress
                            .getLocalHost()
                            .getHostAddress();

            profile =
                    new Peer(hostAddress, userName);

            listner = new Listner(this);
            broadcast = new Broadcast();

            Logging.log(
                    "Profile created",
                    Logging.LogLevel.info
            );

            if (callback != null) {
                callback.onSuccess(profile);
            }

        } catch (UnknownHostException e) {

            Logging.log(
                    "Failed to make profile",
                    Logging.LogLevel.error
            );

            if (callback != null) {
                callback.onError(e);
            }

        } catch (Exception e) {

            if (callback != null) {
                callback.onError(e);
            }
        }
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

    public Peer getPeer(String address) {

        synchronized (peers) {

            for (Peer peer : peers) {

                if (peer.getAddress()
                        .equals(address)) {

                    return peer;
                }
            }
        }

        return null;
    }

    public void removePeer(Peer peer) {

        synchronized (peers) {
            peers.remove(peer);
        }
    }

    public void broadcastMsg() throws IOException {
        if (broadcast != null) {
            Logging.log("Msg has been broadcast", Logging.LogLevel.info);
            broadcast.sendBroadcast(getProfile());
        }
    }

    public void startListner() throws IOException {

        if (profile != null) {

            listner.startListner();

        } else {

            Logging.log(
                    "No profile",
                    Logging.LogLevel.error
            );
        }
    }

    public void stopLisnter() {

        if (listner != null) {
            listner.stopLisnter();
        }
    }

    public Peer getProfile() {

        if (profile == null) {

            Logging.log(
                    "Profile doesn't exist",
                    Logging.LogLevel.error
            );

            return null;
        }

        return profile;
    }

    public interface InitCallback {

        void onSuccess(Peer profile);

        void onError(Exception e);
    }
}