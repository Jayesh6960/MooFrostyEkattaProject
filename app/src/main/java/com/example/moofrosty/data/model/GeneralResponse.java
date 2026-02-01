package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;

public class GeneralResponse {

    @SerializedName("status")
    private String status;   // "success" / "error"

    @SerializedName("message")
    private String message;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    // ✅ helper method (important)
    public boolean isSuccess() {
        return status != null && status.equalsIgnoreCase("success");
    }
}
