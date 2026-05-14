package com.albertsen.core.handlers;

import com.albertsen.core.dataObjs.Connection;
import com.albertsen.core.dataObjs.Peer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionStore {
    private static final Map<Peer,Connection> connectionToPeer =
            new ConcurrentHashMap<>();
    public ConnectionStore(){

    }
    public void addConnection(Connection connection){
       connectionToPeer.put(connection.getPeer(),connection);
    }
    public Connection getConnection(Peer peer){
        return connectionToPeer.get(peer);
    }
}
