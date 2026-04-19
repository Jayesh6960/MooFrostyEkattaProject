package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OrderHistoryResponse {

    @SerializedName("status")
    public String status;

    @SerializedName("data")
    public List<OrderData> data;

//    @SerializedName("place_order_list")
//    public List<OrderData> placeOrderList;

    public static class OrderData implements Serializable {
        @SerializedName("bill_header_id")
        public int billHeaderId;

        @SerializedName("invoice_no")
        public String invoiceNo;

        @SerializedName("order_id")
        public String orderId;

        @SerializedName("shop")
        public Shop shop;

        @SerializedName("checkout_date")
        public String checkoutDate;

        @SerializedName("status")
        public int status; // 0 = Placed, 1 = Billed

        @SerializedName("current_unit")
        public String currentUnit;

        @SerializedName("order_time_unit")
        public String orderTimeUnit;


        @SerializedName("order_summary")
        public OrderSummary orderSummary;

        @SerializedName("useDetails")
        public UserDetails useDetails;


        @SerializedName("items")
        public List<OrderSummary.Item> items;

        public String getinvoiceId() {
            return invoiceNo;
        }

//        public String getOrderId() {
//            return orderId;
//        }

    }

    public static class Shop implements Serializable {
        @SerializedName("shopId")
        public int shopId;

        @SerializedName("storeName")
        public String storeName;

        @SerializedName("ownerfullName")
        public String ownerfullName;

        public String getOwnerName() {
            return ownerfullName != null ? ownerfullName : "Owner";
        }
    }

    // [HIGHLIGHT] Added UserDetails Class
    public static class UserDetails implements Serializable {
        @SerializedName("id")
        public int id;

        @SerializedName("firstName")
        public String firstName;

        @SerializedName("lastName")
        public String lastName;

        public String getFullName() {
            String fName = firstName != null ? firstName : "";
            String lName = lastName != null ? lastName : "";
            return (fName + " " + lName).trim();
        }
    }
    // Current units ==
    public static class OrderSummary implements Serializable {
        @SerializedName("total_units")
        public int totalUnits;
        @SerializedName("bill_qty")
        private int billQty;

        @SerializedName("total_basic_amount")
        public double totalBasicAmount;

        @SerializedName("total_discount")
        public double totalDiscount;

        @SerializedName("total_gst")
        public double totalGst;

        @SerializedName("total_final_amount")
        public double totalFinalAmount;

        @SerializedName("order_value")
        public double orderValue;

        @SerializedName("bill_value")
        public double billValue;

        public int getBillQty() {
            return billQty;
        }

        public void setBillQty(int billQty) {
            this.billQty = billQty;
        }

        public void setTotalFinalAmount(double totalFinalAmount) {
            this.totalFinalAmount = totalFinalAmount;
        }

        public double getTotalFinalAmount() {
            return totalFinalAmount;
        }

        public void setTotalUnits(int totalUnits) {
            this.totalUnits = totalUnits;
        }

        public int getTotalUnits() {
            return totalUnits;


        }

        public static class Item implements Serializable {
            @SerializedName("productDetails")
            public ProductDetail productDetails;

            @SerializedName("product_name")
            public String productName;

            @SerializedName("product_mrp")
            public String productMrp;

            @SerializedName("product_selling_price")
            public String productSellingPrice;

            @SerializedName("units")
            public String units; // Billed Qty

            @SerializedName("final_amount")
            public String finalAmount; // Selling Price Total

            @SerializedName("discount_amount")
            public String discountAmount;

            @SerializedName("discount_percent")
            public String discountPercent;

            @SerializedName("is_discard")
            public int isDiscard;
        }

        public static class ProductDetail implements Serializable {
            @SerializedName("productId")
            public int productId;

            @SerializedName("productImage")
            public String productImage;
        }

//    @SerializedName("status")
//    public String status;
//
//    @SerializedName("data")
//    public List<OrderData> data;
//
//    public static class OrderData implements Serializable {
//        @SerializedName("invoiceId")
//        public String invoiceId;
//
//        @SerializedName("checkout_date")
//        public String checkoutDate;
//
//        @SerializedName("total_quantity")
//        public int totalQuantity;
//
//        @SerializedName("total_case_qty")
//        public int totalCaseQty;
//
//        @SerializedName("total_amount")
//        public double totalAmount;
//
//        @SerializedName("status")
//        public int status; // 0 = Placed, 1 = Billed
//
//        @SerializedName("user")
//        public User user;
//
//        @SerializedName("items")
//        public List<Item> items;
//
//        public String getinvoiceId() {
//            return invoiceId;
//        }
//    }
//
//    public static class User implements Serializable {
//        @SerializedName("firstName")
//        public String firstName;
//
//        @SerializedName("lastName")
//        public String lastName;
//
//        public String getFullName() {
//            return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
//        }
//    }
//
//    public static class Item implements Serializable {
//        @SerializedName("quantity")
//        public int quantity; // Total units
//
//        @SerializedName("amount")
//        public String amount; // "54.81"
//
//        @SerializedName("product")
//        public ProductDetail product;
//
//        @SerializedName("batch")
//        public Batch batch;
//    }
//
//    public static class ProductDetail implements Serializable {
//        @SerializedName("productName")
//        public String productName;
//
//        @SerializedName("productImage")
//        public String productImage;
//    }
//
//    public static class Batch implements Serializable {
//        @SerializedName("mrp")
//        public String mrp;
//
//        @SerializedName("sellingPrice")
//        public String sellingPrice;
//    }
    }
}