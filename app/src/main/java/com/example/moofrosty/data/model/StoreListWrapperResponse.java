//package com.example.moofrosty.data.model;
//
//import com.google.gson.annotations.SerializedName;
//
//import java.util.List;
//
//public class StoreListWrapperResponse {
//
////    @SerializedName("status")
////    private boolean status;
//
//    @SerializedName("status")
//    private String status;
//
//    @SerializedName("count")
//    private int count;
//
//    @SerializedName("store")
//    private List<StoreWrapper> wrappers;
//
////    public boolean isStatus() { return status; }
//    public boolean isStatus() { return "success".equalsIgnoreCase(status); }
//    public int getCount() { return count; }
//    public List<StoreWrapper> getWrappers() { return wrappers; }
//
////    public Store[] getStores() {
////        Store[] stores = new Store[wrappers.size()];
////        for (int i = 0; i < wrappers.size(); i++) {
////            stores[i] = wrappers.get(i).getStore();
////        }
////        return stores;
////    }
//
//    public static class StoreWrapper {
//        @SerializedName("store")
//        private Store store;
//
//
//        public Store getStore() { return store; }
//    }
//}
package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class StoreListWrapperResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("count")
    private int count;

    @SerializedName("store")
    private List<Store> stores;

    public boolean isStatus() {
        return "success".equalsIgnoreCase(status);
    }

    public int getCount() {
        return count;
    }

    public List<Store> getStores() {
        return stores;
    }
}
