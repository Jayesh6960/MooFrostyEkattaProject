package com.example.moofrosty.ui.enterstoreorders.ordersdetails;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moofrosty.R;
import com.example.moofrosty.core.utils.NetworkUtil;
import com.example.moofrosty.data.local.SessionManager;
import com.example.moofrosty.data.model.Order;
import com.example.moofrosty.data.model.OrderHistoryResponse;
import com.example.moofrosty.ui.cart.CartViewModel;
import com.example.moofrosty.ui.enterstoreorders.takeorder.TakeOrderActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;


public class OrdersFragment extends Fragment implements OrderAdapter.OnOrderClickListener{

    private CartViewModel cartViewModel;
    private RecyclerView recyclerOrders;
    private OrderAdapter adapter;
    private TextView tvOrderCount;
    private TabLayout tabLayout;
    private SessionManager sessionManager;
    ProgressBar progressBar;

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

        sessionManager = new SessionManager(requireContext());
        cartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);

        // Pass token to ViewModel so it can make the call
        // Pass token and SHOP ID to ViewModel
        int shopId = sessionManager.getShopId(); // [HIGHLIGHT] Getting shop id dynamically
        cartViewModel.setSessionData(sessionManager.getToken(), sessionManager.getUserId(), shopId);
//        cartViewModel.setSessionData(sessionManager.getToken(), 0, 0);

        // 2. Init Views
        tvOrderCount = view.findViewById(R.id.tv_order_count);
        tabLayout = view.findViewById(R.id.tab_layout_orders);
        recyclerOrders = view.findViewById(R.id.recycler_orders);
        progressBar = view.findViewById(R.id.progressBar);

        setupTabs();
        setupRecyclerView();

        // 3. Fetch Data
        if (NetworkUtil.isNetworkAvailable(getContext())) {
            cartViewModel.fetchOrders(); // Trigger API
        } else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        // 4. Observe Data
        setupObservers();

    }

    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("All"));
    }

    private void setupRecyclerView() {
        adapter = new OrderAdapter(this);
        recyclerOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerOrders.setAdapter(adapter);
    }


    private void setupObservers() {
        cartViewModel.getOrderHistory().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case LOADING:
                        progressBar.setVisibility(View.VISIBLE);
                        break;

                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        if (resource.data != null && resource.data.data != null) {
                            adapter.updateList(resource.data.data);
                            // [HIGHLIGHT] Setting count properly dynamically
                            tvOrderCount.setText(" : "+resource.data.data.size() + " orders");
                            Log.d("datacount", "datacount"+resource.data.data.size());
                            int orderCount = resource.data.data.size();
                            Intent intent = new Intent(getActivity(), TakeOrderActivity.class);
                            intent.putExtra("order_count", orderCount);
                            Log.d("order_count", "order_count"+orderCount);
//                            startActivity(intent);
                        }
                        break;

                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Something wrong", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

//    private void setupObservers() {
//
//        cartViewModel.getOrderHistory().observe(getViewLifecycleOwner(), resource -> {
//            if (resource != null) {
//                switch (resource.status) {
//                    case LOADING:
//                        // Show progress if needed
//                        progressBar.setVisibility(View.VISIBLE);
//                        Log.d("OrdersFragment", "Loading...");
//                        break;
//
//                    case SUCCESS:
//                        progressBar.setVisibility(View.GONE);
//                        if (resource.data != null && resource.data.data != null) {
//                            Log.d("OrdersFragment", "Success! Items: " + resource.data.data.size());
//                            adapter.updateList(resource.data.data);
//                            tvOrderCount.setText(String.format(Locale.getDefault(), ": (%d)", resource.data.data.size()));
//                        } else {
//                            Log.e("OrdersFragment", "Success but data is NULL");
//                        }
//                        break;
//
//                    case ERROR:
//                        progressBar.setVisibility(View.GONE);
//                        Log.e("OrdersFragment", "Error: " + resource.message);
//                        Toast.makeText(getContext(), "Something wrong", Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//        });
//    }

    @Override
    public void onOrderClick(OrderHistoryResponse.OrderData order) {
        Intent intent = new Intent(getActivity(), OrderDetailsActivity.class);
        // Pass the entire object using Serializable
        intent.putExtra("ORDER_DATA", order);
        startActivity(intent);
    }
}


     //   dummy code below api not add that

//        tvOrderCount = view.findViewById(R.id.tv_order_count);
//        tabLayout = view.findViewById(R.id.tab_layout_orders);
//
//        setupTabs();
//        setupRecyclerView(view);
//        //setupObservers();
//    }
//
//    private void setupTabs() {
//        tabLayout.addTab(tabLayout.newTab().setText("All"));
//        tabLayout.addTab(tabLayout.newTab().setText("Shikhar Orders"));
//        tabLayout.addTab(tabLayout.newTab().setText("Salesperson Orders"));
//        // Add other tabs...
//    }
//
//    private void setupRecyclerView(View view) {
//        recyclerOrders = view.findViewById(R.id.recycler_orders);
//        adapter = new OrderAdapter(this);
//        recyclerOrders.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerOrders.setAdapter(adapter);
//    }
//
////    private void setupObservers() {
////        cartViewModel.getOrders().observe(getViewLifecycleOwner(), orders -> {
////            if (orders != null) {
////                adapter.updateList(orders);
////                // Update the count header
////                tvOrderCount.setText(String.format(Locale.getDefault(), "Last 7 Days (%d orders)", orders.size()));
////            }
////        });
////    }
//
//    @Override
//    public void onOrderClick(Order order) {
//        Intent intent = new Intent(getActivity(), OrderDetailsActivity.class);
//        // Pass only the ID
//        intent.putExtra("ORDER_ID", order.getId());
//        startActivity(intent);
//    }
//}