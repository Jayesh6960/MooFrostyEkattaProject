package com.example.moofrosty.data.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Order {
    public final String id;
    public final String orderDate;
    public final double orderValue;
    public final int totalItems;
    public final String status;
    public final String itemsBilledString;
    public final List<CartItem> items;
    public Order(String id, double orderValue, int totalItems, String status, List<CartItem> items) {
        this.id = id;
        this.orderValue = orderValue;
        this.totalItems = totalItems;
        this.status = status;
        this.items = items;
        // Create a formatted date string
        this.orderDate = new SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault()).format(new Date());
        // Create "11/11" string
        this.itemsBilledString = totalItems + "/" + totalItems;
    }
    public List<CartItem> getItems() {
        return items;
    }

    public String getId() {
        return id;
    }
}
