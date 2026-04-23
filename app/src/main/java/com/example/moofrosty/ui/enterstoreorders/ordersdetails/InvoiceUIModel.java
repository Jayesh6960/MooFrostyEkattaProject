package com.example.moofrosty.ui.enterstoreorders.ordersdetails;

import com.example.moofrosty.data.model.OrderDetailsResponse;

public class InvoiceUIModel {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;

    private int type;
    private String invoiceNo;
    private OrderDetailsResponse.OrderItem item;

    // Header
    public InvoiceUIModel(String invoiceNo) {
        this.type = TYPE_HEADER;
        this.invoiceNo = invoiceNo;
    }

    // Item
    public InvoiceUIModel(OrderDetailsResponse.OrderItem item) {
        this.type = TYPE_ITEM;
        this.item = item;
    }

    public int getType() { return type; }
    public String getInvoiceNo() { return invoiceNo; }
    public OrderDetailsResponse.OrderItem getItem() { return item; }
}