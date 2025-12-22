package com.example.moofrosty.ui.enterstoreorders.takeorder;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moofrosty.R;
import com.example.moofrosty.data.model.CartItem;
import com.example.moofrosty.data.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;

    // --- NEW ---
    private CartInteractionListener cartListener;
    private Map<String, CartItem> cartMap = new HashMap<>();

    public interface CartInteractionListener {
        void onAddToCartClick(Product product);

        void onIncrementUnit(Product product);

        void onDecrementUnit(Product product);

        void onIncrementCase(Product product);

        void onDecrementCase(Product product);
    }


    // --- MODIFIED: Constructor ---
    public ProductAdapter(List<Product> productList, CartInteractionListener listener) {
        this.productList = productList;
        this.cartListener = listener; // Save the listener
    }

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

//    @NonNull
//    @Override
//    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
//        return new ProductViewHolder(view);
//    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        // --- MODIFIED: Pass listener ---
        return new ProductViewHolder(view, cartListener);


    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
//        holder.tvProductName.setText(product.getName());
//        holder.tvMrp.setText(product.getMrp());
//        holder.tvRate.setText(product.getRate());
//        holder.tvMargin.setText(product.getMargin());
//        holder.tvStock.setText(product.getStock());
//        holder.tvCapacity.setText(product.getCapacity());
//        holder.tvVmq.setText(product.getVmq());
//        holder.tvL3mrr.setText(product.getL3mrr());
//        holder.tvMtd.setText(product.getMtd());
//
//        holder.imgProduct.setImageResource(product.getImageResId()); // <-- ADDED THIS

        CartItem cartItem = cartMap.get(product.getId());
        holder.bind(product, cartItem); // Call new bind method
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // --- ADD THIS METHOD ---
    // This allows the fragment to update the adapter's data
    public void updateList(List<Product> filteredList) {
        this.productList = filteredList;
        notifyDataSetChanged(); // Refresh the RecyclerView
    }

    public void filterList(List<Product> fullList, String query) {
        List<Product> filteredList = new ArrayList<>();

        if (query == null || query.isEmpty()) {
            // If search is empty, show everything
            filteredList.addAll(fullList);
        } else {
            String filterPattern = query.toLowerCase().trim();

            for (Product item : fullList) {
                // Check if product name contains the search text
                if (item.getName().toLowerCase().contains(filterPattern)) {
                    filteredList.add(item);
                }
            }
        }

        // Reuse the updateList method
        updateList(filteredList);
    }

    public void setCartMap(Map<String, CartItem> newCartMap) {
        this.cartMap = newCartMap;
        notifyDataSetChanged();
    }

