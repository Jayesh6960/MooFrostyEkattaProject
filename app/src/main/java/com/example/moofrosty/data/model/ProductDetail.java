package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

// ---------------- Product Details ----------------
    public class ProductDetail implements Serializable {

        @SerializedName("productId")
        public int productId;

        @SerializedName("categoryId")
        public int categoryId;

        @SerializedName("productName")
        public String productName;

        @SerializedName("productImage")
        public String productImage;

        @SerializedName("productWeight")
        public String productWeight;

        @SerializedName("mrp")
        public String mrp;

        @SerializedName("sellingPrice")
        public String sellingPrice;

        @SerializedName("gstPercent")
        public String gstPercent;
    }
