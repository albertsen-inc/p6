package com.albertsen.core.peerdiscovery;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestPeerDiscovery {

    public static ExecutorService sendExecutor = Executors.newSingleThreadExecutor();

    public static void main(String[] args){
        Listner listner = new Listner();
        Broadcast broadcastHandler = new Broadcast();

        sendExecutor.execute(()->{
            try {
                listner.startListner();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        try{
            Thread.sleep(1000);
            for (int i = 0; i < 3 ; i++) {
                broadcastHandler.sendMsgBroadcast("SUP");
                System.out.println("yo");
            }
        }catch (IOException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
