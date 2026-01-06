package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;

public class StoreExistResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("exists")
    private boolean exists;

    @SerializedName("data")
    private StoreData data;

    public boolean isSuccess() {
        return success;
    }

    public boolean isExists() {
        return exists;
    }

    public StoreData getData() {
        return data;
    }

    // Inner class for the "data" object (storeName, etc.)
    public static class StoreData {
        @SerializedName("storeName")
        private String storeName;

        @SerializedName("RS_SSIdentifier")
        private String rsSsIdentifier;

        public String getStoreName() {
            return storeName;
        }

        public String getRsSsIdentifier() {
            return rsSsIdentifier;
        }
    }
}
