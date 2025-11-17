package com.example.moofrosty;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements CartAdapter.CartInteractionListener{

    private CartViewModel cartViewModel;
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private TextView tvTotalMrp, tvTotalSavings, tvTotalBill, tvCheckoutPrice, tvRetailerMargin, tvSchemeDiscount;
    private TextView toolbarTitle;
    MaterialCardView checkoutBar;
    RelativeLayout relativelayoutbottom;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_cart);
        AppBarLayout appBarLayout = findViewById(R.id.app_bar_layout);

        ViewCompat.setOnApplyWindowInsetsListener(appBarLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });
      //  checkoutBar = findViewById(R.id.checkout_bar);
        relativelayoutbottom = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(relativelayoutbottom, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), systemBars.bottom);
            return insets;
        });
        // check out code
        TextView btnCheckout = findViewById(R.id.btn_checkout);
        btnCheckout.setOnClickListener(v -> {
            cartViewModel.checkout(); // Call the checkout method
            finish(); // Close this activity
        });

        // --- FIND TOOLBAR TITLE ---
        toolbarTitle = findViewById(R.id.toolbar_title);
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());


        // --- This gets the ViewModel with the SHARED repository data ---
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        // (Your existing findViewByIds for totals)
        recyclerView = findViewById(R.id.recycler_cart_items);
        tvTotalMrp = findViewById(R.id.tv_total_mrp);
        tvTotalSavings = findViewById(R.id.tv_total_savings);
        tvTotalBill = findViewById(R.id.tv_total_bill);
        tvCheckoutPrice = findViewById(R.id.tv_checkout_price);
        tvRetailerMargin = findViewById(R.id.tv_retailer_margin);
        tvSchemeDiscount = findViewById(R.id.tv_scheme_discount);

        setupRecyclerView();
        setupObservers();
    }

    private void setupRecyclerView() {
        adapter = new CartAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupObservers() {
        cartViewModel.getCartMap().observe(this, cartMap -> {
            adapter.updateList(new ArrayList<>(cartMap.values()));
        });

        cartViewModel.getCartTotals().observe(this, totals -> {
            // (Your existing code to set totals)
            tvTotalMrp.setText(String.format(Locale.getDefault(), "Total MRP : ₹%,.0f", totals.totalMrp));
            tvTotalSavings.setText(String.format(Locale.getDefault(), "Total Savings : ₹%,.0f", totals.totalSavings));
            tvTotalBill.setText(String.format(Locale.getDefault(), "Estimated Bill Value : ₹%,.0f", totals.totalPrice));
            tvCheckoutPrice.setText(String.format(Locale.getDefault(), "Total: ₹%,.0f", totals.totalPrice));
            tvRetailerMargin.setText(String.format(Locale.getDefault(), "Retailer Margin : ₹%,.0f", totals.totalSavings));
            tvSchemeDiscount.setText("Scheme Discount : ₹0");

            // --- THIS IS THE FIX ---
            // Update toolbar title dynamically
            if (totals.uniqueItemCount == 0) {
                toolbarTitle.setText("Cart");
            } else {
                toolbarTitle.setText(String.format(Locale.getDefault(), "Cart (%d items)", totals.uniqueItemCount));
            }
        });
    }

    // --- (Your existing CartInteractionListener methods) ---
    @Override
    public void onIncrementUnit(Product product) {
        cartViewModel.incrementUnit(product);
    }
    @Override
    public void onDecrementUnit(Product product) {
        cartViewModel.decrementUnit(product);
    }
    @Override
    public void onDeleteItem(Product product) {
        cartViewModel.removeFromCart(product);
    }
}