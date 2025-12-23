package com.example.moofrosty.ui.cart;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.moofrosty.R;
import com.example.moofrosty.data.model.Product;

import java.util.ArrayList;
import java.util.Locale;


public class CartFragment extends Fragment implements CartAdapter.CartInteractionListener{

    private CartViewModel cartViewModel;
    private RecyclerView recyclerView;
    private CartAdapter adapter;

    private TextView tvTotalMrp, tvTotalSavings, tvTotalBill, tvCheckoutPrice, tvRetailerMargin, tvSchemeDiscount;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);

        // Find views
        recyclerView = view.findViewById(R.id.recycler_cart_items);
        tvTotalMrp = view.findViewById(R.id.tv_total_mrp);
        tvTotalSavings = view.findViewById(R.id.tv_total_savings);
        tvTotalBill = view.findViewById(R.id.tv_total_bill);
        tvCheckoutPrice = view.findViewById(R.id.tv_checkout_price);
        tvRetailerMargin = view.findViewById(R.id.tv_retailer_margin);
        tvSchemeDiscount = view.findViewById(R.id.tv_scheme_discount);

        setupRecyclerView();
        setupObservers();
    }
    private void setupRecyclerView() {
        adapter = new CartAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void setupObservers() {
        cartViewModel.getCartMap().observe(getViewLifecycleOwner(), cartMap -> {
            adapter.updateList(new ArrayList<>(cartMap.values()));
        });

        cartViewModel.getCartTotals().observe(getViewLifecycleOwner(), totals -> {
            tvTotalMrp.setText(String.format(Locale.getDefault(), "Total MRP : ₹%,.0f", totals.totalMrp));
            tvTotalSavings.setText(String.format(Locale.getDefault(), "Total Savings : ₹%,.0f", totals.totalSavings));
            tvTotalBill.setText(String.format(Locale.getDefault(), "Estimated Bill Value : ₹%,.0f", totals.totalPrice));
            tvCheckoutPrice.setText(String.format(Locale.getDefault(), "Total: ₹%,.0f", totals.totalPrice));

            // You can set these to real values if you have them
            tvRetailerMargin.setText(String.format(Locale.getDefault(), "Retailer Margin : ₹%,.0f", totals.totalSavings));
            tvSchemeDiscount.setText("Scheme Discount : ₹0");
        });
    }

    // --- Implement CartInteractionListener Methods ---
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