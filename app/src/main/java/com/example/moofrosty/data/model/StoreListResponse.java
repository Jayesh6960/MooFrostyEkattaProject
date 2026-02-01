package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoreListResponse {

    @SerializedName("status")
    private boolean status;

    @SerializedName("store")
    private List<StoreModel> storeList;

    @SerializedName("count")
    private int count;

    public boolean isStatus() {
        return status;
    }

    public List<StoreModel> getStoreList() {
        return storeList;
    }

    public int getCount() { return count; }

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

        @SerializedName("shop_kyc")
        private StoreKyc shopKyc;

        @SerializedName("beat")
        private Beat beat;

        // Getter for KYC
        public StoreKyc getShopKyc() { return shopKyc; }
        // Getters
        public int getShopId() { return shopId; }
        public String getStoreName() { return storeName; }
        public String getOwnerName() { return ownerName; }
        public String getMobileNumber() { return mobileNumber; }
        public String getAddress() { return address; }
        public String getCreatedAt() { return createdAt; }
        public int getBeatId() { return beatId; }
        public int getStatus() { return status; }
        public Beat getBeat() { return beat; }


        public static class StoreKyc {
            @SerializedName("documentType")
            private String documentType;

            public String getDocumentType() { return documentType; }
        }

        // --- NEW Inner Class: Beat ---
        public static class Beat {
            @SerializedName("beatId")
            private int beatId;

            @SerializedName("beatNameFrom")
            private String beatNameFrom;

            @SerializedName("beatNameTo")
            private String beatNameTo;

            @SerializedName("city")
            private String city;

            @SerializedName("district")
            private String district;

            public int getBeatId() { return beatId; }
            public String getBeatNameFrom() { return beatNameFrom; }
            public String getBeatNameTo() { return beatNameTo; }
            public String getCity() { return city; }
            public String getDistrict() { return district; }
        }
    }

}
