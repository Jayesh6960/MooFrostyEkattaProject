package com.example.moofrosty.ui.cart;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moofrosty.R;
import com.example.moofrosty.core.utils.NetworkUtil;
import com.example.moofrosty.data.local.SessionManager;
import com.example.moofrosty.data.model.Product;
import com.example.moofrosty.data.model.Store;
import com.example.moofrosty.ui.enterstoreorders.takeorder.TakeOrderActivity;
import com.example.moofrosty.ui.enterstoreorders.takeorder.TakeOrderActivityViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements CartAdapter.CartInteractionListener {

    private CartViewModel cartViewModel;
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private TextView tvTotalMrp, tvTotalSavings, tvTotalBill, tvCheckoutPrice, tvRetailerMargin, tvSchemeDiscount;
    private TextView toolbarTitle;
    MaterialCardView checkoutBar;
    RelativeLayout relativelayoutbottom;
    TextView btnCheckout;
    private ProgressBar progressBar;
    private SessionManager sessionManager;
    private TakeOrderActivityViewModel takeOrderActivityViewModel;
    private Store currentStore;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_cart);
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        windowInsetsController.setAppearanceLightStatusBars(true);
        AppBarLayout appBarLayout = findViewById(R.id.app_bar_layout);
        relativelayoutbottom = findViewById(R.id.relativelayoutbottom);

        // 2. Window Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.app_bar_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), systemBars.bottom);
            return insets;
        });

        // 2. Init Views
        if (getIntent().getSerializableExtra("STORE_DATA") != null) {
            currentStore = (Store) getIntent().getSerializableExtra("STORE_DATA");
        }
        initViews();

        sessionManager = new SessionManager(this);
