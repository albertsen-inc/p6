package com.albertsen.core.peerdiscovery;
import com.albertsen.core.dataObjs.Peer;
import com.albertsen.core.handlers.PeerHandler;

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
                System.out.println("Socket stopped.");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
                isRunning.set(false);
                System.out.println("Socket closed.");
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

        String msg = new String(packet.getData(), 0, packet.getLength());
        System.out.println("Raw: " + msg);
        System.out.println("got a package");

        String[] parts = msg.split("\\|", 5);

        if (parts.length != 5) {
            System.out.println("Invalid format");
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
            System.out.println("Invalid timestamp");
            return;
        }

        if (name.isEmpty() || id.isEmpty()) return;
        if (name.length() > 50 || id.length() > 50) return;


        if (replayCache.isReplay(nonce, timestamp, ip)) {
            System.out.println("Replay detected, ignoring");
            return;
        }

        System.out.println("before if own profile");
        System.out.println(id + " = " + peerHandler.getProfile().getID() + " " + id.equals(peerHandler.getProfile().getID().toString()));
        if (id.equals(peerHandler.getProfile().getID().toString())){
            System.out.println("you Recive Own Profile");
            return;
        }
        System.out.println("after if own profile");

        System.out.println("listener printout");
        System.out.println("Peer discovered:");
        System.out.println("Name: " + name);
        System.out.println("ID: " + id);
        System.out.println("IP: " + ip);
        System.out.println("peer is beeing made now :D");
        peerHandler.addPeer(new Peer(ip,name));
    }

}
