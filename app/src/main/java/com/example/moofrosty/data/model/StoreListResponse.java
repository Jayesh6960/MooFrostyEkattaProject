package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoreListResponse {

    @SerializedName("status")
    private boolean status;

    @SerializedName("store")
    private List<StoreModel> storeList;

    public boolean isStatus() {
        return status;
    }

    public List<StoreModel> getStoreList() {
        return storeList;
    }

    public static class StoreModel {
        @SerializedName("shopId")
        private int shopId;

        @SerializedName("storeName")
        private String storeName;

        @SerializedName("ownerfullName")
        private String ownerName;

        @SerializedName("mobileNumber")
        private String mobileNumber;

        @SerializedName("address")
        private String address;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("beatId")
        private int beatId; // You might need to map this ID to a name if you have a Beat list, otherwise "Waluj Pandharpur" as placeholder

        @SerializedName("status")
        private int status; // 0=Pending, 1=Approved, 2=Rejected

        // Getters
        public int getShopId() { return shopId; }
        public String getStoreName() { return storeName; }
        public String getOwnerName() { return ownerName; }
        public String getMobileNumber() { return mobileNumber; }
        public String getAddress() { return address; }
        public String getCreatedAt() { return createdAt; }
        public int getBeatId() { return beatId; }
        public int getStatus() { return status; }
    }
}
