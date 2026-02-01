package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;

public class BatchModel {

    @SerializedName("batchId")
    public int batchId;
    @SerializedName("mrp")
    public String mrp;
    @SerializedName("sellingPrice")
    public String sellingPrice;
    @SerializedName("margin")
    public String margin;
    @SerializedName("quantity")
    public int quantity;
    @SerializedName("caseQty")
    public int caseQty;
    @SerializedName("unit")
    public String unit;
    @SerializedName("marginPercent")
    public String marginPercent;
    @SerializedName("productType")
    public String productType;
}
