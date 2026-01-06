package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;

public class StoreExistRequest {

    @SerializedName("mobileNumber")
    private String mobileNumber;

    public StoreExistRequest(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}

