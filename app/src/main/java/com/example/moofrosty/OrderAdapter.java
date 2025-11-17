package com.example.moofrosty;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>{

    private List<Order> orderList = new ArrayList<>();
    private final OnOrderClickListener listener;

    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    // --- 2. Update Constructor ---
    public OrderAdapter(OnOrderClickListener listener) {
        this.listener = listener;
    }

    public void updateList(List<Order> newList) {
        this.orderList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.bind(order);

        // --- 3. Set Click Listener ---
        holder.itemView.setOnClickListener(v -> {
            listener.onOrderClick(order);
        });
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

        public void bind(Order order) {
            tvOrderDate.setText(order.orderDate);
            tvOrderStatus.setText(order.status);
            tvOrderValue.setText(String.format(Locale.getDefault(), ": ₹%,.2f", order.orderValue));
            tvItemsBilled.setText(": " + order.itemsBilledString);
        }
    }
}
