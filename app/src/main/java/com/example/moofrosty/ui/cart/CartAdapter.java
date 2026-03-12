package com.example.moofrosty.ui.cart;

import android.annotation.SuppressLint;
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

import com.bumptech.glide.Glide;
import com.example.moofrosty.R;
import com.example.moofrosty.core.network.Constants;
import com.example.moofrosty.data.model.CartItem;
import com.example.moofrosty.data.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends ListAdapter<CartItem, CartAdapter.CartViewHolder> {

    private static CartInteractionListener listener;
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

    public void updateList(List<CartItem> newList) {
        this.cartList = new ArrayList<>(newList); // Create a new list copy
        submitList(this.cartList, new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
//        submitList(this.cartList);
//        notifyDataSetChanged(); // <--- CRITICAL FIX: Forces the UI to redraw numbers/savings
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_product, parent, false);
        return new CartViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.bind(cartList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvName, tvRate, tvMrp, tvQty, tvSavedBadge;
        ImageButton btnMinus, btnPlus, btnDelete;

        public CartViewHolder(@NonNull View itemView, CartInteractionListener listener) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_product_cart);
            tvName = itemView.findViewById(R.id.tv_product_name_cart);
            tvRate = itemView.findViewById(R.id.tv_rate_cart);

            // Ensure you have this ID in item_cart_product.xml for MRP
            // If not, use tv_rate_cart for both or add a new TextView
            tvMrp = itemView.findViewById(R.id.tv_mrp_cart); // Assuming you added this

            tvQty = itemView.findViewById(R.id.tv_unit_quantity_cart);
            tvSavedBadge = itemView.findViewById(R.id.tv_item_savings_badge); // Assuming you have a badge TextView

            btnMinus = itemView.findViewById(R.id.btn_unit_minus_cart);
            btnPlus = itemView.findViewById(R.id.btn_unit_plus_cart);
            btnDelete = itemView.findViewById(R.id.btn_delete_cart);

            // Set Strikethrough for MRP once
            if(tvMrp != null) {
                tvMrp.setPaintFlags(tvMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }

        @SuppressLint("SetTextI18n")
        public void bind(CartItem item, CartInteractionListener listener) {
            Product p = item.getProduct();

            // 1. Image
//            String fullImageUrl = "https://moofrosty.ekatta.in/" + p.getImageUrl();
            String fullImageUrl = Constants.BASE_URL + p.getImageUrl();
            Glide.with(itemView.getContext())
                    .load(fullImageUrl)
                    .placeholder(R.drawable.icecategori)
                    .error(R.drawable.icecategori)
                    .into(imgProduct);

            tvName.setText(p.getName());

            // 2. Prices (3 Decimals)
            tvRate.setText(String.format(Locale.US, "₹%.2f", p.ratePrice));
            if (tvMrp != null) {
                tvMrp.setText(String.format(Locale.US, "₹%.2f", p.mrpPrice));
                tvMrp.setVisibility(View.VISIBLE);
            }

            // 3. Savings Badge (Calculated from item)
            double savings = item.getTotalSavings();
            if (tvSavedBadge != null) {
                if (savings > 0) {
                    tvSavedBadge.setVisibility(View.VISIBLE);
                    // Shows e.g. "₹46.150 SAVED"
                    tvSavedBadge.setText(String.format(Locale.US, "₹%.2f SAVED", savings));
                } else {
                    tvSavedBadge.setVisibility(View.GONE);
                }
            }

            // 4. Quantity Display (Case + Unit Logic)
            int cases = item.getCaseQuantity();
            int units = item.getUnitQuantity();

            // --- FIX 2: SHOW CASE/UNIT TEXT INSTEAD OF JUST NUMBER ---
            if (cases > 0 && units > 0) {
                tvQty.setText(cases + " Case + " + units + " Unit");
            } else if (cases > 0) {
                tvQty.setText(cases + "\nCase");
            } else {
                tvQty.setText(units + "\nUnit");
            }

            // 5. Button Logic
            boolean isCaseProduct = "case".equalsIgnoreCase(p.productType);

            btnPlus.setOnClickListener(v -> {
                listener.onIncrementUnit(p);
//                int currentTotal = item.getTotalUnits();
//                int stock = p.getStockInt();
//                // If Case type, we add 'caseSize', if Unit type we add '1'
//                int incrementAmount = isCaseProduct ? p.caseSize : 1;
//                if (currentTotal + incrementAmount <= stock) {
//                    listener.onIncrementUnit(p); // Activity decides Case/Unit logic
//                } else {
//                    // Visual feedback for stock limit
//                    btnPlus.setAlpha(0.5f);
//                }
            });

            btnMinus.setOnClickListener(v -> listener.onDecrementUnit(p));
            btnDelete.setOnClickListener(v -> listener.onDeleteItem(p));

            // Reset Alpha if stock is okay
            int incrementAmount = isCaseProduct ? p.caseSize : 1;
            if (item.getTotalUnits() + incrementAmount <= p.getStockInt()) {
                btnPlus.setAlpha(1.0f);
            } else {
                btnPlus.setAlpha(1.0f);
            }
        }
    }

//    static class CartViewHolder extends RecyclerView.ViewHolder {
//        ImageView imgProduct;
//        TextView tvName, tvPrice, tvQty;
//        ImageButton btnMinus, btnPlus, btnDelete;
//
//        public CartViewHolder(@NonNull View itemView, CartInteractionListener listener) {
//            super(itemView);
//            imgProduct = itemView.findViewById(R.id.img_product_cart);
//            tvName = itemView.findViewById(R.id.tv_product_name_cart);
//            tvPrice = itemView.findViewById(R.id.tv_rate_cart);
//            tvQty = itemView.findViewById(R.id.tv_unit_quantity_cart);
//            btnMinus = itemView.findViewById(R.id.btn_unit_minus_cart);
//            btnPlus = itemView.findViewById(R.id.btn_unit_plus_cart);
//            btnDelete = itemView.findViewById(R.id.btn_delete_cart);
//        }
//
//        public void bind(CartItem item, CartInteractionListener listener) {
//            Product p = item.getProduct();
//            tvName.setText(p.getName());
//
//            // Format price to 3 decimals
//            tvPrice.setText("₹" + String.format(Locale.US, "%.3f", item.getTotalPrice()));
//
//            int cases = item.getCaseQuantity();
//            int units = item.getUnitQuantity();
//
//            if (cases > 0 && units > 0) {
//                tvQty.setText(cases + " Case + " + units + " Unit");
//            } else if (cases > 0) {
//                tvQty.setText(cases + " Case");
//            } else {
//                tvQty.setText(units + " Unit");
//            }
//
//            // Logic to call correct incrementer based on Product Type
//            btnPlus.setOnClickListener(v -> {
//                if ("case".equalsIgnoreCase(p.productType)) {
//                    // Check stock handled in Repository, but we call increment logic
//                    // Since cart view usually has generic +/- buttons, we need to know what to increment.
//                    // Based on your requirement: if type is case, only add case.
//                    // However, Cart interface only has onIncrementUnit in your code snippet.
//                    // You should ideally cast or check type inside the Activity listener implementation
//                    // OR update the interface.
//                    // For now, assuming generic increment maps to correct type in Repo:
//                    listener.onIncrementUnit(p);
//                } else {
//                    listener.onIncrementUnit(p);
//                }
//            });
//
//            // Note: In CartActivity, ensure onIncrementUnit calls:
//            // if (p.type == case) viewModel.incrementCase else viewModel.incrementUnit
//
//            btnMinus.setOnClickListener(v -> listener.onDecrementUnit(p));
//            btnDelete.setOnClickListener(v -> listener.onDeleteItem(p));
//        }
//    }

    private static final DiffUtil.ItemCallback<CartItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<CartItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull CartItem oldItem, @NonNull CartItem newItem) {
            return oldItem.getProduct().getId().equals(newItem.getProduct().getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull CartItem oldItem, @NonNull CartItem newItem) {
            return oldItem.getCaseQuantity() == newItem.getCaseQuantity()
                    && oldItem.getUnitQuantity() == newItem.getUnitQuantity()
                    && oldItem.getTotalUnits() == newItem.getTotalUnits();
        }
    };
}


          // above code just for home api chnage that time do