//    static class ProductViewHolder extends RecyclerView.ViewHolder {
//        TextView tvProductName, tvMrp, tvRate, tvMargin, tvStock, tvCapacity, tvVmq, tvL3mrr, tvMtd;
//        ImageView imgProduct;
//
//        public ProductViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tvProductName = itemView.findViewById(R.id.tv_product_name);
//            tvMrp = itemView.findViewById(R.id.tv_mrp);
//            tvRate = itemView.findViewById(R.id.tv_rate);
//            tvMargin = itemView.findViewById(R.id.tv_margin);
//            tvStock = itemView.findViewById(R.id.tv_stock);
//            tvCapacity = itemView.findViewById(R.id.tv_capacity);
//            tvVmq = itemView.findViewById(R.id.tv_vmq);
//            tvL3mrr = itemView.findViewById(R.id.tv_l3mrr);
//            tvMtd = itemView.findViewById(R.id.tv_mtd);
//            imgProduct = itemView.findViewById(R.id.img_product);
//        }
//    }

    // --- MODIFIED: ViewHolder ---
    static class ProductViewHolder extends RecyclerView.ViewHolder {
        // (All your view declarations)
        ImageView imgProduct;
        TextView tvProductName, tvMrp, tvRate, tvMargin, tvStock;
        TextView tvCapacity, tvVmq, tvL3mrr, tvMtd;
        Button btnAddToCart;
        LinearLayout quantityControls;
        ImageButton btnCaseMinus, btnCasePlus, btnUnitMinus, btnUnitPlus;
        TextView tvCaseQuantity, tvUnitQuantity, tvSavings, tvNetPrice;

        CartInteractionListener listener;
        Product currentProduct;

        public ProductViewHolder(@NonNull View itemView, CartInteractionListener listener) {
            super(itemView);
            this.listener = listener; // This is now a valid listener

            // (All your findViewById calls)
            imgProduct = itemView.findViewById(R.id.img_product);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvMrp = itemView.findViewById(R.id.tv_mrp);
            tvRate = itemView.findViewById(R.id.tv_rate);
            tvMargin = itemView.findViewById(R.id.tv_margin);
            tvStock = itemView.findViewById(R.id.tv_stock);
            btnAddToCart = itemView.findViewById(R.id.btn_add_to_cart);
            quantityControls = itemView.findViewById(R.id.quantity_controls);
            btnCaseMinus = itemView.findViewById(R.id.btn_case_minus);
            btnCasePlus = itemView.findViewById(R.id.btn_case_plus);
            btnUnitMinus = itemView.findViewById(R.id.btn_unit_minus);
            btnUnitPlus = itemView.findViewById(R.id.btn_unit_plus);
            tvCaseQuantity = itemView.findViewById(R.id.tv_case_quantity);
            tvUnitQuantity = itemView.findViewById(R.id.tv_unit_quantity);
            tvCapacity = itemView.findViewById(R.id.tv_capacity);
            tvVmq = itemView.findViewById(R.id.tv_vmq);
            tvL3mrr = itemView.findViewById(R.id.tv_l3mrr);
            tvMtd = itemView.findViewById(R.id.tv_mtd);
            tvSavings = itemView.findViewById(R.id.tv_savings);
            tvNetPrice = itemView.findViewById(R.id.tv_net_price);

        }

        @SuppressLint("ResourceAsColor")
        public void bind(Product product, CartItem cartItem) {
            this.currentProduct = product;

            // (All your bind logic)
            imgProduct.setImageResource(product.getImageResId());
            tvProductName.setText(product.getName());
            // ... (rest of your bind logic) ...
            tvMrp.setText(product.getMrp());
            tvRate.setText(product.getRate());
            tvMargin.setText(product.getMargin());
            tvStock.setText(product.getStock());
            tvCapacity.setText(product.getCapacity());
            tvVmq.setText(product.getVmq());
            tvL3mrr.setText(product.getL3mrr());
            tvMtd.setText(product.getMtd());

            int currentQuantity = (cartItem == null) ? 0 : cartItem.getQuantity();

            if (currentQuantity == 0) {
                btnAddToCart.setVisibility(View.VISIBLE);
                btnAddToCart.setTextColor(R.color.infoBarBlue);
                quantityControls.setVisibility(View.GONE);
            } else {
                btnAddToCart.setVisibility(View.GONE);
                quantityControls.setVisibility(View.VISIBLE);

                tvCaseQuantity.setText(String.valueOf(cartItem.getCaseQuantity()));
                tvUnitQuantity.setText(String.valueOf(cartItem.getUnitQuantity()));

                tvSavings.setText(String.format(Locale.getDefault(), "₹%,.0f", cartItem.getTotalSavings()));
                tvNetPrice.setText(String.format(Locale.getDefault(), "₹%,.0f", cartItem.getTotalPrice()));
            }
            // --- Set Click Listeners ---
            // This will no longer crash because 'listener' is not null
            btnAddToCart.setOnClickListener(v -> listener.onAddToCartClick(currentProduct));
            btnUnitPlus.setOnClickListener(v -> listener.onIncrementUnit(currentProduct));
            btnUnitMinus.setOnClickListener(v -> listener.onDecrementUnit(currentProduct));
            btnCasePlus.setOnClickListener(v -> listener.onIncrementCase(currentProduct));
            btnCaseMinus.setOnClickListener(v -> listener.onDecrementCase(currentProduct));
            // (Your button enable/disable logic)
            boolean canAddMore = currentQuantity < product.getStockInt();
            btnUnitPlus.setEnabled(canAddMore);
            btnCasePlus.setEnabled((currentQuantity + product.caseSize) <= product.getStockInt());
            boolean canRemove = currentQuantity > 0;
            btnUnitMinus.setEnabled(canRemove);
            btnCaseMinus.setEnabled(canRemove);
            if (product.getStockInt() == 0) {
                btnAddToCart.setEnabled(false);
                btnAddToCart.setText("Out of Stock");
            } else {
                btnAddToCart.setEnabled(true);
                btnAddToCart.setText(R.string.add_to_cart);
            }
        }
    }
}

