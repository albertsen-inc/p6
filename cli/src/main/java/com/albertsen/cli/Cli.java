package com.albertsen.cli;

import com.albertsen.core.peerdiscovery.Broadcast;
import com.albertsen.core.peerdiscovery.Listner;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Cli {

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
            System.out.println("fished");
        });
        sendExecutor.shutdown();
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