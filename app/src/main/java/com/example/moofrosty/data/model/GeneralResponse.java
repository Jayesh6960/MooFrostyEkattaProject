package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;

public class GeneralResponse {
    @SerializedName("status")
    private String status; // "success" or "error" or boolean in some cases (handle carefully)

    @SerializedName("message")
    private String message;

    public String getStatus() { return status; }
    public String getMessage() { return message; }
}