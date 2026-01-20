package com.example.moofrosty.ui.enterstoreorders.takeorder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moofrosty.R;
import com.example.moofrosty.data.model.CartItem;
import com.example.moofrosty.data.model.Product;
import com.example.moofrosty.data.model.ProductApiModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<ProductApiModel> productList;
    private CartInteractionListener cartListener;
    private Map<String, CartItem> cartMap; // Map ProductID (String) to CartItem

    // Interface to communicate with Fragment/ViewModel
    public interface CartInteractionListener {
        void onAddToCartClick(ProductApiModel product);
        void onIncrementUnit(ProductApiModel product);
        void onDecrementUnit(ProductApiModel product);
        void onIncrementCase(ProductApiModel product);
        void onDecrementCase(ProductApiModel product);
    }

    public ProductAdapter(List<ProductApiModel> productList, CartInteractionListener listener) {
        this.productList = productList;
        this.cartListener = listener;
    }

    public void updateList(List<ProductApiModel> newList) {
        this.productList = newList;
        notifyDataSetChanged();
    }

    public void setCartMap(Map<String, CartItem> newCartMap) {
        this.cartMap = newCartMap;
        notifyDataSetChanged();
    }

    public void filterList(List<ProductApiModel> fullList, String query) {
        List<ProductApiModel> filteredList = new ArrayList<>();
        if (query == null || query.isEmpty()) {
            filteredList.addAll(fullList);
        } else {
            String filterPattern = query.toLowerCase().trim();
            for (ProductApiModel item : fullList) {
                if (item.productName != null && item.productName.toLowerCase().contains(filterPattern)) {
                    filteredList.add(item);
                }
            }
        }
        updateList(filteredList);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view, cartListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductApiModel product = productList.get(position);
        CartItem cartItem = null;
        if (cartMap != null) {
            cartItem = cartMap.get(String.valueOf(product.productId));
        }
        holder.bind(product, cartItem);
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvProductName, tvMrp, tvRate, tvMargin, tvStock, tvCapacity, caseunitnumber;
        MaterialButton btnAddToCart;
        LinearLayout quantityControls;
        ImageButton btnCaseMinus, btnCasePlus, btnUnitMinus, btnUnitPlus;
        TextView tvCaseQuantity, tvUnitQuantity, tvSavings, tvNetPrice;
        CartInteractionListener listener;
        ProductApiModel currentProduct;

        public ProductViewHolder(@NonNull View itemView, CartInteractionListener listener) {
            super(itemView);
            this.listener = listener;
            imgProduct = itemView.findViewById(R.id.img_product);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvMrp = itemView.findViewById(R.id.tv_mrp);
            tvRate = itemView.findViewById(R.id.tv_rate);
            tvMargin = itemView.findViewById(R.id.tv_margin);
            tvStock = itemView.findViewById(R.id.tv_stock);
            tvCapacity = itemView.findViewById(R.id.tv_capacity);
            btnAddToCart = itemView.findViewById(R.id.btn_add_to_cart);
            quantityControls = itemView.findViewById(R.id.quantity_controls);
            btnCaseMinus = itemView.findViewById(R.id.btn_case_minus);
            btnCasePlus = itemView.findViewById(R.id.btn_case_plus);
            btnUnitMinus = itemView.findViewById(R.id.btn_unit_minus);
            btnUnitPlus = itemView.findViewById(R.id.btn_unit_plus);
            tvCaseQuantity = itemView.findViewById(R.id.tv_case_quantity);
            tvUnitQuantity = itemView.findViewById(R.id.tv_unit_quantity);
            tvSavings = itemView.findViewById(R.id.tv_savings);
            tvNetPrice = itemView.findViewById(R.id.tv_net_price);
            caseunitnumber = itemView.findViewById(R.id.caseunitnumber);
    }

        @SuppressLint("SetTextI18n")
        public void bind(ProductApiModel product, CartItem cartItem) {
            this.currentProduct = product;
            Context context = itemView.getContext();

            tvProductName.setText(product.productName);
            tvMrp.setText("₹" + product.getMrp());
            tvRate.setText("₹" + product.getSellingPrice());
            tvMargin.setText(product.getMargin() + "%");
            tvStock.setText(product.getStock());
            tvCapacity.setText(product.productWeight);
            caseunitnumber.setText("1 Case = "+product.getUnit()+" Unit");

            String imageUrl = "https://moofrosty.ekatta.in/" + product.productImage;
            Glide.with(context).load(imageUrl).placeholder(R.drawable.icecategori).into(imgProduct);

            int currentQuantity = (cartItem == null) ? 0 : cartItem.getQuantity();

            if (currentQuantity == 0) {
                btnAddToCart.setVisibility(View.VISIBLE);
                quantityControls.setVisibility(View.GONE);

                int stock = 0;
                try { stock = Integer.parseInt(product.getStock()); } catch (Exception e) {}

                if (stock <= 0) {
                    btnAddToCart.setEnabled(false);
                    btnAddToCart.setText("Out of Stock");
                    btnAddToCart.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
                } else {
                    btnAddToCart.setEnabled(true);
                    btnAddToCart.setText(R.string.add_to_cart);
                    btnAddToCart.setTextColor(ContextCompat.getColor(context, R.color.infoBarBlue));
                }
            } else {
                btnAddToCart.setVisibility(View.GONE);
                quantityControls.setVisibility(View.VISIBLE);
                tvCaseQuantity.setText(String.valueOf(cartItem.getCaseQuantity()));
                tvUnitQuantity.setText(String.valueOf(cartItem.getUnitQuantity()));
                tvSavings.setText(String.format(Locale.getDefault(), "₹%,.0f", cartItem.getTotalSavings()));
                tvNetPrice.setText(String.format(Locale.getDefault(), "₹%,.0f", cartItem.getTotalPrice()));
            }

            // --- Product Type Logic ---
            // If "case", hide unit buttons
            if ("case".equalsIgnoreCase(product.productType)) {
                btnUnitPlus.setVisibility(View.INVISIBLE);
                btnUnitMinus.setVisibility(View.INVISIBLE);
            } else {
                btnUnitPlus.setVisibility(View.VISIBLE);
                btnUnitMinus.setVisibility(View.VISIBLE);
            }

            btnAddToCart.setOnClickListener(v -> listener.onAddToCartClick(currentProduct));
            btnCasePlus.setOnClickListener(v -> listener.onIncrementCase(currentProduct));
            btnCaseMinus.setOnClickListener(v -> listener.onDecrementCase(currentProduct));
            btnUnitPlus.setOnClickListener(v -> listener.onIncrementUnit(currentProduct));
            btnUnitMinus.setOnClickListener(v -> listener.onDecrementUnit(currentProduct));
        }
    }

