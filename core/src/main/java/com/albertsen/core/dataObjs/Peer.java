package com.albertsen.core.dataObjs;

import java.util.UUID;

import javax.crypto.SecretKey;

public class Peer {
    private String Address;
    private String Name;
    private UUID ID;

    public Peer(String Address, String Name) {
        this.Address = Address;
        this.Name = Name;
        this.ID = UUID.randomUUID();
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getName(){
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public UUID getID(){
        return ID;
    }

}
