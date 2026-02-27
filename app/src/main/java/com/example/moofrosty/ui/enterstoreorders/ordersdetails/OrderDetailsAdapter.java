package com.example.moofrosty.ui.enterstoreorders.ordersdetails;

import android.annotation.SuppressLint;
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
import com.example.moofrosty.data.model.CartItem;
import com.example.moofrosty.data.model.OrderHistoryResponse;
import com.example.moofrosty.data.model.Product;

import java.util.List;
import java.util.Locale;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder>{

    private final List<OrderHistoryResponse.Item> itemList;

    public OrderDetailsAdapter(List<OrderHistoryResponse.Item> itemList) {
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
        TextView tvProductName, tvMrp, tvBilledQty, tvAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_product);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvMrp = itemView.findViewById(R.id.tv_mrp);
            tvBilledQty = itemView.findViewById(R.id.tv_billed_qty);
            // Assuming you have a TextView for the item amount, if not, add one to XML
            tvAmount = itemView.findViewById(R.id.tv_item_amount);
        }

        @SuppressLint("SetTextI18n")
        public void bind(OrderHistoryResponse.Item item) {

            // [HIGHLIGHT] Fetching Flat Product Name
            tvProductName.setText(item.productName != null ? item.productName : "Unknown Product");

            // Getting Image from nested object
            if (item.productDetails != null && item.productDetails.productImage != null) {
                String imgUrl = "https://moofrosty.ekatta.in/" + item.productDetails.productImage;
                Glide.with(itemView.getContext())
                        .load(imgUrl)
                        .placeholder(R.drawable.icecategori)
                        .into(imgProduct);
            }

            // [HIGHLIGHT] Getting MRP directly from flat mapping
            if (item.productMrp != null) {
                tvMrp.setText("MRP : ₹" + item.productMrp);
            }

            // [HIGHLIGHT] Getting Units
            if (item.units != null) {
                tvBilledQty.setText(item.units + " Unit(s)");
            }

            // [HIGHLIGHT] Final Amount mapping (Selling Total)
            if (tvAmount != null && item.finalAmount != null) {
                tvAmount.setText("₹" + item.finalAmount);
                tvAmount.setVisibility(View.VISIBLE);
            }
            Log.d("tvamount", "tvamount :-"+item.finalAmount);
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
