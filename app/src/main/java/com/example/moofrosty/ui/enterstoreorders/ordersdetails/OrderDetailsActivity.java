package com.example.moofrosty.ui.enterstoreorders.ordersdetails;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moofrosty.R;
import com.example.moofrosty.data.model.Order;
import com.example.moofrosty.data.repository.CartRepository;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Locale;

public class OrderDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderDetailsAdapter adapter;
    private Order order;

    private TextInputLayout searchBarLayout;
    private LinearLayout toolbarLayout; // The back/title layout
    private TextView headerTitle;
    private ImageView headerBackArrow;
    private ImageButton iconScan;
    private FrameLayout cartButtonLayout; // The FrameLayout around the cart
    private ImageButton iconPower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_order_details);
        // --- 1. Get the Order from Repository ---
        String orderId = getIntent().getStringExtra("ORDER_ID");
        CartRepository repository = CartRepository.getInstance();
//        order = repository.getOrderById(orderId);

        if (order == null) {
            finish();
            return;
        }

        AppBarLayout appBarLayout = findViewById(R.id.app_bar_layout);
        ViewCompat.setOnApplyWindowInsetsListener(appBarLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });

        View confirmReturnBar = findViewById(R.id.confirm_return_bar);
        ViewCompat.setOnApplyWindowInsetsListener(confirmReturnBar, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), systemBars.bottom);
            return insets;
        });

        // --- 3. Setup Toolbar ---
        searchBarLayout = findViewById(R.id.search_bar_layout);
        toolbarLayout = findViewById(R.id.toolbarlayout);
        headerTitle = findViewById(R.id.header_title);
        headerBackArrow = findViewById(R.id.header_back_arrow);
        iconScan = findViewById(R.id.icon_scan);
        cartButtonLayout = findViewById(R.id.cart_button_layout);
        iconPower = findViewById(R.id.icon_power);

        // --- Apply your requirements ---

        // Hide Search Bar
        searchBarLayout.setVisibility(View.GONE);

        // Show Back/Title layout
        toolbarLayout.setVisibility(View.VISIBLE);

        // Set Title
        if (order.getId() != null) {
           // headerTitle.setText(order.getId());
            headerTitle.setText("Order Details");
        } else {
            headerTitle.setText("Order Details");
        }

        // Set Back Arrow Click
        headerBackArrow.setOnClickListener(v -> finish());

        // Hide Scan Icon
        iconScan.setVisibility(View.GONE);

        // Show Cart and Power (they are visible by default, but this is explicit)
        cartButtonLayout.setVisibility(View.VISIBLE);
        iconPower.setVisibility(View.VISIBLE);

        // --- 4. Bind Summary Data ---
        TextView tvBillAmount = findViewById(R.id.tv_bill_amount);
        tvBillAmount.setText(String.format(Locale.getDefault(), " : ₹%,.2f", order.orderValue));

        // --- 5. Setup RecyclerView ---
        recyclerView = findViewById(R.id.recycler_order_items);
        adapter = new OrderDetailsAdapter(order.getItems());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}