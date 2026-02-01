package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OrderHistoryResponse {

    @SerializedName("status")
    public String status;

    @SerializedName("data")
    public List<OrderData> data;

    public static class OrderData implements Serializable {
        @SerializedName("invoiceId")
        public String invoiceId;

        @SerializedName("checkout_date")
        public String checkoutDate;

        @SerializedName("total_quantity")
        public int totalQuantity;

        @SerializedName("total_case_qty")
        public int totalCaseQty;

        @SerializedName("total_amount")
        public double totalAmount;

        @SerializedName("status")
        public int status; // 0 = Placed, 1 = Billed

        @SerializedName("user")
        public User user;

        @SerializedName("items")
        public List<Item> items;
    }

    public static class User implements Serializable {
        @SerializedName("firstName")
        public String firstName;

        @SerializedName("lastName")
        public String lastName;

        public String getFullName() {
            return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
        }
    }

    public static class Item implements Serializable {
        @SerializedName("quantity")
        public int quantity; // Total units

        @SerializedName("amount")
        public String amount; // "54.81"

        @SerializedName("product")
        public ProductDetail product;

        @SerializedName("batch")
        public Batch batch;
    }

    public static class ProductDetail implements Serializable {
        @SerializedName("productName")
        public String productName;

        @SerializedName("productImage")
        public String productImage;
    }

    public static class Batch implements Serializable {
        @SerializedName("mrp")
        public String mrp;

        @SerializedName("sellingPrice")
        public String sellingPrice;
    }
}