//
//    private List<Product> productList;
//    private CartInteractionListener cartListener;
//    private Map<String, CartItem> cartMap = new HashMap<>();
//
//    public interface CartInteractionListener {
//        void onAddToCartClick(Product product);
//
//        void onIncrementUnit(Product product);
//
//        void onDecrementUnit(Product product);
//
//        void onIncrementCase(Product product);
//
//        void onDecrementCase(Product product);
//    }
//
//
//    // --- MODIFIED: Constructor ---
//    public ProductAdapter(List<Product> productList, CartInteractionListener listener) {
//        this.productList = productList;
//        this.cartListener = listener; // Save the listener
//    }
//
//    public ProductAdapter(List<Product> productList) {
//        this.productList = productList;
//    }
//
////    @NonNull
////    @Override
////    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
////        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
////        return new ProductViewHolder(view);
////    }
//
//    @NonNull
//    @Override
//    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
//        // --- MODIFIED: Pass listener ---
//        return new ProductViewHolder(view, cartListener);
//
//
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
//        Product product = productList.get(position);
////        holder.tvProductName.setText(product.getName());
////        holder.tvMrp.setText(product.getMrp());
////        holder.tvRate.setText(product.getRate());
////        holder.tvMargin.setText(product.getMargin());
////        holder.tvStock.setText(product.getStock());
////        holder.tvCapacity.setText(product.getCapacity());
////        holder.tvVmq.setText(product.getVmq());
////        holder.tvL3mrr.setText(product.getL3mrr());
////        holder.tvMtd.setText(product.getMtd());
////
////        holder.imgProduct.setImageResource(product.getImageResId()); // <-- ADDED THIS
//
//        CartItem cartItem = cartMap.get(product.getId());
//        holder.bind(product, cartItem); // Call new bind method
//    }
//
//    @Override
//    public int getItemCount() {
//        return productList.size();
//    }
//
//    // --- ADD THIS METHOD ---
//    // This allows the fragment to update the adapter's data
//    public void updateList(List<Product> filteredList) {
//        this.productList = filteredList;
//        notifyDataSetChanged(); // Refresh the RecyclerView
//    }
//
//    public void filterList(List<Product> fullList, String query) {
//        List<Product> filteredList = new ArrayList<>();
//
//        if (query == null || query.isEmpty()) {
//            // If search is empty, show everything
//            filteredList.addAll(fullList);
//        } else {
//            String filterPattern = query.toLowerCase().trim();
//
//            for (Product item : fullList) {
//                // Check if product name contains the search text
//                if (item.getName().toLowerCase().contains(filterPattern)) {
//                    filteredList.add(item);
//                }
//            }
//        }
//
//        // Reuse the updateList method
//        updateList(filteredList);
//    }
//
//    public void setCartMap(Map<String, CartItem> newCartMap) {
//        this.cartMap = newCartMap;
//        notifyDataSetChanged();
//    }
//
////    static class ProductViewHolder extends RecyclerView.ViewHolder {
////        TextView tvProductName, tvMrp, tvRate, tvMargin, tvStock, tvCapacity, tvVmq, tvL3mrr, tvMtd;
////        ImageView imgProduct;
////
////        public ProductViewHolder(@NonNull View itemView) {
////            super(itemView);
////            tvProductName = itemView.findViewById(R.id.tv_product_name);
////            tvMrp = itemView.findViewById(R.id.tv_mrp);
////            tvRate = itemView.findViewById(R.id.tv_rate);
////            tvMargin = itemView.findViewById(R.id.tv_margin);
////            tvStock = itemView.findViewById(R.id.tv_stock);
////            tvCapacity = itemView.findViewById(R.id.tv_capacity);
////            tvVmq = itemView.findViewById(R.id.tv_vmq);
////            tvL3mrr = itemView.findViewById(R.id.tv_l3mrr);
////            tvMtd = itemView.findViewById(R.id.tv_mtd);
////            imgProduct = itemView.findViewById(R.id.img_product);
////        }
////    }
//
//    // --- MODIFIED: ViewHolder ---
//    static class ProductViewHolder extends RecyclerView.ViewHolder {
//        // (All your view declarations)
//        ImageView imgProduct;
//        TextView tvProductName, tvMrp, tvRate, tvMargin, tvStock;
//        TextView tvCapacity, tvVmq, tvL3mrr, tvMtd;
//        Button btnAddToCart;
//        LinearLayout quantityControls;
//        ImageButton btnCaseMinus, btnCasePlus, btnUnitMinus, btnUnitPlus;
//        TextView tvCaseQuantity, tvUnitQuantity, tvSavings, tvNetPrice;
//
//        CartInteractionListener listener;
//        Product currentProduct;
//
//        public ProductViewHolder(@NonNull View itemView, CartInteractionListener listener) {
//            super(itemView);
//            this.listener = listener; // This is now a valid listener
//
//            // (All your findViewById calls)
//            imgProduct = itemView.findViewById(R.id.img_product);
//            tvProductName = itemView.findViewById(R.id.tv_product_name);
//            tvMrp = itemView.findViewById(R.id.tv_mrp);
//            tvRate = itemView.findViewById(R.id.tv_rate);
//            tvMargin = itemView.findViewById(R.id.tv_margin);
//            tvStock = itemView.findViewById(R.id.tv_stock);
//            btnAddToCart = itemView.findViewById(R.id.btn_add_to_cart);
//            quantityControls = itemView.findViewById(R.id.quantity_controls);
//            btnCaseMinus = itemView.findViewById(R.id.btn_case_minus);
//            btnCasePlus = itemView.findViewById(R.id.btn_case_plus);
//            btnUnitMinus = itemView.findViewById(R.id.btn_unit_minus);
//            btnUnitPlus = itemView.findViewById(R.id.btn_unit_plus);
//            tvCaseQuantity = itemView.findViewById(R.id.tv_case_quantity);
//            tvUnitQuantity = itemView.findViewById(R.id.tv_unit_quantity);
//            tvCapacity = itemView.findViewById(R.id.tv_capacity);
//            tvSavings = itemView.findViewById(R.id.tv_savings);
//            tvNetPrice = itemView.findViewById(R.id.tv_net_price);
//
//        }
//
//        @SuppressLint("ResourceAsColor")
//        public void bind(Product product, CartItem cartItem) {
//            this.currentProduct = product;
//
//            // (All your bind logic)
//            imgProduct.setImageResource(product.getImageResId());
//            tvProductName.setText(product.getName());
//            // ... (rest of your bind logic) ...
//            tvMrp.setText(product.getMrp());
//            tvRate.setText(product.getRate());
//            tvMargin.setText(product.getMargin());
//            tvStock.setText(product.getStock());
//            tvCapacity.setText(product.getCapacity());
//            tvVmq.setText(product.getVmq());
//            tvL3mrr.setText(product.getL3mrr());
//            tvMtd.setText(product.getMtd());
//
//            int currentQuantity = (cartItem == null) ? 0 : cartItem.getQuantity();
//
//            if (currentQuantity == 0) {
//                btnAddToCart.setVisibility(View.VISIBLE);
//                btnAddToCart.setTextColor(R.color.infoBarBlue);
//                quantityControls.setVisibility(View.GONE);
//            } else {
//                btnAddToCart.setVisibility(View.GONE);
//                quantityControls.setVisibility(View.VISIBLE);
//
//                tvCaseQuantity.setText(String.valueOf(cartItem.getCaseQuantity()));
//                tvUnitQuantity.setText(String.valueOf(cartItem.getUnitQuantity()));
//
//                tvSavings.setText(String.format(Locale.getDefault(), "₹%,.0f", cartItem.getTotalSavings()));
//                tvNetPrice.setText(String.format(Locale.getDefault(), "₹%,.0f", cartItem.getTotalPrice()));
//            }
//            // --- Set Click Listeners ---
//            // This will no longer crash because 'listener' is not null
//            btnAddToCart.setOnClickListener(v -> listener.onAddToCartClick(currentProduct));
//            btnUnitPlus.setOnClickListener(v -> listener.onIncrementUnit(currentProduct));
//            btnUnitMinus.setOnClickListener(v -> listener.onDecrementUnit(currentProduct));
//            btnCasePlus.setOnClickListener(v -> listener.onIncrementCase(currentProduct));
//            btnCaseMinus.setOnClickListener(v -> listener.onDecrementCase(currentProduct));
//            // (Your button enable/disable logic)
//            boolean canAddMore = currentQuantity < product.getStockInt();
//            btnUnitPlus.setEnabled(canAddMore);
//            btnCasePlus.setEnabled((currentQuantity + product.caseSize) <= product.getStockInt());
//            boolean canRemove = currentQuantity > 0;
//            btnUnitMinus.setEnabled(canRemove);
//            btnCaseMinus.setEnabled(canRemove);
//            if (product.getStockInt() == 0) {
//                btnAddToCart.setEnabled(false);
//                btnAddToCart.setText("Out of Stock");
//            } else {
//                btnAddToCart.setEnabled(true);
//                btnAddToCart.setText(R.string.add_to_cart);
//            }
//        }
//    }
}

