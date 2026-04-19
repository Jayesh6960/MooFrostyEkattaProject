package com.example.moofrosty.ui.enterstoreorders.ordersdetails;

import com.example.moofrosty.data.model.OrderDetailsResponse;

import java.util.ArrayList;

public class InvoiceUIModel {
    public String invoiceNo;
    public ArrayList<OrderDetailsResponse.OrderItem> items;

    public InvoiceUIModel(String invoiceNo,
                          ArrayList<OrderDetailsResponse.OrderItem> items) {
        this.invoiceNo = invoiceNo;
        this.items = items;
    }
}