package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;

public class BatchModel {

    @SerializedName("batchId")
    public int batchId;

    @SerializedName("productId")
    public int productId;

    @SerializedName("batchNumber")
    public String batchNumber;

    @SerializedName("quantity")
    public int quantity;

    @SerializedName("mfgDate")
    public String mfgDate;

    @SerializedName("expDate")
    public String expDate;

    @SerializedName("hsn")
    public String hsn;

    @SerializedName("gstPercent")
    public String gstPercent;

    @SerializedName("cgst")
    public String cgst;

    @SerializedName("sgst")
    public String sgst;

    @SerializedName("remainingStock")
    public String remainingStock;

    // Using Integer because JSON returns "caseQty": null
    @SerializedName("caseQty")
    public Integer caseQty;

    @SerializedName("unit")
    public String unit;

//    @SerializedName("batchId")
//    public int batchId;
//    @SerializedName("mrp")
//    public String mrp;
//    @SerializedName("sellingPrice")
//    public String sellingPrice;
//    @SerializedName("margin")
//    public String margin;
//    @SerializedName("quantity")
//    public int quantity;
//    @SerializedName("caseQty")
//    public int caseQty;
//    @SerializedName("unit")
//    public String unit;
//    @SerializedName("marginPercent")
//    public String marginPercent;
//    @SerializedName("productType")
//    public String productType;
}
