package com.albertsen.core.peerdiscovery;
import com.albertsen.core.dataObjs.Peer;
import com.albertsen.core.handlers.PeerHandler;
import com.albertsen.core.utilFunctions.Logging;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;

public class Listner {

    private static final AtomicBoolean isRunning = new AtomicBoolean(false);
    private volatile DatagramSocket socket;
    private final ReplayCache replayCache = new ReplayCache();
    private PeerHandler peerHandler;

    public Listner(PeerHandler peerHandler){
        this.peerHandler = peerHandler;
    }

    public void startListner() throws IOException {

        if (!isRunning.compareAndSet(false, true)) {
            return;
        }

        Thread thread = new Thread(() -> {
            try {
                socket = new DatagramSocket(8888);
                while (isRunning.get()) {
                    listner(socket);
                }

            } catch (SocketException e) {
                Logging.log("Socket stopped.", Logging.LogLevel.info);
            } catch (IOException e) {
                Logging.log(e.getMessage(), Logging.LogLevel.error);
            } finally {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
                isRunning.set(false);
                Logging.log("Socket closed.", Logging.LogLevel.info);
            }
        });

        thread.start();

    }

    public void stopLisnter(){
        if (isRunning.compareAndSet(true, false)) {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }


    public void listner(DatagramSocket socket) throws IOException {

        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        socket.receive(packet);
        Logging.log("got a package", Logging.LogLevel.info);
        String msg = new String(packet.getData(), 0, packet.getLength());
        Logging.log("Raw: " + msg, Logging.LogLevel.info);

        String[] parts = msg.split("\\|", 5);

        if (parts.length != 5) {
            Logging.log("Invalid format", Logging.LogLevel.warn);
            return;
        }

        String type = parts[0];
        String name = parts[1];
        String id = parts[2];
        long timestamp;
        String nonce = parts[4];
        String ip = packet.getAddress().toString();

        if (!"DISCOVER".equals(type)) return;

        try {
            timestamp = Long.parseLong(parts[3]);
        } catch (NumberFormatException e) {
            Logging.log("Invalid timestamp", Logging.LogLevel.warn);
            return;
        }

        if (name.isEmpty() || id.isEmpty()) return;
        if (name.length() > 50 || id.length() > 50) return;


        if (replayCache.isReplay(nonce, timestamp, ip)) {
            Logging.log("Replay detected, ignoring", Logging.LogLevel.info);
            return;
        }


        if (id.equals(peerHandler.getProfile().getID().toString())){
            Logging.log("you Recive Own Profile", Logging.LogLevel.info);
            return;
        }

        Logging.log("listener printout", Logging.LogLevel.info);
        Logging.log("Peer discovered:", Logging.LogLevel.info);
        Logging.log("Name: " + name, Logging.LogLevel.info);
        Logging.log("ID: " + id, Logging.LogLevel.info);
        Logging.log("IP: " + ip, Logging.LogLevel.info);
        Logging.log("peer is beeing made now :D", Logging.LogLevel.info);
        peerHandler.addPeer(new Peer(ip,name));
    }

}
