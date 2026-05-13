package com.albertsen.core.dataObjs;

import javax.crypto.SecretKey;

public class Connection {


    private SecretKey Key;

    private Peer peer;

    public Connection (Peer peer, SecretKey key){
        this.peer = peer;
        this.Key = key;

    }

    public SecretKey getKey(){
        return Key;
    }

    public Peer getPeer(){return peer;}




}
