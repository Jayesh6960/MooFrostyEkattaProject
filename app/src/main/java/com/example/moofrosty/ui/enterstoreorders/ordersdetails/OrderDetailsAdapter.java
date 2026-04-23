package com.example.moofrosty.ui.enterstoreorders.ordersdetails;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moofrosty.R;
import com.example.moofrosty.core.network.Constants;
import com.example.moofrosty.data.model.OrderDetailsResponse;

import java.util.ArrayList;
import java.util.List;
public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {

    private final ArrayList<OrderDetailsResponse.OrderItem> itemList;
    private ArrayList<OrderDetailsResponse.InvoiceItem> itemList1 = new ArrayList<>();

    public OrderDetailsAdapter(ArrayList<OrderDetailsResponse.OrderItem> itemList, int orderStatus) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProduct;
        TextView tvProductName, tvMrp, tvBilledQty, tvOrderQty,
                tvAmount, tvDiscountPercent, tvBilledTag,
                tvbilledvalue, tvorderTag, tvordervalue,
                tvinvoice, tvremian;

        // ✅ ADDED: track last invoice
        static String lastInvoiceNo = "";

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProduct = itemView.findViewById(R.id.img_product);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvMrp = itemView.findViewById(R.id.tv_mrp);
            tvBilledQty = itemView.findViewById(R.id.tv_billed_qty);
            tvOrderQty = itemView.findViewById(R.id.tv_order_qty);
            tvremian = itemView.findViewById(R.id.tv_remain_qty);

            tvAmount = itemView.findViewById(R.id.tv_item_amount);
            tvDiscountPercent = itemView.findViewById(R.id.tv_discount_amount);
            tvBilledTag = itemView.findViewById(R.id.tv_billed_tag);
            tvbilledvalue = itemView.findViewById(R.id.tv_bill_value);

            tvinvoice = itemView.findViewById(R.id.tv_invoice_header);
        }

        @SuppressLint("SetTextI18n")
        public void bind(OrderDetailsResponse.OrderItem item) {

            // ---------------- PRODUCT ----------------
            tvProductName.setText(
                    item.getProductDetails().productName != null
                            ? item.getProductDetails().productName
                            : "Unknown Product"
            );

            if (item.getProductDetails() != null && item.getProductDetails().productImage != null) {
                String imgUrl = Constants.BASE_URL + item.getProductDetails().productImage;
                Glide.with(itemView.getContext())
                        .load(imgUrl)
                        .placeholder(R.drawable.icecategori)
                        .into(imgProduct);
            }

            if (item.getProductDetails().mrp != null) {
                tvMrp.setText("MRP : ₹" + item.getProductDetails().mrp);
            }

            if (tvAmount != null && item.getProductDetails().sellingPrice != null &&
                    !item.getProductDetails().sellingPrice.equals("0")) {

                tvAmount.setVisibility(View.VISIBLE);
                tvAmount.setText("RATE : ₹" + item.getProductDetails().sellingPrice);
            }

            // ================= ✅ INVOICE FIX =================

            String currentInvoice = item.getInvoiceNo();

            if (currentInvoice != null &&
                    !currentInvoice.trim().isEmpty() &&
                    !currentInvoice.equals("0")) {

                if (!currentInvoice.equals(lastInvoiceNo)) {
                    tvinvoice.setVisibility(View.VISIBLE);
                    tvinvoice.setText("Invoice : " + currentInvoice);
                    lastInvoiceNo = currentInvoice;
                } else {
                    tvinvoice.setVisibility(View.GONE);
                }

            } else {
                tvinvoice.setVisibility(View.VISIBLE);
                tvinvoice.setText("Invoice : N/A");
            }
            String discount = item.getDiscountPercent();

            Log.d("Discount FIX", "Value: " + discount);

            if (discount != null && !discount.isEmpty() && !discount.equals("0")) {
                tvDiscountPercent.setVisibility(View.VISIBLE);
                tvDiscountPercent.setText("DISCOUNT : " + discount + "%");
            } else {
                tvDiscountPercent.setVisibility(View.GONE);
            }

            // ================= OTHER LOGIC =================

            if (item.getStatus() == 1) {



                if (item.getBilledQty() != null) {
                    tvBilledQty.setVisibility(View.VISIBLE);
                    tvBilledQty.setText("BILL QTY : " + item.getBilledQty() + " Unit(s)");
                }

                if (item.getOrderQty() != null) {
                    tvOrderQty.setVisibility(View.VISIBLE);
                    tvOrderQty.setText("ORDER QTY : " + item.getOrderQty() + " Unit(s)");
                }

                tvBilledTag.setVisibility(View.VISIBLE);

                if (item.getStatus() == 0) {
                    tvBilledTag.setText("Not Billed");
                    tvBilledTag.setBackgroundColor(Color.parseColor("#9E9E9E"));
                } else {
                    tvBilledTag.setText("Billed");
                    tvBilledTag.setBackgroundColor(Color.parseColor("#4CAF50"));
                }

            } else {

                if (item.getProductDetails().discount_percent != null) {
                    tvDiscountPercent.setVisibility(View.VISIBLE);
                    tvDiscountPercent.setText("DISCOUNT : " +
                            item.getProductDetails().discount_percent + "%");
                } else {
                    tvDiscountPercent.setVisibility(View.GONE);
                }

                if (item.getBilledQty() != null) {
                    tvBilledQty.setVisibility(View.VISIBLE);
                    tvBilledQty.setText("BILL QTY : " + item.getBilledQty() + " Unit(s)");
                }

                if (item.getOrderQty() != null) {
                    tvOrderQty.setVisibility(View.VISIBLE);
                    tvOrderQty.setText("ORDER QTY : " + item.getOrderQty() + " Unit(s)");
                }


                if (item.getStatus() == 0) {
                    tvBilledTag.setText("Not Billed");
                    tvBilledTag.setVisibility(View.VISIBLE);
                    tvBilledTag.setBackgroundColor(Color.parseColor("#9E9E9E"));
                    if (item.getRemainingQty() >0) {
                        tvremian.setVisibility(View.VISIBLE);
                        tvOrderQty.setVisibility(View.VISIBLE);
                        tvBilledQty.setVisibility(View.GONE);
                        tvremian.setText("REMAIN QTY : " + item.getRemainingQty() + " Unit(s)");
                    }
                    tvremian.setVisibility(View.VISIBLE);
                } else {
                    tvBilledTag.setText("Billed");
                    tvBilledTag.setVisibility(View.INVISIBLE);
                    tvBilledTag.setBackgroundColor(Color.parseColor("#4CAF50"));
                }
            }
        }
    }
}

