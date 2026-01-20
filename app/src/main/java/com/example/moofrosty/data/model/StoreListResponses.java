    package com.example.moofrosty.data.model;

    import com.google.gson.annotations.SerializedName;

    import java.util.List;

    public class StoreListResponses {
        @SerializedName("status")
        private boolean status;

        @SerializedName("count")
        private int count;

        @SerializedName("store")
        private List<Store> storeList;

        public boolean isStatus() { return status; }
        public int getCount() { return count; }
        public List<Store> getStoreList() { return storeList; }
    }
