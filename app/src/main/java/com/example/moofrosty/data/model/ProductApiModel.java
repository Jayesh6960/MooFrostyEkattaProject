package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductApiModel {

    @SerializedName("productId")
    public int productId;

    @SerializedName("categoryId")
    public int categoryId;

    @SerializedName("productName")
    public String productName;

    @SerializedName("productImage")
    public String productImage;

    @SerializedName("description")
    public String description;

    @SerializedName("flavour")
    public String flavour;

    @SerializedName("productWeight")
    public String productWeight;

    @SerializedName("minSellingPrice")
    public String minSellingPrice;

    @SerializedName("maxSellingPrice")
    public String maxSellingPrice;

    @SerializedName("mrp")
    public String mrp;

    @SerializedName("margin")
    public String margin;

    @SerializedName("sellingPrice")
    public String sellingPrice;

    @SerializedName("productType")
    public String productType;

    @SerializedName("status")
    public String status;

    @SerializedName("created_at")
    public String createdAt;

    @SerializedName("updated_at")
    public String updatedAt;

    @SerializedName("deleted_at")
    public String deletedAt;

    @SerializedName("total_remaining_quantity")
    public int totalRemainingQuantity;

    // Use Integer because the JSON returns "null" here sometimes
    @SerializedName("total_remaining_case_quantity")
    public Integer totalRemainingCaseQuantity;

    @SerializedName("category")
    public CategoryModel category;

    // Keeping subcategory so your other code doesn't break
    @SerializedName("subcategory")
    public SubCategoryModel subcategory;

    @SerializedName("batches")
    public List<BatchModel> batches;


    // ==========================================
    // --- YOUR EXACT HELPER METHODS ---
    // (Updated to read from the ROOT fields)
    // ==========================================

    public double getMrpDouble() {
        // Now reads directly from root "mrp"
        try {
            return mrp != null ? Double.parseDouble(mrp) : 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }

    public double getSellingPriceDouble() {
        // Now reads directly from root "sellingPrice"
        try {
            return sellingPrice != null ? Double.parseDouble(sellingPrice) : 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }

    public int getStockInt() {
        // Now reads directly from root "total_remaining_quantity"
        return totalRemainingQuantity;
    }

    public int getCaseSizeInt() {
        if (batches != null && !batches.isEmpty()) {
            try {
                String unitStr = batches.get(0).unit != null ? batches.get(0).unit.replaceAll("[^0-9]", "") : "1";
                if (unitStr.isEmpty()) return 1;
                return Integer.parseInt(unitStr);
            } catch (Exception e) { return 1; }
        }
        return 1;
    }

    // --- String Getters for UI (3 Decimals) ---
    public String getMrp() {
        return String.format(java.util.Locale.US, "%.2f", getMrpDouble());
    }

    public String getSellingPrice() {
        return String.format(java.util.Locale.US, "%.2f", getSellingPriceDouble());
    }

    public String getMargin() {
        // Now reads directly from root "margin"
        return margin != null ? margin : "0";
    }

    public String getStock() {
        return String.valueOf(getStockInt());
    }

    public String getUnit() {
        if (batches != null && !batches.isEmpty() && batches.get(0).unit != null) {
            return batches.get(0).unit;
        }
        return "0";
    }

    public String getCapacity() {
        // Now maps to the root "productWeight".................2
        return productWeight != null ? productWeight : "";
    }

//    @SerializedName("productId")
//    public int productId;
//    @SerializedName("productName")
//    public String productName;
//    @SerializedName("productImage")
//    public String productImage;
//    @SerializedName("category")
//    public CategoryModel category;
////    @SerializedName("productType")
////    public String productType; // "case" or "unit" (or other)
//    @SerializedName("productWeight")
//    public String productWeight;
//    @SerializedName("subcategory")
//    public SubCategoryModel subcategory;
//    @SerializedName("batches")
//    public List<BatchModel> batches;
//
//    // --- Helper Methods for Safe Parsing ---
//
//    public double getMrpDouble() {
//        if (batches != null && !batches.isEmpty()) {
//            try { return Double.parseDouble(batches.get(0).mrp); } catch (Exception e) { return 0.0; }
//        }
//        return 0.0;
//    }
//
//    public double getSellingPriceDouble() {
//        if (batches != null && !batches.isEmpty()) {
//            try { return Double.parseDouble(batches.get(0).sellingPrice); } catch (Exception e) { return 0.0; }
//        }
//        return 0.0;
//    }
//
//    public int getStockInt() {
//        if (batches != null && !batches.isEmpty()) {
//            return batches.get(0).quantity;
//        }
//        return 0;
//    }
//
//    public int getCaseSizeInt() {
//        if (batches != null && !batches.isEmpty()) {
//            try {
//                // If the unit field contains text like "10 pcs", parse just the number
//                String unitStr = batches.get(0).unit.replaceAll("[^0-9]", "");
//                if (unitStr.isEmpty()) return 1;
//                return Integer.parseInt(unitStr);
//            } catch (Exception e) { return 1; }
//        }
//        return 1;
//    }
//
//    // --- String Getters for UI (3 Decimals) ---
//    public String getMrp() {
//        return String.format(java.util.Locale.US, "%.3f", getMrpDouble());
//    }
//
//    public String getSellingPrice() {
//        return String.format(java.util.Locale.US, "%.3f", getSellingPriceDouble());
//    }
////
////    public String getMargin() {
////        if (batches != null && !batches.isEmpty()) return batches.get(0).marginPercent;
////        return "0";
////    }
//
//    public String getMargin() {
//        if (batches != null && !batches.isEmpty()) {
//            BatchModel batch = batches.get(0);
//            if (batch.margin != null && !batch.margin.isEmpty()) {
//                return batch.margin;
//            }
//        }
//        return "0";
//    }
//
//    public String getStock() {
//        return String.valueOf(getStockInt());
//    }
//
//    public String getUnit() {
//        if (batches != null && !batches.isEmpty()) return batches.get(0).unit;
//        return "0";
//    }
//
//    public String getCapacity() {
//        if (batches != null && !batches.isEmpty()) return batches.get(0).unit;
//        return "";
//    }
}
