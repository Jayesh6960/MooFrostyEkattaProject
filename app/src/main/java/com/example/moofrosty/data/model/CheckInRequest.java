package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;

public class CheckInRequest {

    @SerializedName("shopId")
    private int shopId;

    @SerializedName("logDate")
    private String logDate; // Format: YYYY-MM-DD

    @SerializedName("logTime")
    private String logTime; // Format: HH:mm

    @SerializedName("storein")
    private String storeIn; // Value: "in"

    public CheckInRequest(int shopId, String logDate, String logTime, String storeIn) {
        this.shopId = shopId;
        this.logDate = logDate;
        this.logTime = logTime;
        this.storeIn = storeIn;
    }
}
