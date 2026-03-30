    package com.example.moofrosty.data.model;

    import com.google.gson.annotations.SerializedName;

    import java.io.Serializable;

    public class Store implements Serializable {

        @SerializedName("shopId")
        private int shopId;

        @SerializedName("beatId")
        private int beatId;

        @SerializedName("storeName")
        private String storeName;

        @SerializedName("mobileNumber")
        private String mobileNumber;

        @SerializedName("address")
        private String address;

        @SerializedName("ownerfullName")
        private String ownerName;

        @SerializedName("owneremailId")
        private String ownerEmail;

        @SerializedName("lat_long")
        private String latLong;
        // --- Local UI State ---
        private boolean isVisited = false;
        private boolean isOrderTaken = false;
        private double orderValue = 0.0;

        // --- Getters ---
        public int getShopId() { return shopId; }
        public String getStoreName() { return storeName; }
        public String getMobileNumber() { return mobileNumber; }
//        public String getAddress() { return address; }
        public String getOwnerName() { return ownerName; }
        public String getAddress() {
            if (address != null && !address.isEmpty()) {
                String[] parts = address.split("#");
                return android.text.TextUtils.join(", ", parts);
            }
            return "";
        }

        // Helper for Lat/Lng
        public double getLat() {
            if (latLong != null && latLong.contains(",")) {
                try { return Double.parseDouble(latLong.split(",")[0].trim()); } catch (Exception e) {}
            }
            return 0.0;
        }
        public double getLng() {
            if (latLong != null && latLong.contains(",")) {
                try { return Double.parseDouble(latLong.split(",")[1].trim()); } catch (Exception e) {}
            }
            return 0.0;
        }

        // --- Setters for UI ---
        public boolean isVisited() { return isVisited; }
        public void setVisited(boolean visited) { isVisited = visited; }
        public boolean isOrderTaken() { return isOrderTaken; }
        public void setOrderTaken(boolean orderTaken) { isOrderTaken = orderTaken; }
        public double getOrderValue() { return orderValue; }
        public void setOrderValue(double orderValue) { this.orderValue = orderValue; }
    }


    //    private String id;
    //    private String name;
    //    private String beatId; // Which area this store belongs to
    //    private boolean isVisited;
    //    private boolean isOrderTaken;
    //    private double orderValue;
    //    private String phoneNumber;
    //    private double lat;
    //    private double lng;
    //
    //    private String ownerName;
    //    private String address;
    //    private String hulCode;
    //
    //    public Store(String id, String name, String beatId, boolean isVisited, boolean isOrderTaken, double orderValue, String phoneNumber, double lat, double lng,
    //                        String address, String ownerName, String hulCode) {
    //        this.id = id;
    //        this.name = name;
    //        this.beatId = beatId;
    //        this.isVisited = isVisited;
    //        this.isOrderTaken = isOrderTaken;
    //        this.orderValue = orderValue;
    //        this.phoneNumber = phoneNumber;
    //        this.lat = lat;
    //        this.lng = lng;
    //        this.address = address;
    //        this.ownerName = ownerName;
    //        this.hulCode = hulCode;
    //    }
    //
    //    public String getId() { return id; }
    //    public String getName() { return name; }
    //    public String getBeatId() { return beatId; }
    //    public boolean isVisited() { return isVisited; }
    //    public boolean isOrderTaken() { return isOrderTaken; }
    //    public double getOrderValue() { return orderValue; }
    //    public String getPhoneNumber() { return phoneNumber; }
    //    public double getLat() { return lat; }
    //    public double getLng() { return lng; }
    //
    //    public String getOwnerName() { return ownerName; }
    //    public String getAddress() { return address; }
    //    public String getHulCode() { return hulCode; }
    //}
