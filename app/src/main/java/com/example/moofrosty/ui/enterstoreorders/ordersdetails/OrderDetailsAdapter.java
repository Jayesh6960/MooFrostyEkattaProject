package com.example.moofrosty.ui.enterstoreorders.ordersdetails;

import android.annotation.SuppressLint;
import android.graphics.Color;
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
import com.example.moofrosty.data.model.CartItem;
import com.example.moofrosty.data.model.OrderHistoryResponse;
import com.example.moofrosty.data.model.Product;

import java.util.List;
import java.util.Locale;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder>{

    private final List<OrderHistoryResponse.Item> itemList;
    private final int orderStatus;

    public OrderDetailsAdapter(List<OrderHistoryResponse.Item> itemList, int orderStatus) {
        this.itemList = itemList;
        this.orderStatus = orderStatus;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(itemList.get(position),orderStatus);
    }

    @Override
    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvProductName, tvMrp, tvBilledQty, tvAmount, tvDiscountPercent, tvBilledTag;;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_product);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvMrp = itemView.findViewById(R.id.tv_mrp);
            tvBilledQty = itemView.findViewById(R.id.tv_billed_qty);
            // Assuming you have a TextView for the item amount, if not, add one to XML
            tvAmount = itemView.findViewById(R.id.tv_item_amount);
            // [HIGHLIGHT] Find Discount ID
            tvDiscountPercent = itemView.findViewById(R.id.tv_discount_amount);
            tvBilledTag = itemView.findViewById(R.id.tv_billed_tag);

        }

        @SuppressLint("SetTextI18n")
        public void bind(OrderHistoryResponse.Item item, int orderStatus) {

            // [HIGHLIGHT] Fetching Flat Product Name
            tvProductName.setText(item.productName != null ? item.productName : "Unknown Product");

            // Getting Image from nested object
            if (item.productDetails != null && item.productDetails.productImage != null) {
                String imgUrl = Constants.BASE_URL + item.productDetails.productImage;
                Glide.with(itemView.getContext())
                        .load(imgUrl)
                        .placeholder(R.drawable.icecategori)
                        .into(imgProduct);
            }

            // [HIGHLIGHT] Getting MRP directly from flat mapping
            if (item.productMrp != null) {
                tvMrp.setText("MRP               : ₹" + item.productMrp);
            }

            // [HIGHLIGHT] Getting Units


            // [HIGHLIGHT] Final Amount mapping (Selling Total)
//            if (tvAmount != null && item.finalAmount != null && item.productSellingPrice!=null && !item.productSellingPrice.equals("0")) {
//                tvAmount.setText("RATE              : ₹" + item.productSellingPrice);
//                tvAmount.setVisibility(View.VISIBLE);
//            }

            if (tvAmount != null && item.productSellingPrice!=null && !item.productSellingPrice.equals("0")) {
                tvAmount.setVisibility(View.VISIBLE);
                tvAmount.setText("RATE              : ₹" + item.productSellingPrice);
            }

            // [HIGHLIGHT] 6. Discount & Status Logic
            // Only show discount if Order Status is 1 (Billed)
            if (orderStatus == 1) {
                if (item.discountPercent != null) {
                    tvDiscountPercent.setVisibility(View.VISIBLE);
                    tvDiscountPercent.setText("DISCOUNT     : " + item.discountPercent + "%");
                } else {
                    tvDiscountPercent.setVisibility(View.GONE);
                }
                if (item.units != null) {
                    tvBilledQty.setVisibility(View.VISIBLE);
                    tvBilledQty.setText("BiLL QTY        : "+item.units + " Unit(s)");
                }else {
                    tvBilledQty.setVisibility(View.GONE);
                }
                tvBilledTag.setVisibility(View.VISIBLE);
                if (item.isDiscard == 1) {
                    // Item was discarded
                    tvBilledTag.setText("Discarded");
                    tvBilledTag.setBackgroundColor(Color.parseColor("#9E9E9E")); // Grey Background
                } else {
                    // Item was billed successfully
                    tvBilledTag.setText("Billed");
                    tvBilledTag.setBackgroundColor(Color.parseColor("#4CAF50")); // Green Background
                }
            } else {
                tvDiscountPercent.setVisibility(View.GONE);
                tvBilledQty.setVisibility(View.GONE);
            }

        }
    }

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

}

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