//        progressBar = new ProgressDialog(this);
//        progressBar.setMessage("Placing Order...");
//        progressBar.setCancelable(false);

        // 3. ViewModel & Session
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        // Replace with actual session getters when available
        //    int userId = 1;
        //    int shopId = 3;

        int userId = sessionManager.getUserId();
        int shopId = sessionManager.getShopId();

        if (userId == 0 || shopId == 0) {
            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        cartViewModel.setSessionData(sessionManager.getToken(), userId, shopId);

        // 4. Setup Recycler
        setupRecyclerView();

        // 5. Setup Observers
        setupObservers();

        // 6. Click Listeners
        btnCheckout.setOnClickListener(v -> {
            handleCheckout();
//            Intent intent = new Intent(CartActivity.this, TakeOrderActivity.class);
//            intent.putExtra("IS_ORDER_TAKEN", true); // ✅ IMPORTANT
//            startActivity(intent);
        });
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }

    private void handleCheckout() {
        if (!NetworkUtil.isNetworkAvailable(this)) {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }
        if (adapter.getItemCount() == 0) {
            Toast.makeText(this, "Cart is Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        cartViewModel.checkout();

    }

    private void setupObservers() {
        // A. Cart Items
        cartViewModel.getCartMap().observe(this, cartMap -> {
            adapter.updateList(new ArrayList<>(cartMap.values()));
        });

        // B. Totals - FIXED TO SHOW 3 DECIMAL PLACES (%.3f)
        cartViewModel.getCartTotals().observe(this, totals -> {
            // Using %.3f prevents rounding (e.g., 416.650 stays 416.650)
            tvTotalMrp.setText(String.format(Locale.US, "Total MRP : ₹%.2f", totals.totalMrp));
            tvTotalSavings.setText(String.format(Locale.US, "Total Savings : ₹%.2f", totals.totalSavings));
            tvTotalBill.setText(String.format(Locale.US, "Estimated Bill Value : ₹%.2f", totals.totalPrice));

            // Bottom Checkout Bar Total
            tvCheckoutPrice.setText(String.format(Locale.US, "Total: ₹%.2f", totals.totalPrice));

            tvRetailerMargin.setText(String.format(Locale.US, "Retailer Margin : ₹%.2f", totals.totalSavings));
            tvSchemeDiscount.setText("Scheme Discount : ₹0.000");

            if (totals.uniqueItemCount == 0) {
                toolbarTitle.setText("Cart");
            } else {
                toolbarTitle.setText(String.format(Locale.US, "Cart (%d items)", totals.uniqueItemCount));
            }
        });

        // C. API Result
        cartViewModel.getCheckoutResult().observe(this, resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case LOADING:
                        progressBar.setVisibility(View.VISIBLE);
                        btnCheckout.setEnabled(false);
                        break;
                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        btnCheckout.setEnabled(false);
                        //    Toast.makeText(this, "Order Placed Successfully!", Toast.LENGTH_LONG).show();
                        showSuccessDialog();
                        //    finish();
                        break;
                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        btnCheckout.setEnabled(true);
                        Toast.makeText(this, "Something Error", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }
// are per the requiremnt
    private void setupRecyclerView() {
        adapter = new CartAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_cart_items);
        tvTotalMrp = findViewById(R.id.tv_total_mrp);
        tvTotalSavings = findViewById(R.id.tv_total_savings);
        tvTotalBill = findViewById(R.id.tv_total_bill);
        tvCheckoutPrice = findViewById(R.id.tv_checkout_price);
        tvRetailerMargin = findViewById(R.id.tv_retailer_margin);
        tvSchemeDiscount = findViewById(R.id.tv_scheme_discount);
        toolbarTitle = findViewById(R.id.toolbar_title);
        progressBar = findViewById(R.id.cart_progress_bar);
        btnCheckout = findViewById(R.id.btn_checkout); // Can handle click on the Text or parent Layout
    }

//    private void setupWindowInsets() {
//        View appBar = findViewById(R.id.app_bar_layout);
//        if (appBar != null) {
//            ViewCompat.setOnApplyWindowInsetsListener(appBar, (v, insets) -> {
//                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//                v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom());
//                return insets;
//            });
//        }
//        View bottomLayout = findViewById(R.id.relativelayoutbottom); // Bottom bar ID from your XML
//        if (bottomLayout != null) {
//            ViewCompat.setOnApplyWindowInsetsListener(bottomLayout, (v, insets) -> {
//                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//                v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), systemBars.bottom);
//                return insets;
//            });
//        }
//    }

    private void showSuccessDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Inflate the custom layout
        View dialogView = getLayoutInflater().inflate(R.layout.layout_order_success_overlay, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        // Make background transparent so CardView corners look rounded
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        // Find button and set click listener
        Button btnDone = dialogView.findViewById(R.id.btn_continue_shopping);
//        boolean isOrderTaken = false;
//        btnDone.setOnClickListener(v -> {
//            dialog.dismiss();
//            Intent intent = new Intent(CartActivity.this, TakeOrderActivity.class);
//            intent.putExtra("IS_ORDER_TAKEN", isOrderTaken);  // or your variable
//            startActivity(intent);
//            finish(); // Finish activity when button clicked
//        });

//        SessionManager sessionManager = new SessionManager(CartActivity.this);

//        btnDone.setOnClickListener(v -> {
//            dialog.dismiss();
//
//            sessionManager.setOrderTaken(true);  // ✅ set ONLY on Done click
//
//            Intent intent = new Intent(CartActivity.this, TakeOrderActivity.class);
//            if (currentStore != null) {
//                intent.putExtra("STORE_DATA", currentStore);
//            }
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//            finish();
//
////            Intent intent = new Intent(CartActivity.this, TakeOrderActivity.class);
////            intent.putExtra("IS_ORDER_TAKEN", true);  // optional (can keep or remove)
////            startActivity(intent);
////            finish();
//        });

        btnDone.setOnClickListener(v -> {
            dialog.dismiss();

            // [HIGHLIGHT] Save Order Taken State and Shop ID to Session
            sessionManager.setOrderTaken(true);
//            sessionManager.setLastCheckoutReason("Order Taken"); // Helper for logic
            sessionManager.saveShopId(sessionManager.getShopId());

            Intent intent = new Intent(CartActivity.this, TakeOrderActivity.class);
            if (currentStore != null) {
                intent.putExtra("STORE_DATA", currentStore);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    // Adapter Callbacks
    @Override
    public void onIncrementUnit(Product p) {
        if ("case".equalsIgnoreCase(p.productType)) {
            cartViewModel.incrementCase(p);
        } else {
            cartViewModel.incrementUnit(p);
        }
    }

    @Override
    public void onDecrementUnit(Product p) {
        if ("case".equalsIgnoreCase(p.productType)) {
            cartViewModel.decrementCase(p);
        } else {
            cartViewModel.decrementUnit(p);
        }
    }

    @Override public void onDeleteItem(Product p) {
        cartViewModel.removeFromCart(p);
    }

//    @Override public void onDeleteItem(Product p) {
//        cartViewModel.removeFromCart(p);
//    }
//    private ActivityResultLauncher<Intent> takeOrderLauncher =
//            registerForActivityResult(
//                    new ActivityResultContracts.StartActivityForResult(),
//                    result -> {
//                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
//
//                            boolean isOrderTaken =
//                                    result.getData().getBooleanExtra("IS_ORDER_TAKEN", false);
//
//                            String reason =
//                                    result.getData().getStringExtra("REASON");
//
//                            String shopId =
//                                    result.getData().getStringExtra("SHOP_ID");
//
//                            Log.d("FLOW", "Returned → " + isOrderTaken + ", " + reason);
//
//                            // ✅ Restore store safely
//
//                            if (currentStore.getStoreName() != null) {
//                                currentStore.setShopId(currentStore.getShopId());
//                            }
//
//                            // ✅ NOW open checkout dialog
//                            openFinalCheckout(isOrderTaken, reason);
//                        }
//                    }
//            );
//
//    private void openFinalCheckout(boolean isOrderTaken, String reason) {
//        Intent intent = new Intent(CartActivity.this, TakeOrderActivity.class);
//        intent.putExtra("IS_ORDER_TAKEN", isOrderTaken);
//        intent.putExtra("REASON", reason);
//        startActivity(intent);
//    }
}



///   abpove code check for deleteitem and that intent

//      //  checkoutBar = findViewById(R.id.checkout_bar);
//        relativelayoutbottom = findViewById(R.id.main);
//        ViewCompat.setOnApplyWindowInsetsListener(relativelayoutbottom, (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), systemBars.bottom);
//            return insets;
//        });
//        // check out code
//        TextView btnCheckout = findViewById(R.id.btn_checkout);
//        btnCheckout.setOnClickListener(v -> {
//            cartViewModel.checkout(); // Call the checkout method
//            finish(); // Close this activity
//        });
//
//        // --- FIND TOOLBAR TITLE ---
//        toolbarTitle = findViewById(R.id.toolbar_title);
//        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
//
//
//        // --- This gets the ViewModel with the SHARED repository data ---
//        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
//
//        // (Your existing findViewByIds for totals)
//        recyclerView = findViewById(R.id.recycler_cart_items);
//        tvTotalMrp = findViewById(R.id.tv_total_mrp);
//        tvTotalSavings = findViewById(R.id.tv_total_savings);
//        tvTotalBill = findViewById(R.id.tv_total_bill);
//        tvCheckoutPrice = findViewById(R.id.tv_checkout_price);
//        tvRetailerMargin = findViewById(R.id.tv_retailer_margin);
//        tvSchemeDiscount = findViewById(R.id.tv_scheme_discount);
//
//        setupRecyclerView();
//        setupObservers();
//    }
//
//    private void setupRecyclerView() {
//        adapter = new CartAdapter(this);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);
//    }
//
//    private void setupObservers() {
//        cartViewModel.getCartMap().observe(this, cartMap -> {
//            adapter.updateList(new ArrayList<>(cartMap.values()));
//        });
//
//        cartViewModel.getCartTotals().observe(this, totals -> {
//            // (Your existing code to set totals)
//            tvTotalMrp.setText(String.format(Locale.getDefault(), "Total MRP : ₹%,.0f", totals.totalMrp));
//            tvTotalSavings.setText(String.format(Locale.getDefault(), "Total Savings : ₹%,.0f", totals.totalSavings));
//            tvTotalBill.setText(String.format(Locale.getDefault(), "Estimated Bill Value : ₹%,.0f", totals.totalPrice));
//            tvCheckoutPrice.setText(String.format(Locale.getDefault(), "Total: ₹%,.0f", totals.totalPrice));
//            tvRetailerMargin.setText(String.format(Locale.getDefault(), "Retailer Margin : ₹%,.0f", totals.totalSavings));
//            tvSchemeDiscount.setText("Scheme Discount : ₹0");
//
//            // --- THIS IS THE FIX ---
//            // Update toolbar title dynamically
//            if (totals.uniqueItemCount == 0) {
//                toolbarTitle.setText("Cart");
//            } else {
//                toolbarTitle.setText(String.format(Locale.getDefault(), "Cart (%d items)", totals.uniqueItemCount));
//            }
//        });
//    }
//
//    // --- (Your existing CartInteractionListener methods) ---
//    @Override
//    public void onIncrementUnit(Product product) {
//        cartViewModel.incrementUnit(product);
//    }
//    @Override
//    public void onDecrementUnit(Product product) {
//        cartViewModel.decrementUnit(product);
//    }
//    @Override
//    public void onDeleteItem(Product product) {
//        cartViewModel.removeFromCart(product);
//    }
//}