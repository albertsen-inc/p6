package com.albertsen.core.run;

import com.albertsen.core.dataObjs.Folder;
import com.albertsen.core.dataObjs.Peer;
import com.albertsen.core.handlers.ConnectionHandler;
import com.albertsen.core.handlers.FileHandler;
import com.albertsen.core.handlers.PeerHandler;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OurMain {

    private final ConnectionHandler connectionHandler;
    private final FileHandler fileHandler;

    // background thread pool
    private final ExecutorService executor =
            Executors.newCachedThreadPool();

    public OurMain() {

        // initialize normally
        connectionHandler = new ConnectionHandler();
        fileHandler = new FileHandler();
    }

    // =========================
    // CALLBACKS
    // =========================

    public interface SimpleCallback {
        void onComplete();

        void onError(Exception e);
    }

    // =========================
    // GETTERS
    // =========================

    public ArrayList<Peer> getPeers() {
        return connectionHandler.getPeers();
    }

    public Folder getFolder(UUID id) {
        return fileHandler.getFolder(id);
    }

    public ArrayList<Folder> getFolders() {
        return fileHandler.getFolders();
    }

    // =========================
    // THREAD FUNCTIONS
    // =========================

    public void stopListningTCP(SimpleCallback callback) {

        executor.execute(() -> {

            try {

                connectionHandler.tcpStopListner();

                if (callback != null) {
                    callback.onComplete();
                }

            } catch (Exception e) {

                if (callback != null) {
                    callback.onError(e);
                }
            }
        });
    }

    public void startConnectionServer(SimpleCallback callback) {

        executor.execute(() -> {

            try {

                connectionHandler.tcpServerStarter();

                if (callback != null) {
                    callback.onComplete();
                }

            } catch (Exception e) {

                if (callback != null) {
                    callback.onError(e);
                }
            }
        });
    }

    public void stopConnectionServer(SimpleCallback callback) {

        executor.execute(() -> {

            try {

                connectionHandler.tcpServerStopper();

                if (callback != null) {
                    callback.onComplete();
                }

            } catch (Exception e) {

                if (callback != null) {
                    callback.onError(e);
                }
            }
        });
    }

    public void peerInit(String userName,
                         PeerHandler.InitCallback callback) {

        executor.execute(() ->
                connectionHandler.peerinit(userName, callback)
        );
    }

    public void startTCPListenerForREQ(SimpleCallback callback) {

        executor.execute(() -> {

            try {

                connectionHandler.tcpStartListenerForREQ();

                if (callback != null) {
                    callback.onComplete();
                }

            } catch (Exception e) {

                if (callback != null) {
                    callback.onError(e);
                }
            }
        });
    }

    public void joinConnection(Peer peer,
                               SimpleCallback callback) {

        executor.execute(() -> {

            try {

                connectionHandler
                        .tcpJoinAlreadyExsistingServer(peer);

                if (callback != null) {
                    callback.onComplete();
                }

            } catch (Exception e) {

                if (callback != null) {
                    callback.onError(e);
                }
            }
        });
    }

    public void startListningForBroadCast(
            SimpleCallback callback
    ) {

        executor.execute(() -> {

            try {

                connectionHandler
                        .startListnerForBroadcast();

                if (callback != null) {
                    callback.onComplete();
                }

            } catch (Exception e) {

                if (callback != null) {
                    callback.onError(e);
                }
            }
        });
    }

    public void sendBroadcast(SimpleCallback callback) {

        executor.execute(() -> {

            try {

                connectionHandler.broadCastMsg();

                if (callback != null) {
                    callback.onComplete();
                }

            } catch (Exception e) {

                if (callback != null) {
                    callback.onError(e);
                }
            }
        });
    }

    public void addFolder(Folder folder,
                          SimpleCallback callback) {

        executor.execute(() -> {

            try {

                fileHandler.addFolder(folder);

                if (callback != null) {
                    callback.onComplete();
                }

            } catch (Exception e) {

                if (callback != null) {
                    callback.onError(e);
                }
            }
        });
    }
}