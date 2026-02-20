package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CheckoutRequest {

    @SerializedName("user_id")
    private int userId;

    @SerializedName("shopId")
    private int shopId;

    @SerializedName("checkout_date")
    private String checkoutDate;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(String checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public List<CheckoutItem> getItems() {
        return items;
    }

    public void setItems(List<CheckoutItem> items) {
        this.items = items;
    }

    @SerializedName("items")
    private List<CheckoutItem> items;

    public CheckoutRequest(int userId, int shopId, String checkoutDate, List<CheckoutItem> items) {
        this.userId = userId;
        this.shopId = shopId;
        this.checkoutDate = checkoutDate;
        this.items = items;
    }

    public static class CheckoutItem {
        @SerializedName("productid")
        private int productId;

        @SerializedName("case_qty")
        private int caseQty;

        public CheckoutItem(int productId, int caseQty) {
            this.productId = productId;
            this.caseQty = caseQty; // Note: API seems to only ask for case_qty, ensure this covers total qty logic
        }
    }

}
