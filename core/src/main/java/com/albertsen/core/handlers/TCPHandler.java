package com.albertsen.core.handlers;

import static com.albertsen.core.utilFunctions.TCP.acceptClient;
import static com.albertsen.core.utilFunctions.TCP.closeClient;
import static com.albertsen.core.utilFunctions.TCP.closeServer;
import static com.albertsen.core.utilFunctions.TCP.connectAsClient;
import static com.albertsen.core.utilFunctions.TCP.findIP;
import static com.albertsen.core.utilFunctions.TCP.receiveBytes;
import static com.albertsen.core.utilFunctions.TCP.sendBytes;
import static com.albertsen.core.utilFunctions.TCP.startServer;

import com.albertsen.core.authentication.Authenticate;
import com.albertsen.core.dataObjs.Connection;
import com.albertsen.core.dataObjs.Peer;

import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Arrays;

import javax.crypto.SecretKey;

public class TCPHandler {

    private static final int PORT = 10000;

    private static ServerSocket server;
    private static boolean running = false;

    private final PeerHandler peerHandler;
    private final ConnectionStore connectionStore;

    public TCPHandler(PeerHandler peerHandler,ConnectionStore connectionStore) {
        this.connectionStore = connectionStore;
        this.peerHandler = peerHandler;
    }

    public void startTCPServer() {

        try {

            server = startServer(PORT);

            if (server == null) {

                System.out.println("Failed to start server");

                return;
            }

            running = true;

            Thread listenerThread = new Thread(this::listener);

            listenerThread.start();

            System.out.println("TCP Server listening...");

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public synchronized void stopTCPServer() {

        running = false;

        try {

            if (server != null) {

                server.close();
                server = null;

            }

            System.out.println("Server stopped");

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    private void listener() {

        while (running) {

            try {

                Socket client = server.accept();

                new Thread(() ->
                        authenticationProtocolFromServer(client)
                ).start();

            } catch (Exception e) {

                if (running) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void joinTCPServer(Peer peer) {

        Thread joinThread = new Thread(() -> {

            try {

                Socket socket =
                        connectAsClient(peer.getAddress(), PORT);

                if (socket == null) {

                    System.out.println("Connection failed");

                    return;
                }

                authenticationProtocolFromClient(socket);

            } catch (Exception e) {

                e.printStackTrace();

            }

        });

        joinThread.start();
    }


    public void authenticationProtocolFromServer(Socket socket) {

        try {

            Authenticate authenticate = new Authenticate();

            byte[] generatedKey =
                    authenticate.generateKey();

            sendBytes(socket, generatedKey);

            byte[] peerKey =
                    receiveBytes(socket);

            System.out.println(
                    Arrays.toString(peerKey)
            );


            if (!authenticate.checkFingerprint(peerKey)) {

                System.out.println("Fingerprint check failed");

                closeClient(socket);

                return;
            }

            PublicKey publicKey =
                    authenticate.reciveKey(peerKey);

            byte[] secret =
                    authenticate.generateSharedSecret(publicKey);

            SecretKey aesKey =
                    authenticate.makeAESKey(secret);


            connectionStore.addConnection(
                    new Connection(
                            peerHandler.getPeer(findIP(socket)),
                            aesKey,
                            socket
                    )
            );

            System.out.println("Authenticated client: "
                    + findIP(socket));

        } catch (Exception e) {

            e.printStackTrace();

            closeClient(socket);
        }
    }


    public void authenticationProtocolFromClient(Socket socket) {

        try {

            Authenticate authenticate = new Authenticate();


            byte[] generatedKey =
                    authenticate.generateKey();


            byte[] peerKey =
                    receiveBytes(socket);

            sendBytes(socket, generatedKey);

            System.out.println(
                    Arrays.toString(peerKey)
            );


            if (!authenticate.checkFingerprint(peerKey)) {

                System.out.println("Fingerprint failed");

                closeClient(socket);

                return;
            }

            PublicKey publicKey =
                    authenticate.reciveKey(peerKey);


            byte[] secret =
                    authenticate.generateSharedSecret(publicKey);

            SecretKey aesKey =
                    authenticate.makeAESKey(secret);


            connectionStore.addConnection(
                    new Connection(
                            peerHandler.getPeer(findIP(socket)),
                            aesKey,
                            socket
                    )
            );

            System.out.println("Connected and authenticated");

        } catch (Exception e) {

            e.printStackTrace();

            closeClient(socket);
        }
    }
}