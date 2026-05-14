package com.albertsen.core.dataObjs;

import java.net.Socket;

import javax.crypto.SecretKey;

public class Connection {


    private SecretKey Key;

    private Peer peer;

    private Socket socket;

    public Connection (Peer peer, SecretKey key,Socket socket){
        this.peer = peer;
        this.Key = key;
        this.socket = socket;

    }

    public SecretKey getKey(){
        return Key;
    }

    public Peer getPeer(){return peer;}

    public Socket getSocket(){
        return socket;
    }




}
