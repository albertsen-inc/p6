package dataObjs;

public class Peer {
    private String Address;
    private String Name;
    private String ID;
    private String Key;

    public Peer(String Address, String Name, String ID, String Key) {
        this.Address = Address;
        this.Name = Name;
        this.ID = ID;
        this.Key = Key;
    }

    public String getAddress() {
        return Address;
    }

    public String getName(){
        return Name;
    }

    public String getID(){
        return ID;
    }

}
