package dataObjs;

import java.util.UUID;

public class Peer {
    private String Address;
    private String Name;
    private UUID ID;
    private String Key;

    public Peer(String Address, String Name, String Key) {
        this.Address = Address;
        this.Name = Name;
        this.ID = UUID.randomUUID();
        this.Key = Key;
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
