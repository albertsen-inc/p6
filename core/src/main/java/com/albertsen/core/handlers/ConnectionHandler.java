package com.albertsen.core.handlers;

import static com.albertsen.core.utilFunctions.TCP.*;

import com.albertsen.core.authentication.Authenticate;
import com.albertsen.core.dataObjs.Connection;
import com.albertsen.core.dataObjs.Peer;

import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;
import java.util.ArrayList;

import javax.crypto.SecretKey;

public class ConnectionHandler {
    private PeerHandler peerHandler = new PeerHandler();
    private ArrayList<Connection> connections = new ArrayList<>();


    public ConnectionHandler() {

    }

    public void startConnection() {
        Thread startConnectionThread = new Thread(() -> {

            try {
                ServerSocket server = startServer();



                Thread clientThread = new Thread(()-> {
                    while (!server.isClosed()){
                        Socket clientSocket = null;
                        if (server != null)
                            clientSocket = acceptClient(server);
                        System.out.println("hey from server");


                        Authenticate authenticate = new Authenticate();
                        System.out.println("location1");
                        try{

                            byte[] generatedKey = authenticate.generateKey();
                            if (clientSocket == null){
                                System.out.println("accept failed");
                                continue;
                            }
                            System.out.println("location2");
                            sendMessage(clientSocket,generatedKey);
                            System.out.println("location3");
                            byte[] peersKey = receiveBytes(clientSocket);
                            System.out.println("location4");
                            //CHECK FINGERPRINT

                            PublicKey publicKey  = authenticate.reciveKey(peersKey);
                            System.out.println("location5");
                            System.out.println(peersKey + "peersKey serverside");

                            byte[] Secret = authenticate.generateSharedSecret(publicKey);
                            System.out.println("location6");
                            SecretKey aesKey = authenticate.makeAESKey(Secret);
                            System.out.println("location7");

                            connections.add(new Connection(peerHandler.getPeer(findIP(clientSocket)),aesKey));
                            System.out.println("location8");

                        } catch (Exception e) {
                            System.out.println("failed to generate private and public key.");
                            throw new RuntimeException(e);
                        }


                    }
                });

                clientThread.start();

            } catch (Exception e) {
                System.out.println("server failed to start");

                e.printStackTrace();

            }

        });

        startConnectionThread.start();

    }

    public void joinConnection(Peer peer)throws Exception{

        Thread runJoinConnection = new Thread(() -> {

            try {
                Socket client = connectAsClient(peer.getAddress(), 9090);
                System.out.println("hej from join");
                if(client == null){
                    //function in frontend to let user know to click button again
                    return;
                }
                System.out.println("location10");
                Authenticate authenticate = new Authenticate();
                System.out.println("location11");
                byte[] generatedKey = authenticate.generateKey();
                System.out.println("location12");
                byte[] peersKey = receiveBytes(client);
                System.out.println("location13");
                sendMessage(client,generatedKey);

                System.out.println("location14");


                //CHECK FINGERPRINT

                PublicKey publicKey  = authenticate.reciveKey(peersKey);
                System.out.println("location15");

                System.out.println(peersKey + "peersKey clientside");
                byte[] Secret = authenticate.generateSharedSecret(publicKey);
                System.out.println("location16");
                SecretKey aesKey = authenticate.makeAESKey(Secret);
                System.out.println("location17");

                connections.add(new Connection(peer,aesKey));
                System.out.println("location18");

            } catch (Exception e) {

                e.printStackTrace();

            }

        });
        runJoinConnection.start();
    }

    public ArrayList<Peer> getPeers (){
        return peerHandler.getPeers();
    }



}