//public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {
//
//    //    private final List<OrderHistoryResponse.Item> itemList;
////    private final int orderStatus;
////
////    public OrderDetailsAdapter(List<OrderHistoryResponse.Item> itemList, int orderStatus) {
////        this.itemList = itemList;
////        this.orderStatus = orderStatus;
////    }
//    private final ArrayList<OrderDetailsResponse.OrderItem> itemList;
//    private ArrayList<OrderDetailsResponse.InvoiceItem> itemList1 = new ArrayList<>();
//
//    public OrderDetailsAdapter(ArrayList<OrderDetailsResponse.OrderItem> itemList, int orderStatus) {
//        this.itemList = itemList;
////        this.orderStatus = orderStatus;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_details, parent, false);
//        return new ViewHolder(view);
//    }
//
//    //
////    @Override
////    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
////        holder.bind(itemList.get(position),orderStatus);
////    }
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.bind(itemList.get(position));
////
//    }
//
//
//
//    @Override
//    public int getItemCount() {
//        return itemList != null ? itemList.size() : 0;
//    }
//
////    public void setData(List<InvoiceUIModel> finalList) {
////        itemList.clear();
////        itemList.addAll(finalList);
////        notifyDataSetChanged();
////    }
//
//    static class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView imgProduct;
//        TextView tvProductName, tvMrp, tvBilledQty, tvOrderQty,tvAmount, tvDiscountPercent, tvBilledTag, tvbilledvalue,tvorderTag, tvordervalue, tvinvoice,tvremian;
//        ;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            imgProduct = itemView.findViewById(R.id.img_product);
//            tvProductName = itemView.findViewById(R.id.tv_product_name);
//            tvMrp = itemView.findViewById(R.id.tv_mrp);
//            tvBilledQty = itemView.findViewById(R.id.tv_billed_qty);
//            tvOrderQty = itemView.findViewById(R.id.tv_order_qty);
//            tvremian=itemView.findViewById(R.id.tv_remain_qty);
//
//
//            // Assuming you have a TextView for the item amount, if not, add one to XML
//            tvAmount = itemView.findViewById(R.id.tv_item_amount);
//            // [HIGHLIGHT] Find Discount ID
//            tvDiscountPercent = itemView.findViewById(R.id.tv_discount_amount);
//            tvBilledTag = itemView.findViewById(R.id.tv_billed_tag);
//            tvbilledvalue = itemView.findViewById(R.id.tv_bill_value);
//
//
//
//            tvinvoice = itemView.findViewById(R.id.tv_invoice_header);
//
//        }
//
//        //        public void bind(OrderDetailsResponse.OrderItem orderItem) {
////
////            if (orderItem == null) return;
////
////            OrderDetailsResponse.ProductDetail product =
////                    orderItem.getProductDetails();
////
////            if (product == null) return;
////
////            // ✅ Product Name
////            tvProductName.setText(
////                    product.getProductName() != null
////                            ? product.getProductName()
////                            : "N/A"
////            );
////
////            // ✅ MRP
////            tvMrp.setText("₹ " + product.getMrp());
////
////            // ✅ Selling Price / Amount
////            tvAmount.setText("₹ " + orderItem.getProductSellingPrice());
////
////            // ✅ Quantity
////            tvBilledQty.setText(
////                    orderItem.getBilledQty() + "/" +
////                            orderItem.getOrderQty() + " Unit(s)"
////            );
////
////            // ❌ REMOVE OLD DISCOUNT LOGIC (not in model)
////            tvDiscountPercent.setVisibility(View.GONE);
////
////            // ❌ REMOVE FINAL AMOUNT (not in model)
////            tvordervalu.setVisibility(View.GONE);
////
////            // ✅ Billed Tag
////            if (orderItem.getStatus() == 1) {
////                tvBilledTag.setVisibility(View.VISIBLE);
////                tvBilledTag.setText("Billed");
////            } else{
////                tvBilledTag.setVisibility(View.VISIBLE);
////                tvBilledTag.setText("Not Billed");
////            }
////
////
////            // ✅ Image Load
////            Glide.with(itemView.getContext())
////                    .load(Constants.BASE_URL + product.getProductImage())
////                    .placeholder(R.drawable.icecategori)
////                    .error(R.drawable.errorimage)
////                    .into(imgProduct);
////        }
////        }
//        @SuppressLint("SetTextI18n")
//
//        public void bind(OrderDetailsResponse.InvoiceItem item1)
//
//        {
//            if (item1.getInvoiceNo() != null) {
//                tvMrp.setText("Invoice               : ₹" + item1.getInvoiceNo());
//            }
//            if (item1.getInvoiceNo() != null) {
//                tvMrp.setText("Invoice              : ₹" + item1.getInvoiceNo());
//            }
//        }
////
//        @SuppressLint("SetTextI18n")
//
//        public void bind(OrderDetailsResponse.OrderItem item) {
//
//            // [HIGHLIGHT] Fetching Flat Product Name
//            tvProductName.setText(item.getProductDetails().productName != null ? item.getProductDetails().productName : "Unknown Product");
//            tvProductName.setText(item.getProductDetails().productName != null ? item.getProductDetails().productName : "Unknown Product");
//
//            // Getting Image from nested object
//            if (item.getProductDetails() != null && item.getProductDetails().productImage != null) {
//                String imgUrl = Constants.BASE_URL + item.getProductDetails().productImage;
//                Glide.with(itemView.getContext())
//                        .load(imgUrl)
//                        .placeholder(R.drawable.icecategori)
//                        .into(imgProduct);
//            }
//
//            // [HIGHLIGHT] Getting MRP directly from flat mapping
//            if (item.getProductDetails().mrp != null) {
//                tvMrp.setText("MRP               : ₹" + item.getProductDetails().mrp);
//            }
//            if (item.getProductDetails().mrp != null) {
//                tvMrp.setText("MRP               : ₹" + item.getProductDetails().mrp);
//            }
//
//            // [HIGHLIGHT] Getting Units
//
//
//            // [HIGHLIGHT] Final Amount mapping (Selling Total)
//            if (tvAmount != null && item.getProductDetails().sellingPrice != null && item.getProductDetails().sellingPrice != null && !item.getProductDetails().equals("0")) {
//                tvAmount.setText("RATE              : ₹" + item.getProductDetails().sellingPrice);
//                tvAmount.setVisibility(View.VISIBLE);
//            }
//
//            if (tvAmount != null && item.getProductDetails().sellingPrice != null && !item.getProductDetails().sellingPrice.equals("0")) {
//                tvAmount.setVisibility(View.VISIBLE);
//                tvAmount.setText("RATE              : ₹" + item.getProductDetails().sellingPrice);
//            }
//            if (tvAmount != null && item.getProductDetails().sellingPrice != null && !item.getProductDetails().sellingPrice.equals("0")) {
//                tvAmount.setVisibility(View.VISIBLE);
//                tvAmount.setText("RATE              : ₹" + item.getProductDetails().sellingPrice);
//            }
//
//            Log.d("selling price", "selling price: " + item.getProductDetails().sellingPrice);
////
////                // [HIGHLIGHT] 6. Discount & Status Logic
////                // Only show discount if Order Status is 1 (Billed)
//            if (item.getStatus() == 1) {
//                if (item.getInvoiceNo() == null) {
//                tvinvoice.setVisibility(View.VISIBLE);
//                Log.d("Invoice","bind: "+"--");
//                tvinvoice.setText("--");
//            }
//
//                if (item.getInvoiceNo() != null) {
//                    tvinvoice.setVisibility(View.VISIBLE);
//                    Log.d("Invoice","bind: "+"--");
//                    tvinvoice.setText("--");
//                } else {
//                    tvinvoice.setVisibility(View.VISIBLE);
//                }
//
//
//                if (item.getProductDetails().sellingPrice != null) {
//                    tvDiscountPercent.setVisibility(View.VISIBLE);
//                    tvDiscountPercent.setText("DISCOUNT       : " + item.getProductDetails().discount_percent + "%");
//                } else {
//                    tvDiscountPercent.setVisibility(View.GONE);
//                }
//                if (item.getOrderQty() != null) {
//                    tvDiscountPercent.setVisibility(View.VISIBLE);
//                    tvDiscountPercent.setText("DISCOUNT      : " + item.getProductDetails().discount_percent + "%");
//                }
//                if (item.getBilledQty() != null) {
//                    tvBilledQty.setVisibility(View.VISIBLE);
//                    tvBilledQty.setText("BILL QTY        : " + item.getBilledQty() + " Unit(s)");
//                } else {
//                    tvBilledQty.setVisibility(View.GONE);
//                }
//                tvBilledTag.setVisibility(View.VISIBLE);
//                if (item.getOrderQty() != null) {
//                    tvOrderQty.setVisibility(View.VISIBLE);
//                    tvOrderQty.setText("ORDER QTY    : " + item.getOrderQty() + " Unit(s)");
//                } else {
//                    tvOrderQty.setVisibility(View.GONE);
//                }
////                if (item.getOrderQty() != null) {
////                    tvremian.setVisibility(View.VISIBLE);
////                    tvremian.setText("Remain QTY    : " + item.getRemainingQty() + " Unit(s)");
////                } else {
////                    tvremian.setVisibility(View.GONE);
////                }
//                if (item.getStatus() == 0) {
//                    // Item was discarded
//                    tvBilledTag.setText("Not Billed");
//                    tvBilledTag.setBackgroundColor(Color.parseColor("#9E9E9E")); // Grey Background
//                }
//                else {
//                    // Item was billed successfully
//                    tvBilledTag.setText("Billed");
//                    tvBilledTag.setBackgroundColor(Color.parseColor("#4CAF50")); // Green Background
//                }
////                if (item.getStatus() == 1) {
////                    // Item was discarded
////                    tvBilledTag.setText("Discarded");
////                    tvBilledTag.setBackgroundColor(Color.parseColor("#9E9E9E")); // Grey Background
////                }
//
//            }
//            else {
//                if (item.getProductDetails().sellingPrice != null) {
//                    tvDiscountPercent.setVisibility(View.GONE);
//                    tvDiscountPercent.setText("DISCOUNT     : " + item.getProductSellingPrice() + "%");
//                } else {
//                    tvDiscountPercent.setVisibility(View.GONE);
//                }
//                if (item.getBilledQty() != null) {
//                    tvBilledQty.setVisibility(View.VISIBLE);
//                    tvBilledQty.setText("BILL QTY        : " + item.getBilledQty() + " Unit(s)");
//                }
//                if (item.getOrderQty() != null) {
//                    tvOrderQty.setVisibility(View.VISIBLE);
//                    tvOrderQty.setText("ORDER QTY    : " + item.getOrderQty() + " Unit(s)");
//                } else {
//                    tvOrderQty.setVisibility(View.GONE);
//                }
//                if (item.getStatus() == 0) {
//                    // Item was discarded
//                    tvBilledTag.setText("Not Billed");
//                    tvBilledTag.setVisibility(View.VISIBLE);
//                    tvBilledTag.setBackgroundColor(Color.parseColor("#9E9E9E")); // Grey Background
//                }
//                else {
//                    // Item was billed successfully
//                    tvBilledTag.setText("Billed");
//                    tvBilledTag.setVisibility(View.INVISIBLE);
//                    tvBilledTag.setBackgroundColor(Color.parseColor("#4CAF50")); // Green Background
//                }
//            }
//        }
//    }
//}
////
//////
////            // Discount
//////            if (item.discountPercent != null) {
//////                tvDiscountPercent.setVisibility(View.GONE);
//////                tvDiscountPercent.setText("DISCOUNT : " + item.discountPercent + "%");
//////
//////            } else {
//////                tvDiscountPercent.setVisibility(View.GONE);
//////            }
////                // Discount
////                if (orderStatus == 1 && item.discountPercent != null && !item.discountPercent.equals("0")) {
////
////                    tvDiscountPercent.setVisibility(View.VISIBLE);
////                    tvDiscountPercent.setText("DISCOUNT : " + item.discountPercent + "%");
////
////                } else {
////
////                    tvDiscountPercent.setVisibility(View.GONE);
////
////                }
//
//// Billed Quantity
////            if (item.units != null) {
////                tvBilledQty.setVisibility(View.GONE);
//////                tvBilledQty.setText("BILL QTY : " + item.units + " Unit(s)");
////            } else {
////                tvBilledQty.setVisibility(View.GONE);
////            }
//
//// Status Tag (Billed / Discarded)
//                tvBilledTag.setVisibility(View.VISIBLE);
//
//                String statusText;
//                String colorCode;
//
//// Handling both statuses in the same tab
//                if (item.status == 1) {
//                    statusText = "Discarded";
//                    colorCode = "#9E9E9E";
//                }
////            if (item.isDiscard == 1) {
////                statusText = "Not Billed";
////                colorCode = "#36802d";
////            }
//                else {
//                    statusText = "Billed";
//                    colorCode = "#4CAF50";
//                }
//
//// Apply UI
//                tvBilledTag.setText(statusText);
//                tvBilledTag.setBackgroundColor(Color.parseColor(colorCode));
//
//// Logging
////            Log.d("ORDER_STATUS",
////                    "Invoice: " + item.invoiceNo +
////                            " isDiscard: " + item.isDiscard +
////                            " Status: " + statusText);
//
//            }
//        }
//        @SuppressLint("SetTextI18n")
//        public void bind(OrderDetailsResponse.InvoiceItem item) {
//
//            if (item == null) return;
//
//            // ---------------- Product Name ----------------
//            if (item.getItems() != null && item.i.productName != null) {
//                tvProductName.setText(item.productDetails.productName);
//            } else {
//                tvProductName.setText("Unknown Product");
//            }
//
//            // ---------------- Product Image ----------------
//            if (item.productDetails != null && item.productDetails.productImage != null) {
//
//                String imgUrl = Constants.BASE_URL + item.productDetails.productImage;
//
//                Glide.with(itemView.getContext())
//                        .load(imgUrl)
//                        .placeholder(R.drawable.icecategori)
//                        .into(imgProduct);
//            }
//
//            // ---------------- MRP ----------------
//            if (item.productDetails != null && item.productDetails.mrp != null) {
//                tvMrp.setText("MRP : ₹" + item.productDetails.mrp);
//            } else {
//                tvMrp.setText("MRP : ₹0");
//            }
//
//            // ---------------- RATE ----------------
//            tvAmount.setVisibility(View.VISIBLE);
//            tvAmount.setText("RATE : ₹" + item.productDetails.sellingPrice);
//
//            // ---------------- Units ----------------
//            tvBilledQty.setVisibility(View.VISIBLE);
//            tvBilledQty.setText("BILL QTY : " + item.units + " Unit(s)");
//
//            // ---------------- Discount ---------------
//            if (item.discountPercent > 0) {
//                tvDiscountPercent.setVisibility(View.VISIBLE);
//                tvDiscountPercent.setText("DISCOUNT : " + item.discountPercent + "%");
//            } else {
//                tvDiscountPercent.setVisibility(View.GONE);
//            }
//
//            // ---------------- Status Tag ----------------
//            tvBilledTag.setVisibility(View.VISIBLE);
//
//            String statusText;
//            String colorCode;
//
////            if (item.status == 0) {
////                statusText = "Not Billed";
////                colorCode = "#9E9E9E";
////            }
//            if (item.status == 0) {
//                statusText = "Not Billed";
//                colorCode = "#006400";
//            }
//            else {
//                statusText = "Billed";
//                colorCode = "#4CAF50";
//            }
//
//            tvBilledTag.setText(statusText);
//            tvBilledTag.setBackgroundColor(Color.parseColor(colorCode));
//
//            // ---------------- Debug Logs ----------------
//            Log.d("ORDER_ITEM_BIND",
//                    "Product: " + item.getItems() +
//                            " | Units: " + item.units +
//                            " | Price: " + item.productSellingPrice +
//                            " | Discount: " + item.discountPercent +
//                            " | Status: " + item.status);
//        }
//@SuppressLint("SetTextI18n")
//public void bind(OrderDetailsResponse.InvoiceItem item) {
//
//    if (item == null || item.getItems() == null || item.getItems().isEmpty()) return;
//
//    OrderDetailsResponse.O orderItem = item.getItems().get(0);
//    OrderDetailsResponse.productDetail product = orderItem.getProductDetails();
//
//    if (product == null) return;
//
//    // Product Name
//    tvProductName.setText(product.productName != null ? product.productName : "Unknown Product");
//
//    // Product Image
//    if (product.productImage != null) {
//
//        String imgUrl = Constants.BASE_URL + product.productImage;
//
//        Glide.with(itemView.getContext())
//                .load(imgUrl)
//                .placeholder(R.drawable.icecategori)
//                .into(imgProduct);
//    }
//
//    // MRP
//    tvMrp.setText("MRP : ₹" + (product.mrp != null ? product.mrp : "0"));
//
//    // Rate
//    tvAmount.setVisibility(View.VISIBLE);
//    tvAmount.setText("RATE : ₹" + orderItem.getProductSellingPrice());
//
//    // Billed Qty
//    tvBilledQty.setVisibility(View.VISIBLE);
//    tvBilledQty.setText("BILL QTY : " + orderItem.getBilledQty() + " Unit(s)");
//
//    // Status
//    tvBilledTag.setVisibility(View.VISIBLE);
//
//    String statusText;
//    String colorCode;
//
//    if (orderItem.getStatus() == 0) {
//        statusText = "Not Billed";
//        colorCode = "#006400";
//    } else {
//        statusText = "Billed";
//        colorCode = "#4CAF50";
//    }
//
//    tvBilledTag.setText(statusText);
//    tvBilledTag.setBackgroundColor(Color.parseColor(colorCode));
//
//    // Debug
//    Log.d("ORDER_ITEM_BIND",
//            "Product: " + product.productName +
//                    " | BilledQty: " + orderItem.getBilledQty() +
//                    " | Price: " + orderItem.getProductSellingPrice() +
//                    " | Status: " + orderItem.getStatus());
//}


