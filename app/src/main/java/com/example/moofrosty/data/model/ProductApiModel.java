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
//    @SerializedName("productType")
//    public String productType; // "case" or "unit" (or other)
    @SerializedName("productWeight")
    public String productWeight;
    @SerializedName("subcategory")
    public SubCategoryModel subcategory;
    @SerializedName("batches")
    public List<BatchModel> batches;

    // --- Helper Methods for Safe Parsing ---

    public double getMrpDouble() {
        if (batches != null && !batches.isEmpty()) {
            try { return Double.parseDouble(batches.get(0).mrp); } catch (Exception e) { return 0.0; }
        }
        return 0.0;
    }

    public double getSellingPriceDouble() {
        if (batches != null && !batches.isEmpty()) {
            try { return Double.parseDouble(batches.get(0).sellingPrice); } catch (Exception e) { return 0.0; }
        }
        return 0.0;
    }

    public int getStockInt() {
        if (batches != null && !batches.isEmpty()) {
            return batches.get(0).quantity;
        }
        return 0;
    }

    public int getCaseSizeInt() {
        if (batches != null && !batches.isEmpty()) {
            try {
                // If the unit field contains text like "10 pcs", parse just the number
                String unitStr = batches.get(0).unit.replaceAll("[^0-9]", "");
                if (unitStr.isEmpty()) return 1;
                return Integer.parseInt(unitStr);
            } catch (Exception e) { return 1; }
        }
        return 1;
    }

    // --- String Getters for UI (3 Decimals) ---
    public String getMrp() {
        return String.format(java.util.Locale.US, "%.3f", getMrpDouble());
    }

    public String getSellingPrice() {
        return String.format(java.util.Locale.US, "%.3f", getSellingPriceDouble());
    }
//
//    public String getMargin() {
//        if (batches != null && !batches.isEmpty()) return batches.get(0).marginPercent;
//        return "0";
//    }

    public String getMargin() {
        if (batches != null && !batches.isEmpty()) {
            BatchModel batch = batches.get(0);
            if (batch.margin != null && !batch.margin.isEmpty()) {
                return batch.margin;
            }
        }
        return "0";
    }

    public String getStock() {
        return String.valueOf(getStockInt());
    }

    public String getUnit() {
        if (batches != null && !batches.isEmpty()) return batches.get(0).unit;
        return "0";
    }

    public String getCapacity() {
        if (batches != null && !batches.isEmpty()) return batches.get(0).unit;
        return "";
    }
}
