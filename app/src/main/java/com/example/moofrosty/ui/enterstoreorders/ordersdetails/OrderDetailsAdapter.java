package com.example.moofrosty.ui.enterstoreorders.ordersdetails;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moofrosty.R;
import com.example.moofrosty.data.model.CartItem;
import com.example.moofrosty.data.model.Product;

import java.util.List;
import java.util.Locale;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder>{

    private final List<CartItem> itemList;

    public OrderDetailsAdapter(List<CartItem> itemList) {
        this.itemList = itemList;
    }
    @NonNull
    @Override
    public OrderDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsAdapter.ViewHolder holder, int position) {
        holder.bind(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvProductName, tvMrp, tvBilledQty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_product);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvMrp = itemView.findViewById(R.id.tv_mrp);
            tvBilledQty = itemView.findViewById(R.id.tv_billed_qty);
        }

        public void bind(CartItem item) {
            Product product = item.getProduct();
            tvProductName.setText(product.getName());
            tvMrp.setText(product.getRate()); // Show the rate they paid
            imgProduct.setImageResource(product.getImageResId());

//            String qtyString = String.format(Locale.getDefault(), "%d/%d Unit(s)",
//                    item.getQuantity(), item.getQuantity());
//            tvBilledQty.setText(qtyString);
        }
    }
}