//        @SuppressLint("SetTextI18n")
//        public void bind(OrderHistoryResponse.Item item) {
//            // 1. Product Name & Image
//            if (item.product != null) {
//                tvProductName.setText(item.product.productName);
//
//                String imgUrl = "https://moofrosty.ekatta.in/" + item.product.productImage;
//                Glide.with(itemView.getContext())
//                        .load(imgUrl)
//                        .placeholder(R.drawable.icecategori)
//                        .into(imgProduct);
//            }
//            // 2. Pricing from Batch
//            if (item.batch != null) {
//                // Showing MRP. You can show Selling Price if preferred.
//                tvMrp.setText("MRP : ₹" + item.batch.mrp);
//            }
//            // 3. Quantity
//            // Display: "180/180 Unit(s)" logic based on your requirement
//            // Assuming item.quantity is the total billed quantity
//            tvBilledQty.setText(item.quantity + " Unit(s)");
//            // 4. Amount (Item Total)
//            // JSON provides "amount": "54.81"
//            if (tvAmount != null && item.amount != null) {
//                tvAmount.setText("Selling Price : ₹" + item.amount);
//                tvAmount.setVisibility(View.VISIBLE);
//            }
//        }
//    }




//    private final List<CartItem> itemList;
//
//
//    public OrderDetailsAdapter(List<CartItem> itemList) {
//        this.itemList = itemList;
//    }
//    @NonNull
//    @Override
//    public OrderDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_details, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull OrderDetailsAdapter.ViewHolder holder, int position) {
//        holder.bind(itemList.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return itemList.size();
//    }
//
//    static class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView imgProduct;
//        TextView tvProductName, tvMrp, tvBilledQty;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            imgProduct = itemView.findViewById(R.id.img_product);
//            tvProductName = itemView.findViewById(R.id.tv_product_name);
//            tvMrp = itemView.findViewById(R.id.tv_mrp);
//            tvBilledQty = itemView.findViewById(R.id.tv_billed_qty);
//        }
//
//        public void bind(CartItem item) {
//            Product product = item.getProduct();
//            tvProductName.setText(product.getName());
//            tvMrp.setText(product.getRate()); // Show the rate they paid
//            imgProduct.setImageResource(product.getImageResId());
//
////            String qtyString = String.format(Locale.getDefault(), "%d/%d Unit(s)",
////                    item.getQuantity(), item.getQuantity());
////            tvBilledQty.setText(qtyString);
//        }
//    }
//}
