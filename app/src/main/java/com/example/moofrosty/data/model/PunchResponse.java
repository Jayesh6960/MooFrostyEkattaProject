package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;

public class PunchResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("is_in")
    private boolean isIn; // Can be boolean or int (0/1) based on API, using boolean for safety if Gson handles it, otherwise use Object or int

    @SerializedName("is_out")
    private boolean isOut;

    @SerializedName("message")
    private String message;

    public String getStatus() { return status; }
    public boolean isIn() { return isIn; }
    public boolean isOut() { return isOut; }
    public String getMessage() { return message; }
}
