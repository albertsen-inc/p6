package com.albertsen.core.run;

import com.albertsen.core.dataObjs.Folder;
import com.albertsen.core.dataObjs.Peer;
import com.albertsen.core.handlers.ConnectionHandler;
import com.albertsen.core.handlers.FileHandler;
import com.albertsen.core.handlers.PeerHandler;
import com.albertsen.core.utilFunctions.Logging;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OurMain {

    private final ConnectionHandler connectionHandler;
    private final FileHandler fileHandler;
    private final ExecutorService executor =
            Executors.newCachedThreadPool();

    public OurMain() {

        connectionHandler = new ConnectionHandler();
        fileHandler = new FileHandler();
    }


    public interface SimpleCallback {
        void onComplete();

        void onError(Exception e);
    }


    public ArrayList<Peer> getPeers() {
        return connectionHandler.getPeers();
    }

    public Folder getFolder(UUID id) {
        return fileHandler.getFolder(id);
    }

    public ArrayList<Folder> getFolders() {
        return fileHandler.getFolders();
    }


    public void startConnectionServer() {

        executor.execute(() -> {

            try {

                connectionHandler.startTcpServer();


            } catch (Exception e) {
                Logging.log("failed to start tcpserver", Logging.LogLevel.error);
            }
        });
    }

    public void stopConnectionServer() {

        executor.execute(() -> {

            try {

                connectionHandler.stopTcpServer();



            } catch (Exception e) {

                Logging.log("failed to stop tcpserver", Logging.LogLevel.error);
            }
        });
    }

    public void peerInit(String userName,
                         PeerHandler.InitCallback callback) {

        executor.execute(() ->
                connectionHandler.peerinit(userName, callback)
        );
    }


    public void joinConnection(Peer peer) {

        executor.execute(() -> {

            try {

                connectionHandler
                        .joinTcpServer(peer);



            } catch (Exception e) {

                Logging.log("failed to start server", Logging.LogLevel.error);
            }
        });
    }

    public void startListningForBroadCast(
    ) {

        executor.execute(() -> {

            try {

                connectionHandler
                        .startListnerForBroadcast();



            } catch (Exception e) {
                Logging.log("failed to start broadcastListner", Logging.LogLevel.error);
            }
        });
    }

    public void sendBroadcast() {

        executor.execute(() -> {

            try {

                connectionHandler.broadCastMsg();



            } catch (Exception e) {

                Logging.log("failed to send Broadcast", Logging.LogLevel.error);
            }
        });
    }

    public void addFolder(Folder folder) {

        executor.execute(() -> {

            try {

                fileHandler.addFolder(folder);



            } catch (Exception e) {

                Logging.log("failed to add folder", Logging.LogLevel.error);
            }
        });
    }
}