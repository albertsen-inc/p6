package com.albertsen.core.handlers;

import static com.albertsen.core.utilFunctions.State.ACCEPT;
import static com.albertsen.core.utilFunctions.State.Pending;
import static com.albertsen.core.utilFunctions.TCP.*;

import com.albertsen.core.authentication.Authenticate;
import com.albertsen.core.dataObjs.Connection;
import com.albertsen.core.dataObjs.Peer;
import com.albertsen.core.utilFunctions.ConnectionStateHandler;
import com.albertsen.core.utilFunctions.Logging;
import com.albertsen.core.utilFunctions.State.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.SecretKey;

public class ConnectionHandler {
    private PeerHandler peerHandler = new PeerHandler();
    private ArrayList<Connection> connections = new ArrayList<>();
    private final Object popupLock = new Object();

    private ServerSocket server;

    public void init(){
        Thread startConnectionThread = new Thread(() -> {

            try {
                server = startServer();



            } catch (Exception e) {
                System.out.println("server failed to start");

                e.printStackTrace();

            }

        });

        startConnectionThread.start();
    }

    public void startConnection() {
        if (!ConnectionStateHandler.connectionOnGoing.compareAndSet(false, true)) {
            System.out.println("Connection interruption because another connection ongoing");
            Logging.log(
                    "Connection interruption because another connection ongoing",
                    Logging.LogLevel.warn
            );
            return;
        }


        Thread clientThread = new Thread(()-> {

        connectionsThread(server);

        });

        clientThread.start();
        ConnectionStateHandler.connectionOnGoing.set(false);

    }



    public void joinConnection(Peer peer){
        if (!ConnectionStateHandler.connectionOnGoing.compareAndSet(false, true)) {
            System.out.println("Connection interruption because another connection ongoing");
            Logging.log(
                    "Connection interruption because another connection ongoing",
                    Logging.LogLevel.warn
            );
            return;
        }

        Thread runJoinConnection = new Thread(() -> {

            try {
                Socket client = connectAsClient(peer.getAddress(), 9090);
                System.out.println("hej from join");
                if(client == null){

                    System.out.println("Server == null");
                    //function in frontend to let user know to click button again
                    return;
                }
                Authenticate authenticate = new Authenticate();
                byte[] generatedKey = authenticate.generateKey();
                byte[] peersKey = receiveBytes(client);
                sendMessage(client,generatedKey);

                MessageDigest sha =
                        MessageDigest.getInstance("SHA-256");

                byte[] receivedFingerprint =
                        sha.digest(peersKey);

                ConnectionStateHandler.setFingerprint(Arrays.toString(receivedFingerprint));



                synchronized (popupLock) {
                    while (ConnectionStateHandler.getPopupState() == Pending) {
                        try {
                            popupLock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }

                    if (ConnectionStateHandler.getPopupState() == ACCEPT) {
                        System.out.println("User accepted");
                        // continue connection
                    } else {
                        System.out.println("User denied");
                        // cancel connection
                    }
                }

                //CHECK FINGERPRINT

                PublicKey publicKey  = authenticate.reciveKey(peersKey);



                System.out.println(peersKey + "peersKey clientside");
                byte[] Secret = authenticate.generateSharedSecret(publicKey);
                SecretKey aesKey = authenticate.makeAESKey(Secret);

                connections.add(new Connection(peer,aesKey));

            } catch (Exception e) {

                e.printStackTrace();

            }
        });
        runJoinConnection.start();
        ConnectionStateHandler.connectionOnGoing.set(false);
    }


    public ArrayList<Peer> getPeers (){
        return peerHandler.getPeers();
    }

    public void connectionsThread(ServerSocket server){

            while (!server.isClosed()){
                Socket clientSocket = null;
                if (server != null)
                    clientSocket = acceptClient(server);
                System.out.println("hey from server");

                Authenticate authenticate = new Authenticate();

                try{

                    byte[] generatedKey = authenticate.generateKey();
                    if (clientSocket == null){
                        System.out.println("accept failed");
                        continue;
                    }
                    sendMessage(clientSocket,generatedKey);
                    byte[] peersKey = receiveBytes(clientSocket);

                    MessageDigest sha =
                            MessageDigest.getInstance("SHA-256");

                    byte[] receivedFingerprint =
                            sha.digest(peersKey);

                    ConnectionStateHandler.setFingerprint(Arrays.toString(receivedFingerprint));

                    synchronized (popupLock) {
                        while (ConnectionStateHandler.getPopupState() == Pending) {
                            try {
                                popupLock.wait();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                return;
                            }
                        }

                        if (ConnectionStateHandler.getPopupState() == ACCEPT) {
                            System.out.println("User accepted");
                            // continue connection
                        } else {
                            System.out.println("User denied");
                            // cancel connection
                        }
                    }

                    PublicKey publicKey  = authenticate.reciveKey(peersKey);
                    System.out.println(Arrays.toString(peersKey) + "peersKey serverside");

                    byte[] Secret = authenticate.generateSharedSecret(publicKey);
                    SecretKey aesKey = authenticate.makeAESKey(Secret);

                    connections.add(new Connection(peerHandler.getPeer(findIP(clientSocket)),aesKey));

                } catch (Exception e) {
                    System.out.println("failed to generate private and public key.");
                    throw new RuntimeException(e);
                }


            }

    }



}
