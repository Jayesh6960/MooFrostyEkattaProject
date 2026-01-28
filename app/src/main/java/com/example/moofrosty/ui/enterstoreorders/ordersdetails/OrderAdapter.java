package com.example.moofrosty.ui.enterstoreorders.ordersdetails;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moofrosty.R;
import com.example.moofrosty.data.model.Order;
import com.example.moofrosty.data.model.OrderHistoryResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>{
    private List<OrderHistoryResponse.OrderData> orderList = new ArrayList<>();
    private final OnOrderClickListener listener;

    public interface OnOrderClickListener {
        void onOrderClick(OrderHistoryResponse.OrderData order);
    }

    public OrderAdapter(OnOrderClickListener listener) {
        this.listener = listener;
    }

    public void updateList(List<OrderHistoryResponse.OrderData> newList) {
        this.orderList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderHistoryResponse.OrderData order = orderList.get(position);
        holder.bind(order);
        holder.itemView.setOnClickListener(v -> listener.onOrderClick(order));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderDate, tvOrderStatus, tvOrderValue, tvItemsBilled;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderDate = itemView.findViewById(R.id.tv_order_date);
            tvOrderStatus = itemView.findViewById(R.id.tv_order_status);
            tvOrderValue = itemView.findViewById(R.id.tv_order_value);
            tvItemsBilled = itemView.findViewById(R.id.tv_items_billed);
        }

        @SuppressLint("SetTextI18n")
        public void bind(OrderHistoryResponse.OrderData order) {
            // 1. Date (Parse 2026-01-27T00...)
            String dateStr = order.checkoutDate;
            try {
                if(dateStr != null && dateStr.length() >= 10) {
                    // Take first 10 chars: "2026-01-27"
                    String shortDate = dateStr.substring(0, 10);
                    tvOrderDate.setText(shortDate);
                } else {
                    tvOrderDate.setText(dateStr);
                }
            } catch (Exception e) {
                tvOrderDate.setText(dateStr);
            }

            // 2. Status (Hardcoded or check logic)
            tvOrderStatus.setText("Billed");

            // 3. Order Value
            tvOrderValue.setText(String.format(Locale.getDefault(), ": ₹%.2f", order.totalAmount));

            // 4. Items Billed (Using total_quantity from API)
            tvItemsBilled.setText(": " + order.totalQuantity);
        }
    }

}


  //  dummy code when api not call yet below

//    private List<Order> orderList = new ArrayList<>();
//    private final OnOrderClickListener listener;
//
//    public interface OnOrderClickListener {
//        void onOrderClick(Order order);
//    }
//
//    // --- 2. Update Constructor ---
//    public OrderAdapter(OnOrderClickListener listener) {
//        this.listener = listener;
//    }
//
//    public void updateList(List<Order> newList) {
//        this.orderList = newList;
//        notifyDataSetChanged();
//    }
//
//    @NonNull
//    @Override
//    public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
//        return new OrderViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull OrderAdapter.OrderViewHolder holder, int position) {
//        Order order = orderList.get(position);
//        holder.bind(order);
//
//        // --- 3. Set Click Listener ---
//        holder.itemView.setOnClickListener(v -> {
//            listener.onOrderClick(order);
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return orderList.size();
//    }
//
//    static class OrderViewHolder extends RecyclerView.ViewHolder {
//        TextView tvOrderDate, tvOrderStatus, tvOrderValue, tvItemsBilled;
//
//        public OrderViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tvOrderDate = itemView.findViewById(R.id.tv_order_date);
//            tvOrderStatus = itemView.findViewById(R.id.tv_order_status);
//            tvOrderValue = itemView.findViewById(R.id.tv_order_value);
//            tvItemsBilled = itemView.findViewById(R.id.tv_items_billed);
//        }
//
//        public void bind(Order order) {
//            tvOrderDate.setText(order.orderDate);
//            tvOrderStatus.setText(order.status);
//            tvOrderValue.setText(String.format(Locale.getDefault(), ": ₹%,.2f", order.orderValue));
//            tvItemsBilled.setText(": " + order.itemsBilledString);
//        }
//    }
//}
