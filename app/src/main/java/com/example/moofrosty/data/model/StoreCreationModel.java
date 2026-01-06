package com.example.moofrosty.data.model;

public class StoreCreationModel {
    private String id;
    private String storeName;
    private String ownerName;
    private String status; // "Pending", "Approved", etc.
    private String date;

    public StoreCreationModel(String id, String storeName, String ownerName, String status, String date) {
        this.id = id;
        this.storeName = storeName;
        this.ownerName = ownerName;
        this.status = status;
        this.date = date;
    }

    public String getId() { return id; }
    public String getStoreName() { return storeName; }
    public String getOwnerName() { return ownerName; }
    public String getStatus() { return status; }
    public String getDate() { return date; }

}
