package com.example.moofrosty;

import android.content.Intent;
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

import com.google.android.material.tabs.TabLayout;

import java.util.Locale;


public class OrdersFragment extends Fragment implements OrderAdapter.OnOrderClickListener{

    private CartViewModel cartViewModel;
    private RecyclerView recyclerOrders;
    private OrderAdapter adapter;
    private TextView tvOrderCount;
    private TabLayout tabLayout;

    public OrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_orders, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);

        tvOrderCount = view.findViewById(R.id.tv_order_count);
        tabLayout = view.findViewById(R.id.tab_layout_orders);

        setupTabs();
        setupRecyclerView(view);
        setupObservers();
    }

    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("All"));
        tabLayout.addTab(tabLayout.newTab().setText("Shikhar Orders"));
        tabLayout.addTab(tabLayout.newTab().setText("Salesperson Orders"));
        // Add other tabs...
    }

    private void setupRecyclerView(View view) {
        recyclerOrders = view.findViewById(R.id.recycler_orders);
        adapter = new OrderAdapter(this);
        recyclerOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerOrders.setAdapter(adapter);
    }

    private void setupObservers() {
        cartViewModel.getOrders().observe(getViewLifecycleOwner(), orders -> {
            if (orders != null) {
                adapter.updateList(orders);
                // Update the count header
                tvOrderCount.setText(String.format(Locale.getDefault(), "Last 7 Days (%d orders)", orders.size()));
            }
        });
    }

    @Override
    public void onOrderClick(Order order) {
        Intent intent = new Intent(getActivity(), OrderDetailsActivity.class);
        // Pass only the ID
        intent.putExtra("ORDER_ID", order.getId());
        startActivity(intent);
    }
}