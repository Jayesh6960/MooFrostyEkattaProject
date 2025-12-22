package com.example.moofrosty.data.model;

import java.io.Serializable;

public class Store implements Serializable {

    private String id;
    private String name;
    private String beatId; // Which area this store belongs to
    private boolean isVisited;
    private boolean isOrderTaken;
    private double orderValue;
    private String phoneNumber;
    private double lat;
    private double lng;

    private String ownerName;
    private String address;
    private String hulCode;

    public Store(String id, String name, String beatId, boolean isVisited, boolean isOrderTaken, double orderValue, String phoneNumber, double lat, double lng,
                        String address, String ownerName, String hulCode) {
        this.id = id;
        this.name = name;
        this.beatId = beatId;
        this.isVisited = isVisited;
        this.isOrderTaken = isOrderTaken;
        this.orderValue = orderValue;
        this.phoneNumber = phoneNumber;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
        this.ownerName = ownerName;
        this.hulCode = hulCode;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getBeatId() { return beatId; }
    public boolean isVisited() { return isVisited; }
    public boolean isOrderTaken() { return isOrderTaken; }
    public double getOrderValue() { return orderValue; }
    public String getPhoneNumber() { return phoneNumber; }
    public double getLat() { return lat; }
    public double getLng() { return lng; }

    public String getOwnerName() { return ownerName; }
    public String getAddress() { return address; }
    public String getHulCode() { return hulCode; }
}
