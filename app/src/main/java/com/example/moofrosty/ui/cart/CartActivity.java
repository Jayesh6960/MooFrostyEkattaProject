package com.example.moofrosty.ui.cart;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
        btnCheckout.setOnClickListener(v -> handleCheckout());
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
            tvTotalMrp.setText(String.format(Locale.US, "Total MRP : ₹%.3f", totals.totalMrp));
            tvTotalSavings.setText(String.format(Locale.US, "Total Savings : ₹%.3f", totals.totalSavings));
            tvTotalBill.setText(String.format(Locale.US, "Estimated Bill Value : ₹%.3f", totals.totalPrice));

            // Bottom Checkout Bar Total
            tvCheckoutPrice.setText(String.format(Locale.US, "Total: ₹%.3f", totals.totalPrice));

            tvRetailerMargin.setText(String.format(Locale.US, "Retailer Margin : ₹%.3f", totals.totalSavings));
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
                        Toast.makeText(this, "Order Placed Successfully!", Toast.LENGTH_LONG).show();
                        finish();
                        break;
                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        btnCheckout.setEnabled(true);
                        Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }

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
    @Override public void onDeleteItem(Product p) { cartViewModel.removeFromCart(p); }
}


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