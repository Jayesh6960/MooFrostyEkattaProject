package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderHistoryResponse {

    @SerializedName("status")
    public String status;

    @SerializedName("data")
    public List<OrderData> data;

    public static class OrderData {
        @SerializedName("invoiceId")
        public String invoiceId;

        @SerializedName("checkout_date")
        public String checkoutDate;

        @SerializedName("total_quantity")
        public int totalQuantity; // Total units

        @SerializedName("total_case_qty")
        public int totalCaseQty;

        @SerializedName("total_amount")
        public double totalAmount;

        @SerializedName("status")
        public int status;

        // You can parse items if needed for detail screen, but for list, this is enough
        @SerializedName("items")
        public List<ItemData> items;
    }

    public static class ItemData {
        // We only need this if you click to see details later
        @SerializedName("quantity")
        public int quantity;
    }
}
