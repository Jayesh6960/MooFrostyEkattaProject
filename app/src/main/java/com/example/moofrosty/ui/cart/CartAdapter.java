package com.example.moofrosty.ui.cart;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moofrosty.R;
import com.example.moofrosty.data.model.CartItem;
import com.example.moofrosty.data.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends ListAdapter<CartItem, CartAdapter.CartViewHolder> {
    private final CartInteractionListener listener;

    private List<CartItem> cartList = new ArrayList<>();

    public interface CartInteractionListener {
        void onIncrementUnit(Product product);
        void onDecrementUnit(Product product);
        void onDeleteItem(Product product);
    }

    public CartAdapter(CartInteractionListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    // Helper method to update the list
//    public void updateList(List<CartItem> newList) {
//        submitList(new ArrayList<>(newList));
//        notifyDataSetChanged();
//    }

    public void updateList(List<CartItem> newList) {
        this.cartList = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_product, parent, false);
        return new CartViewHolder(view, listener);
    }
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
       // holder.bind(getItem(position));
        holder.bind(cartList.get(position));
    }
    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvRate, tvMrp, tvQuantity, tvSavingsBadge;
        ImageView imgProduct;
        ImageButton btnPlus, btnMinus, btnDelete;
        CartInteractionListener listener;
        CartItem currentItem;
        public CartViewHolder(@NonNull View itemView, CartInteractionListener listener) {
            super(itemView);
            this.listener = listener;
            tvName = itemView.findViewById(R.id.tv_product_name_cart);
            tvRate = itemView.findViewById(R.id.tv_rate_cart);
            tvMrp = itemView.findViewById(R.id.tv_mrp_cart);
            tvQuantity = itemView.findViewById(R.id.tv_unit_quantity_cart);
            imgProduct = itemView.findViewById(R.id.img_product_cart);
            btnPlus = itemView.findViewById(R.id.btn_unit_plus_cart);
            btnMinus = itemView.findViewById(R.id.btn_unit_minus_cart);
            btnDelete = itemView.findViewById(R.id.btn_delete_cart);
            tvSavingsBadge = itemView.findViewById(R.id.tv_item_savings_badge);
            // Add strikethrough to MRP
            tvMrp.setPaintFlags(tvMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        public void bind(CartItem item) {
            this.currentItem = item;
            Product product = item.getProduct();
            tvName.setText(product.getName());
            tvRate.setText(product.getRate());
            tvMrp.setText(product.getMrp());
            imgProduct.setImageResource(product.getImageResId());
            tvQuantity.setText(String.valueOf(item.getQuantity()));
            // Set savings badge
            double totalSavings = item.getTotalSavings();
            if (totalSavings > 0) {
                tvSavingsBadge.setVisibility(View.VISIBLE);
                tvSavingsBadge.setText(String.format(Locale.getDefault(), "₹%,.0f SAVED", totalSavings));
            } else {
                tvSavingsBadge.setVisibility(View.GONE);
            }
            // Set listeners
            btnPlus.setOnClickListener(v -> listener.onIncrementUnit(product));
            btnMinus.setOnClickListener(v -> listener.onDecrementUnit(product));
            btnDelete.setOnClickListener(v -> listener.onDeleteItem(product));
            btnPlus.setEnabled(item.getQuantity() < product.getStockInt());
            btnMinus.setEnabled(item.getQuantity() > 1);
        }
    }
    private static final DiffUtil.ItemCallback<CartItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<CartItem>() {
                @Override
                public boolean areItemsTheSame(@NonNull CartItem oldItem, @NonNull CartItem newItem) {
                    return oldItem.getProduct().getId() == newItem.getProduct().getId();
                }
                @Override
                public boolean areContentsTheSame(@NonNull CartItem oldItem, @NonNull CartItem newItem) {
                    return oldItem.getQuantity() == newItem.getQuantity();
                }
            };
}
