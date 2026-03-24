package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;

public class UserStatusResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private UserData data;

    public UserData getData() {
        return data;
    }

    public static class UserData {

        @SerializedName("id")
        private int id;

        @SerializedName("status")
        private String status;

        public String getStatus() {
            return status;
        }
    }
}