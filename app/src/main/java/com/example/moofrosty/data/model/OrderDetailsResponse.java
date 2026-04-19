//package com.example.moofrosty.data.model;
//
//import com.google.gson.annotations.SerializedName;
//import java.io.Serializable;
//import java.util.List;
//
//
//public class OrderDetailsResponse implements Serializable {
//
//    @SerializedName("status")
//    public String status;
//
//    @SerializedName("order_no")
//    public String orderNo;
//
////        @SerializedName("invoice_no")
////        public String invoice_no;
//
//    @SerializedName("items")
//    public List<InvoiceItem> items; // Chiled  of the items
//
//    public class Item implements Serializable {
//
//        @SerializedName("productDetails")
//        public OrderHistoryResponse.ProductDetail productDetails;
//
//        @SerializedName("product_selling_price")
//        public double productSellingPrice;
//
//        @SerializedName("units")
//        public int units;
//
//        @SerializedName("basic_amount")
//        public String basicAmount;
//
//        @SerializedName("discount_amount")
//        public double discountAmount;
//
//        @SerializedName("discount_percent")
//        public int discountPercent;
//
//        @SerializedName("final_amount")
//        public String finalAmount;
//
//        @SerializedName("remarks")
//        public String remarks;
//
//        @SerializedName("status")
//        public int status;
//    }
//public class InvoiceItem {
//
//    @SerializedName("invoice_no")
//    private String invoiceNo;
//
//    @SerializedName("items")
//    private List<OrderItem>  items;
//
//    public String getInvoiceNo() {
//        return invoiceNo;
//    }
//
////    public List<OrderItem> getItems() {
////        return items;
////    }
//}
//    public class OrderItem {
//
//        @SerializedName("productDetails")
//        private productDetails productDetails;
//
//        @SerializedName("order_qty")
//        private int orderQty;
//
//        @SerializedName("billed_qty")
//        private String billedQty;
//
//        @SerializedName("remaining_qty")
//        private int remainingQty;
//
//        @SerializedName("product_selling_price")
//        private String productSellingPrice;
//
//        @SerializedName("status")
//        private int status;
//        @SerializedName("discount_percent")
//        private String discountPercent;
//        @SerializedName("discount_amount")
//        private String discountAmount;
//        @SerializedName("basic_amount")
//        private String basicAmount;
//        @SerializedName("items")
//        private List<productDetail> items;
//        public productDetail getProductDetails() {
//            return productDetails;
//        }
//
//        public productDetail getProductDetails() {
//            return productDetails;
//        }
//
//        public String getBilledQty() {
//            return billedQty;
//        }
//
//        public int getOrderQty() {
//            return orderQty;
//        }
//
//        public String getProductSellingPrice() {
//            return productSellingPrice;
//        }
//        public int getStatus() {
//            return status;
//        }
//        public List<productDetail> getItems() {
//            return items;
//        }
//        public List<productDetail> items;
//        public int getRemainingQty() {
//            return remainingQty;
//        }
//        public String getDiscountPercent() {
//            return discountPercent;
//        }
//        public String getDiscountAmount() {
//            return discountAmount;
//        }
//        public String getBasicAmount() {
//            return basicAmount;
//            }
//    }
//    }
//
//
////    public static class productDetail implements Serializable {
//
//        @SerializedName("productId")
//        public int productId;
//
//        @SerializedName("categoryId")
//        public int categoryId;
//
//        @SerializedName("productName")
//        public String productName;
//
//        @SerializedName("productImage")
//        public String productImage;
//
//        @SerializedName("productWeight")
//        public String productWeight;
//
//        @SerializedName("mrp")
//        public String mrp;
//        @SerializedName("sellingPrice")
//        public String sellingPrice;
//
//
////        @SerializedName("basicAmount")
////        public String basicAmount;
//
//        @SerializedName("gstPercent")
//        public String gstPercent;
//    }
//}
package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

//public class OrderDetailsResponse implements Serializable {
//
//    @SerializedName("status")
//    public String status;
//
//    @SerializedName("order_no")
//    public String orderNo;
//
//    @SerializedName("items")
//    public List<InvoiceItem> items;
//
//    // ---------------- Invoice ----------------
//    public static class InvoiceItem implements Serializable {
//
//        @SerializedName("invoice_no")
//        private String invoiceNo;
//
//        @SerializedName("items")
//        private List<OrderItem> items;
//
//        public String getInvoiceNo() {
//            return invoiceNo;
//        }
//
//        public List<OrderItem> getItems() {
//            return items;
//        }
//
//        public OrderHistoryResponse.Item getProductDetails() {
//            return null;
//        }
//
//        public String getBilledQty() {
//            return null;
//        }
//        public int getOrderQty() {
//            return 0;
//        }
//        public String getProductSellingPrice() {
//            return null;
//        }
//        public int getStatus() {
//            return 0;
//        }
//
//
//        public String getDiscountPercent() {
//            return null;
//        }
//        }
//    }

    // ---------------- Order Item ----------------
