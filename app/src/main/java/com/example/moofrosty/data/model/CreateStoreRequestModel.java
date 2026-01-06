package com.example.moofrosty.data.model;

import java.io.File;

public class CreateStoreRequestModel {

    public String ownerFullName;
    public String ownerEmail;
    public String mobileNumber;
    public String storeName;
    public String rsSsIdentifier;
    public String secondaryChannel;
    public String outletType;
    public String ssName;
    public String pincode;
    public String address;

    // These must be IDs (String representation of int)
    public String country;
    public String state;
    public String district;
    public String city;
    public String beatId;

    public String latLong;
    public String documentType;
    public String documentNumber;

    public File uploadDocument;
    public File uploadShopBoardImage;
    public File uploadShopInsideImage;
}
