package com.albertsen.project6.data;

public class Device {

    private final String name;
    private final String ipAddress;
    private final String id;

    public Device(String name, String ipAddress, String id) {
        this.name = name;
        this.ipAddress = ipAddress;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getId() {
        return id;
    }
}
