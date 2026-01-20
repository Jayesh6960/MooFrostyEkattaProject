package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductApiModel {

    @SerializedName("productId")
    public int productId;
    @SerializedName("productName")
    public String productName;
    @SerializedName("productImage")
    public String productImage;
    @SerializedName("category")
    public CategoryModel category;
    @SerializedName("productType") // <--- ADD THIS
    public String productType;
    @SerializedName("productWeight")
    public String productWeight;
    @SerializedName("subcategory")
    public SubCategoryModel subcategory;
    @SerializedName("batches")
    public List<BatchModel> batches;

    // Helper to get price from the first batch
    public String getMrp() {
        if (batches != null && !batches.isEmpty()) return batches.get(0).mrp;
        return "0";
    }
    public String getSellingPrice() {
        if (batches != null && !batches.isEmpty()) return batches.get(0).sellingPrice;
        return "0";
    }
    public String getMargin() {
        if (batches != null && !batches.isEmpty()) return batches.get(0).margin;
        return "0";
    }
    public String getStock() {
        if (batches != null && !batches.isEmpty()) return String.valueOf(batches.get(0).quantity);
        return "0";
    }
    public String getCapacity() {
        // Assuming productWeight or unit represents capacity
        // You might need to adjust this based on your logic
        if (batches != null && !batches.isEmpty()) return batches.get(0).unit;
        return "";
    }
    public String getUnit() {
        // Maps to "unit": "6" (Case Size) or "unit": "50" from your JSON
        if (batches != null && !batches.isEmpty()) return batches.get(0).unit;
        return "0";
    }
}