//    static class CartViewHolder extends RecyclerView.ViewHolder {
//        TextView tvName, tvRate, tvMrp, tvQuantityInfo, tvTotalPrice, tvSavingsBadge;
//        ImageView imgProduct;
//        ImageButton btnPlus, btnMinus, btnDelete;
//
//        public CartViewHolder(@NonNull View itemView, CartInteractionListener listener) {
//            super(itemView);
//            tvName = itemView.findViewById(R.id.tv_product_name_cart);
//            tvRate = itemView.findViewById(R.id.tv_rate_cart);
//            tvMrp = itemView.findViewById(R.id.tv_mrp_cart);
//            tvQuantityInfo = itemView.findViewById(R.id.tv_unit_quantity_cart); // Note: reusing ID for quantity string
//            // Note: If your XML uses different IDs for Total Price, update here.
//            // Based on your XML snippet, you might not be showing row total, but quantity controls.
//
//            imgProduct = itemView.findViewById(R.id.img_product_cart);
//            btnPlus = itemView.findViewById(R.id.btn_unit_plus_cart);
//            btnMinus = itemView.findViewById(R.id.btn_unit_minus_cart);
//            btnDelete = itemView.findViewById(R.id.btn_delete_cart);
//            tvSavingsBadge = itemView.findViewById(R.id.tv_item_savings_badge);
//
//            tvMrp.setPaintFlags(tvMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//        }
//
//        public void bind(CartItem item) {
//            Product product = item.getProduct();
//            tvName.setText(product.getName());
//            tvRate.setText(product.getRate());
//            tvMrp.setText(product.getMrp());
//
//            // Image
////            String imageUrl = "https://moofrosty.ekatta.in/" + product.getImageUrl();
////            Glide.with(itemView.getContext())
////                    .load(imageUrl)
////                    .placeholder(R.drawable.icecategori)
////                    .into(imgProduct);
//
//            // Quantity Display Logic (Case vs Unit)
//            int cases = item.getCaseQuantity();
//            int units = item.getUnitQuantity();
//            StringBuilder qtyString = new StringBuilder();
//
//            // If just showing raw number in the center textview like your XML:
//            // tvQuantityInfo.setText(String.valueOf(item.getQuantity()));
//
//            // If showing detailed info (Case + Unit):
//            if (cases > 0) qtyString.append(cases).append(" Case ");
//            if (units > 0) {
//                if (cases > 0) qtyString.append("+ ");
//                qtyString.append(units).append(" Units");
//            }
//            if (qtyString.length() == 0) qtyString.append(item.getQuantity()); // Fallback to raw qty
//
//            // Find correct textview for this string. If using small center textview, stick to raw qty.
//            // Assuming your XML has a center textview for count:
//            tvQuantityInfo.setText(String.valueOf(item.getQuantity()));
//
//            // Savings
//            double totalSavings = item.getTotalSavings();
//            if (totalSavings > 0) {
//                tvSavingsBadge.setVisibility(View.VISIBLE);
//                tvSavingsBadge.setText(String.format(Locale.getDefault(), "₹%,.0f SAVED", totalSavings));
//            } else {
//                tvSavingsBadge.setVisibility(View.GONE);
//            }
//
//            // Click Listeners
//            btnPlus.setOnClickListener(v -> listener.onIncrementUnit(product)); // Or incrementCase based on logic
//            btnMinus.setOnClickListener(v -> listener.onDecrementUnit(product));
//            btnDelete.setOnClickListener(v -> listener.onDeleteItem(product));
//
//            // Enable/Disable logic
//            int stock = product.getStockInt();
//            btnPlus.setEnabled(item.getQuantity() < stock);
//            btnMinus.setEnabled(item.getQuantity() > 0);
//        }
//    }

        // above code just for home api chnage that time do

