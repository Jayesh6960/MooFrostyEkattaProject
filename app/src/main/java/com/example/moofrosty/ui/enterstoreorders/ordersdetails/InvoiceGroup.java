package com.example.moofrosty.ui.enterstoreorders.ordersdetails;

import com.example.moofrosty.data.model.OrderDetailsResponse;

import java.util.List;

public class InvoiceGroup {
    private String invoiceNo;
    private List<OrderDetailsResponse.OrderItem> items;

    public InvoiceGroup(String invoiceNo, List<OrderDetailsResponse.OrderItem> items) {
        this.invoiceNo = invoiceNo;
        this.items = items;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public List<OrderDetailsResponse.OrderItem> getItems() {
        return items;
    }
}