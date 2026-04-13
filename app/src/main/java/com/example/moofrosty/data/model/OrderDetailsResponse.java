package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;


public class OrderDetailsResponse implements Serializable {

    @SerializedName("status")
    public String status;

    @SerializedName("order_no")
    public String orderNo;

    @SerializedName("bill_no")
    public String billNo;

    @SerializedName("items")
    public List<Item> items;

    public static class Item implements Serializable {

        @SerializedName("productDetails")
        public ProductDetail productDetails;

        @SerializedName("product_selling_price")
        public double productSellingPrice;

        @SerializedName("units")
        public int units;

        @SerializedName("basic_amount")
        public String basicAmount;

        @SerializedName("discount_amount")
        public double discountAmount;

        @SerializedName("discount_percent")
        public int discountPercent;

        @SerializedName("final_amount")
        public String finalAmount;

        @SerializedName("remarks")
        public String remarks;

        @SerializedName("status")
        public int status;
    }

    public static class ProductDetail implements Serializable {

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


//        @SerializedName("basicAmount")
//        public String basicAmount;

        @SerializedName("gstPercent")
        public String gstPercent;
    }
}