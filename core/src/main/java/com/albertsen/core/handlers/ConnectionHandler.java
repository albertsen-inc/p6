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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.SecretKey;

public class ConnectionHandler {

    private PeerHandler peerHandler = new PeerHandler();
    private final ConnectionStore connectionStore = new ConnectionStore();
    private ServerSocket server;


    public void tcpServerStarter(){
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

    public void tcpServerStopper(){
        if (server != null){
            closeServer(server);
            ConnectionStateHandler.connectionOnGoing.set(false);
        }
    }

    public void tcpStopListner(){
        ConnectionStateHandler.closeListner = true;

    }

    public void tcpStartListenerForREQ(){
        if (!ConnectionStateHandler.connectionOnGoing.compareAndSet(false, true)) {
            System.out.println("Connection interruption because another connection ongoing");
            Logging.log(
                    "Connection interruption because another connection ongoing",
                    Logging.LogLevel.warn
            );
            return;
        }

        ConnectionStateHandler.closeListner=false;

        Thread clientThread = new Thread(()-> {

            listener();

        });

        clientThread.start();
    }


    public void listener(){
        while (!server.isClosed() || !ConnectionStateHandler.closeListner) {
            if (server != null){
                Socket clientSocket = null;
                clientSocket = acceptClient(server);
                if (clientSocket != null){
                    authenticationProtocolFromServer(clientSocket);
                }
            }

            System.out.println("hey from server");
        }
    }


    public void authenticationProtocolFromServer(Socket socketInUse){

        Authenticate authenticate = new Authenticate();

        try{
            if (socketInUse == null){
                System.out.println("accept failed");
                return;
            }
            byte[] generatedKey = authenticate.generateKey();
            sendMessage(socketInUse,generatedKey);
            byte[] peersKey = receiveBytes(socketInUse);

            if(!authenticate.checkFingerprint(peersKey)){
                return;
            }

            PublicKey publicKey  = authenticate.reciveKey(peersKey);
            System.out.println(Arrays.toString(peersKey) + "peersKey serverside");

            byte[] Secret = authenticate.generateSharedSecret(publicKey);
            SecretKey aesKey = authenticate.makeAESKey(Secret);

            connectionStore.addConnection(new Connection(peerHandler.getPeer(findIP(socketInUse)), aesKey, socketInUse));

        } catch (Exception e) {
            System.out.println("failed to generate private and public key.");
            throw new RuntimeException(e);
        }
    }


    public void tcpJoinAlreadyExsistingServer(Peer peer){
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
                authenticationProtocolFromClient(client);

            } catch (Exception e) {

                e.printStackTrace();

            }
        });
        runJoinConnection.start();
        ConnectionStateHandler.connectionOnGoing.set(false);

    }

    public void authenticationProtocolFromClient(Socket client){
        System.out.println("hej from join");
        if(client == null){
            System.out.println("connection is not availeble");
            return;
        }

        try{
            Authenticate authenticate = new Authenticate();
            byte[] generatedKey = authenticate.generateKey();
            byte[] peersKey = receiveBytes(client);
            sendMessage(client,generatedKey);

            if(!authenticate.checkFingerprint(peersKey)){
                return;
            }

            PublicKey publicKey  = authenticate.reciveKey(peersKey);

            System.out.println(peersKey + "peersKey clientside");
            byte[] Secret = authenticate.generateSharedSecret(publicKey);
            SecretKey aesKey = authenticate.makeAESKey(Secret);

            connectionStore.addConnection(new Connection(peerHandler.getPeer(findIP(client)), aesKey, client));


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void peerinit(String userName, PeerHandler.InitCallback callback){
        peerHandler.init(userName, callback);
    }

    public ArrayList<Peer> getPeers(){
        return peerHandler.getPeers();
    }

    public void broadCastMsg(){
        try {
            peerHandler.broadcastMsg();
        } catch (IOException e) {
            System.out.println("broadcast msg failed in connectionhandler function");
            throw new RuntimeException(e);
        }
    }

    public void startListnerForBroadcast(){
        try {
            peerHandler.startListner();
        } catch (IOException e) {
            System.out.println("broadcast msg failed in connectionhandler function");
            throw new RuntimeException(e);
        }
    }

}
