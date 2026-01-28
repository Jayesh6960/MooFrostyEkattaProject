package com.example.moofrosty.ui.cart;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.example.moofrosty.R;
import com.example.moofrosty.core.utils.NetworkUtil;
import com.example.moofrosty.data.local.SessionManager;
import com.example.moofrosty.data.model.Product;

import java.util.ArrayList;
import java.util.Locale;


public class CartFragment{}
//
//    private CartViewModel cartViewModel;
//    private RecyclerView recyclerView;
//    private CartAdapter adapter;
//
//    private SessionManager sessionManager;
//    private ProgressDialog progressDialog;
//
//    private TextView tvTotalMrp, tvTotalSavings, tvTotalBill, tvCheckoutPrice, tvRetailerMargin, tvSchemeDiscount;
//    private View btnCheckout;
//    public CartFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_cart, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        sessionManager = new SessionManager(requireContext());
//        progressDialog = new ProgressDialog(requireContext());
//        progressDialog.setMessage("Placing Order...");
//        progressDialog.setCancelable(false);
//
//        cartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);
//        int userId = 1;
//        int shopId = 3;
//        cartViewModel.setSessionData(sessionManager.getToken(), userId, shopId);
//
//        // Find Views
//        recyclerView = view.findViewById(R.id.recycler_cart_items);
//        tvTotalMrp = view.findViewById(R.id.tv_total_mrp);
//        tvTotalSavings = view.findViewById(R.id.tv_total_savings);
//        tvTotalBill = view.findViewById(R.id.tv_total_bill);
//        tvCheckoutPrice = view.findViewById(R.id.tv_checkout_price);
//        tvRetailerMargin = view.findViewById(R.id.tv_retailer_margin);
//        tvSchemeDiscount = view.findViewById(R.id.tv_scheme_discount);
//        btnCheckout = view.findViewById(R.id.btn_checkout);
//
//        setupRecyclerView();
//        setupObservers();
//
//        btnCheckout.setOnClickListener(v -> handleCheckout());
//    }
//
//    private void handleCheckout() {
//        if (!NetworkUtil.isNetworkAvailable(getContext())) {
//            Toast.makeText(getContext(), "No Internet", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (adapter.getItemCount() == 0) {
//            Toast.makeText(getContext(), "Cart is Empty", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        cartViewModel.checkout();
//    }
//
//    private void setupObservers() {
//        cartViewModel.getCartMap().observe(getViewLifecycleOwner(), cartMap -> {
//            adapter.updateList(new ArrayList<>(cartMap.values()));
//        });
//
//        cartViewModel.getCartTotals().observe(getViewLifecycleOwner(), totals -> {
//            tvTotalMrp.setText(String.format(Locale.getDefault(), "Total MRP : ₹%,.0f", totals.totalMrp));
//            tvTotalSavings.setText(String.format(Locale.getDefault(), "Total Savings : ₹%,.0f", totals.totalSavings));
//            tvTotalBill.setText(String.format(Locale.getDefault(), "Estimated Bill Value : ₹%,.0f", totals.totalPrice));
//            tvCheckoutPrice.setText(String.format(Locale.getDefault(), "Total: ₹%,.0f", totals.totalPrice));
//            tvRetailerMargin.setText(String.format(Locale.getDefault(), "Retailer Margin : ₹%,.0f", totals.totalSavings));
//            tvSchemeDiscount.setText("Scheme Discount : ₹0");
//        });
//
//        cartViewModel.getCheckoutResult().observe(getViewLifecycleOwner(), resource -> {
//            if (resource != null) {
//                switch (resource.status) {
//                    case LOADING:
//                        progressDialog.show();
//                        btnCheckout.setEnabled(false);
//                        break;
//                    case SUCCESS:
//                        progressDialog.dismiss();
//                        Toast.makeText(getContext(), "Order Placed Successfully!", Toast.LENGTH_LONG).show();
//                        if (getActivity() != null) getActivity().onBackPressed();
//                        break;
//                    case ERROR:
//                        progressDialog.dismiss();
//                        btnCheckout.setEnabled(true);
//                        Toast.makeText(getContext(), resource.message, Toast.LENGTH_LONG).show();
//                        break;
//                }
//            }
//        });
//    }
//
//    private void setupRecyclerView() {
//        adapter = new CartAdapter(this);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(adapter);
//    }
//
//    @Override public void onIncrementUnit(Product p) { cartViewModel.incrementUnit(p); }
//    @Override public void onDecrementUnit(Product p) { cartViewModel.decrementUnit(p); }
//    @Override public void onDeleteItem(Product p) { cartViewModel.removeFromCart(p); }
//}
//
////        cartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);
////
////        // Find views
////        recyclerView = view.findViewById(R.id.recycler_cart_items);
////        tvTotalMrp = view.findViewById(R.id.tv_total_mrp);
////        tvTotalSavings = view.findViewById(R.id.tv_total_savings);
////        tvTotalBill = view.findViewById(R.id.tv_total_bill);
////        tvCheckoutPrice = view.findViewById(R.id.tv_checkout_price);
////        tvRetailerMargin = view.findViewById(R.id.tv_retailer_margin);
////        tvSchemeDiscount = view.findViewById(R.id.tv_scheme_discount);
////
////        setupRecyclerView();
////        setupObservers();
////    }
////    private void setupRecyclerView() {
////        adapter = new CartAdapter(this);
////        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
////        recyclerView.setAdapter(adapter);
////    }
////
////    private void setupObservers() {
////        cartViewModel.getCartMap().observe(getViewLifecycleOwner(), cartMap -> {
////            adapter.updateList(new ArrayList<>(cartMap.values()));
////        });
////
////        cartViewModel.getCartTotals().observe(getViewLifecycleOwner(), totals -> {
////            tvTotalMrp.setText(String.format(Locale.getDefault(), "Total MRP : ₹%,.0f", totals.totalMrp));
////            tvTotalSavings.setText(String.format(Locale.getDefault(), "Total Savings : ₹%,.0f", totals.totalSavings));
////            tvTotalBill.setText(String.format(Locale.getDefault(), "Estimated Bill Value : ₹%,.0f", totals.totalPrice));
////            tvCheckoutPrice.setText(String.format(Locale.getDefault(), "Total: ₹%,.0f", totals.totalPrice));
////
////            // You can set these to real values if you have them
////            tvRetailerMargin.setText(String.format(Locale.getDefault(), "Retailer Margin : ₹%,.0f", totals.totalSavings));
////            tvSchemeDiscount.setText("Scheme Discount : ₹0");
////        });
////    }
////
////    // --- Implement CartInteractionListener Methods ---
////    @Override
////    public void onIncrementUnit(Product product) {
////        cartViewModel.incrementUnit(product);
////    }
////    @Override
////    public void onDecrementUnit(Product product) {
////        cartViewModel.decrementUnit(product);
////    }
////    @Override
////    public void onDeleteItem(Product product) {
////        cartViewModel.removeFromCart(product);
////    }

//}