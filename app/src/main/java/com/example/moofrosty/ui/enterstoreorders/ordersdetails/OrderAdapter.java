package com.example.moofrosty.ui.enterstoreorders.ordersdetails;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
        TextView tvOrderDate, tvOrderStatus, tvOrderValue, tvBillValue, tvItemsBilled;
        LinearLayout llBillValueRow;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderDate = itemView.findViewById(R.id.tv_order_date);
            tvOrderStatus = itemView.findViewById(R.id.tv_order_status);
            tvOrderValue = itemView.findViewById(R.id.tv_order_value);
            tvItemsBilled = itemView.findViewById(R.id.tv_items_billed);

            tvBillValue = itemView.findViewById(R.id.tv_bill_value);
            llBillValueRow = itemView.findViewById(R.id.ll_bill_value_row);
        }

        @SuppressLint("SetTextI18n")
        public void bind(OrderHistoryResponse.OrderData order) {
            // 1. Date
            String dateStr = order.checkoutDate;
            Log.d("OrderDate", "OrderDate : "+dateStr);
            try {
                if(dateStr != null && dateStr.length() >= 10) {
                    tvOrderDate.setText(dateStr.substring(0, 10));
                } else {
                    tvOrderDate.setText(dateStr);
                }
            } catch (Exception e) {
                tvOrderDate.setText(dateStr);
            }

            // 2. Status
            if (order.status == 1) {
                tvOrderStatus.setText("Billed");
                tvOrderStatus.setTextColor(Color.parseColor("#0D6EfD"));
                tvOrderStatus.setBackgroundResource(R.drawable.tab_unselected_bg);
                // [HIGHLIGHT] Show Bill Value Row only if Billed
                llBillValueRow.setVisibility(View.VISIBLE);
            } else {
                tvOrderStatus.setText("Order Placed");
                tvOrderStatus.setTextColor(Color.parseColor("#0D6EfD"));
                tvOrderStatus.setBackgroundResource(R.drawable.tab_unselected_bg);
                // [HIGHLIGHT] Hide Bill Value Row
                llBillValueRow.setVisibility(View.GONE);
            }

            String currentUnit = (order.currentUnit != null && !order.currentUnit.isEmpty()) ? order.currentUnit : "0";
            String orderTimeUnit = (order.orderTimeUnit != null && !order.orderTimeUnit.isEmpty()) ? order.orderTimeUnit : "0";

            // [HIGHLIGHT] 3. Order Value & Items Billed Using OrderSummary
            if (order.orderSummary != null) {
                tvOrderValue.setText(String.format(Locale.getDefault(), ": ₹%.2f", order.orderSummary.orderValue));
                // [HIGHLIGHT] Show Bill Value
                tvBillValue.setText(String.format(Locale.getDefault(), ": ₹%.2f", order.orderSummary.billValue));
//                tvItemsBilled.setText(": " + order.orderSummary.totalUnits);
                tvItemsBilled.setText(": " + currentUnit + "/" + orderTimeUnit);
            } else {
                tvOrderValue.setText(": ₹0.00");
                    tvItemsBilled.setText(": 0/0");
            }
            Log.d("order.orderSummary.totalFinalAmount", "order.orderSummary.totalFinalAmount"+order.orderSummary.totalFinalAmount);
        }

    }


//        @SuppressLint("SetTextI18n")
//        public void bind(OrderHistoryResponse.OrderData order) {
//            // 1. Date (Parse 2026-01-27T00...)
//            String dateStr = order.checkoutDate;
//            try {
//                if(dateStr != null && dateStr.length() >= 10) {
//                    // Take first 10 chars: "2026-01-27"
//                    String shortDate = dateStr.substring(0, 10);
//                    tvOrderDate.setText(shortDate);
//                } else {
//                    tvOrderDate.setText(dateStr);
//                }
//            } catch (Exception e) {
//                tvOrderDate.setText(dateStr);
//            }
//
//            // 2. Status (Hardcoded or check logic)
//           // tvOrderStatus.setText("Billed");
//            if (order.status == 1) {
//                tvOrderStatus.setText("Billed");
//                tvOrderStatus.setTextColor(Color.parseColor("#0D6EfD")); // Green
//                tvOrderStatus.setBackgroundResource(R.drawable.tab_unselected_bg); // Ensure you have a bg drawable or remove this line
//            } else {
//                tvOrderStatus.setText("Order Placed");
//                tvOrderStatus.setTextColor(Color.parseColor("#0D6EfD")); // Orange
//                tvOrderStatus.setBackgroundResource(R.drawable.tab_unselected_bg); // Ensure you have a bg drawable or remove this line
//            }
//
//            // 3. Order Value
//            tvOrderValue.setText(String.format(Locale.getDefault(), ": ₹%.2f", order.totalAmount));
//
//            // 4. Items Billed (Using total_quantity from API)
//            tvItemsBilled.setText(": " + order.totalQuantity);
//        }
//    }

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
