package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    // Flattened fields (Directly in root)
    @SerializedName("token")
    private String token;

    @SerializedName("name")
    private String name;

    public boolean isStatus() { return status; }
    public String getMessage() { return message; }
    public String getToken() { return token; }
    public String getName() { return name; }
}
