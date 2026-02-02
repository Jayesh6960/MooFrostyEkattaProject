package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoreListWrapperResponse {

    @SerializedName("status")
    private boolean status;

    @SerializedName("count")
    private int count;

    @SerializedName("store")
    private List<StoreWrapper> wrappers;

    public boolean isStatus() { return status; }
    public int getCount() { return count; }
    public List<StoreWrapper> getWrappers() { return wrappers; }

    public static class StoreWrapper {
        @SerializedName("store")
        private Store store;

        public Store getStore() { return store; }
    }
}
