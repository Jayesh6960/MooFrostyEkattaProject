package com.example.moofrosty.ui.enterstoreorders.ordersdetails;

import com.example.moofrosty.data.model.OrderDetailsResponse;

public class OrderDisplayItem {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;

    public int type;

    public String invoiceNo;
    public OrderDetailsResponse.OrderItem orderItem;

    // Header constructor
    public OrderDisplayItem(String invoiceNo) {
        this.type = TYPE_HEADER;
        this.invoiceNo = invoiceNo;
    }

    // Item constructor
    public OrderDisplayItem(OrderDetailsResponse.OrderItem item) {
        this.type = TYPE_ITEM;
        this.orderItem = item;
    }
}