//    private static final DiffUtil.ItemCallback<CartItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<CartItem>() {
//        @Override
//        public boolean areItemsTheSame(@NonNull CartItem oldItem, @NonNull CartItem newItem) {
//            return oldItem.getProduct().getId().equals(newItem.getProduct().getId());
//        }
//        @Override
//        public boolean areContentsTheSame(@NonNull CartItem oldItem, @NonNull CartItem newItem) {
//           // return oldItem.getQuantity() == newItem.getQuantity(); ///   this old work  getCaseQuantity  befor this
//            return oldItem.getCaseQuantity() == newItem.getCaseQuantity()
//                    && oldItem.getUnitQuantity() == newItem.getUnitQuantity();
//        }
//    };
//}

//    private final CartInteractionListener listener;
//
//    private List<CartItem> cartList = new ArrayList<>();
//
//    public interface CartInteractionListener {
//        void onIncrementUnit(Product product);
//        void onDecrementUnit(Product product);
//        void onDeleteItem(Product product);
//    }
//
//    public CartAdapter(CartInteractionListener listener) {
//        super(DIFF_CALLBACK);
//        this.listener = listener;
//    }
//
//    // Helper method to update the list
////    public void updateList(List<CartItem> newList) {
////        submitList(new ArrayList<>(newList));
////        notifyDataSetChanged();
////    }
//
//    public void updateList(List<CartItem> newList) {
//        this.cartList = newList;
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public int getItemCount() {
//        return cartList.size();
//    }
//
//    @NonNull
//    @Override
//    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_product, parent, false);
//        return new CartViewHolder(view, listener);
//    }
//    @Override
//    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
//       // holder.bind(getItem(position));
//        holder.bind(cartList.get(position));
//    }
//    static class CartViewHolder extends RecyclerView.ViewHolder {
//        TextView tvName, tvRate, tvMrp, tvQuantity, tvSavingsBadge;
//        ImageView imgProduct;
//        ImageButton btnPlus, btnMinus, btnDelete;
//        CartInteractionListener listener;
//        CartItem currentItem;
//        public CartViewHolder(@NonNull View itemView, CartInteractionListener listener) {
//            super(itemView);
//            this.listener = listener;
//            tvName = itemView.findViewById(R.id.tv_product_name_cart);
//            tvRate = itemView.findViewById(R.id.tv_rate_cart);
//            tvMrp = itemView.findViewById(R.id.tv_mrp_cart);
//            tvQuantity = itemView.findViewById(R.id.tv_unit_quantity_cart);
//            imgProduct = itemView.findViewById(R.id.img_product_cart);
//            btnPlus = itemView.findViewById(R.id.btn_unit_plus_cart);
//            btnMinus = itemView.findViewById(R.id.btn_unit_minus_cart);
//            btnDelete = itemView.findViewById(R.id.btn_delete_cart);
//            tvSavingsBadge = itemView.findViewById(R.id.tv_item_savings_badge);
//            // Add strikethrough to MRP
//            tvMrp.setPaintFlags(tvMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//        }
//        public void bind(CartItem item) {
//            this.currentItem = item;
//            Product product = item.getProduct();
//            tvName.setText(product.getName());
//            tvRate.setText(product.getRate());
//            tvMrp.setText(product.getMrp());
//            imgProduct.setImageResource(product.getImageResId());
//            tvQuantity.setText(String.valueOf(item.getQuantity()));
//            // Set savings badge
//            double totalSavings = item.getTotalSavings();
//            if (totalSavings > 0) {
//                tvSavingsBadge.setVisibility(View.VISIBLE);
//                tvSavingsBadge.setText(String.format(Locale.getDefault(), "₹%,.0f SAVED", totalSavings));
//            } else {
//                tvSavingsBadge.setVisibility(View.GONE);
//            }
//            // Set listeners
//            btnPlus.setOnClickListener(v -> listener.onIncrementUnit(product));
//            btnMinus.setOnClickListener(v -> listener.onDecrementUnit(product));
//            btnDelete.setOnClickListener(v -> listener.onDeleteItem(product));
//            btnPlus.setEnabled(item.getQuantity() < product.getStockInt());
//            btnMinus.setEnabled(item.getQuantity() > 1);
//        }
//    }
//    private static final DiffUtil.ItemCallback<CartItem> DIFF_CALLBACK =
//            new DiffUtil.ItemCallback<CartItem>() {
//                @Override
//                public boolean areItemsTheSame(@NonNull CartItem oldItem, @NonNull CartItem newItem) {
//                    return oldItem.getProduct().getId() == newItem.getProduct().getId();
//                }
//                @Override
//                public boolean areContentsTheSame(@NonNull CartItem oldItem, @NonNull CartItem newItem) {
//                    return oldItem.getQuantity() == newItem.getQuantity();
//                }
//            };
//}