//     class OrderItem implements Serializable {
//
//        @SerializedName("productDetails")
//        private ProductDetail productDetails;
//
//        @SerializedName("order_qty")
//        private int orderQty;
//
//        @SerializedName("billed_qty")
//        private String billedQty;
//
//        @SerializedName("remaining_qty")
//        private int remainingQty;
//
//        @SerializedName("product_selling_price")
//        private String productSellingPrice;
//
//        @SerializedName("status")
//        private int status;
//
//        @SerializedName("discount_percent")
//        private String discountPercent;
//
//        @SerializedName("discount_amount")
//        private String discountAmount;
//
//        @SerializedName("basic_amount")
//        private String basicAmount;
//
//        public ProductDetail getProductDetails() {
//            return productDetails;
//        }
//
//        public int getOrderQty() {
//            return orderQty;
//        }
//
//        public String getBilledQty() {
//            return billedQty;
//        }
//
//        public int getRemainingQty() {
//            return remainingQty;
//        }
//
//        public String getProductSellingPrice() {
//            return productSellingPrice;
//        }
//
//        public int getStatus() {
//            return status;
//        }
//
//        public String getDiscountPercent() {
//            return discountPercent;
//        }
//
//        public String getDiscountAmount() {
//            return discountAmount;
//        }
//
//        public String getBasicAmount() {
//            return basicAmount;
//        }
//    }
//    package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class OrderDetailsResponse implements Serializable {

    @SerializedName("status")
    public String status;

    @SerializedName("order_no")
    public String orderNo;

    @SerializedName("items")
    public List<InvoiceItem> items;

    public List<InvoiceItem> getItems() {
        return items;
    }
    public String getOrderNo() {
        return orderNo;
    }



    // ---------------- Invoice ----------------
    public static class InvoiceItem implements Serializable {

        @SerializedName("invoice_no")
        private String invoiceNo;


        @SerializedName("items")
        private List<OrderItem> items;

        public String getInvoiceNo() {
            return invoiceNo;
        }

        public List<OrderItem> getItems() {
            return items;
        }


    }

    // ---------------- Order Item ----------------
    public static class OrderItem implements Serializable {

        @SerializedName("productDetails")
        private ProductDetail productDetails;

        @SerializedName("order_qty")
        private String orderQty;

        @SerializedName("billed_qty")
        private String billedQty;

        @SerializedName("remaining_qty")
        private int remainingQty;

        @SerializedName("product_selling_price")
        private String productSellingPrice;

        @SerializedName("status")
        private int status;
        private String invoiceNo; // ✅ ADD THIS

        public String getInvoiceNo() {
            return invoiceNo;
        }

        public void setInvoiceNo(String invoiceNo) {
            this.invoiceNo = invoiceNo;
        }

        public ProductDetail getProductDetails() {
            return productDetails;
        }

        public String getOrderQty() {
            return orderQty;
        }

        public String getBilledQty() {
            return billedQty;
        }

        public int getRemainingQty() {
            return remainingQty;
        }

        public String getProductSellingPrice() {
            return productSellingPrice;
        }

        public int getStatus() {
            return status;
        }


    }

    // ---------------- Product Details ----------------
    public static class ProductDetail implements Serializable {

        @SerializedName("productId")
        public int productId;

        @SerializedName("categoryId")
        public int categoryId;

        @SerializedName("productName")
        public String productName;

        @SerializedName("productImage")
        public String productImage;

        @SerializedName("productWeight")
        public String productWeight;

        @SerializedName("mrp")
        public String mrp;

        @SerializedName("sellingPrice")
        public String sellingPrice;

        @SerializedName("gstPercent")
        public String gstPercent;
        @SerializedName("status")
        public String status;

        @SerializedName("discount_percent")
        public String discount_percent;
        public String getStatus() {
            return status;
        }
        public void setStatus(String status) {
            this.status = status;
        }
        public void setProductName(String productName) {
            this.productName = productName;
        }
        public void setMrp(String mrp) {
            this.mrp = mrp;
        }
        public void setSellingPrice(String sellingPrice) {
            this.sellingPrice = sellingPrice;
        }
        public void setGstPercent(String gstPercent) {
            this.gstPercent = gstPercent;
        }
        public void setProductImage(String productImage) {
            this.productImage = productImage;
        }
        public void setProductWeight(String productWeight) {
            this.productWeight = productWeight;}




        public String getProductName() {
            return productName;
        }
        public String getMrp() {
            return mrp;
        }
        public String getSellingPrice() {
            return sellingPrice;
        }
        public String getGstPercent() {
            return gstPercent;
        }
        public String getProductImage() {
            return productImage;
        }
        public String getProductWeight() {
            return productWeight;
        }
        public int getCategoryId() {
            return categoryId;
        }
        public String getDiscount_percent() {
            return discount_percent;
        }
        public int getProductId() {
            return productId;
        }

    }
}
