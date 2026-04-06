package com.example.moofrosty.ui.enterstoreorders.ordersdetails;

import android.os.Bundle;
import android.util.Log;
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
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moofrosty.R;
import com.example.moofrosty.data.model.Order;
import com.example.moofrosty.data.model.OrderHistoryResponse;
import com.example.moofrosty.data.repository.CartRepository;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Locale;

public class OrderDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderDetailsAdapter adapter;
    private OrderHistoryResponse.OrderData currentOrder;
 //   private Order order;

    private TextInputLayout searchBarLayout;
    private LinearLayout toolbarLayout, invoicenolayout; // The back/title layout
    private TextView headerTitle, tvbilledno;

    private ImageView headerBackArrow;
    private ImageButton iconScan;
    private FrameLayout cartButtonLayout; // The FrameLayout around the cart
    private ImageButton iconPower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_order_details);
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        windowInsetsController.setAppearanceLightStatusBars(true);
        // --- 1. Get the Order from Repository ---
//        String orderId = getIntent().getStringExtra("ORDER_ID");
//        CartRepository repository = CartRepository.getInstance();
////        order = repository.getOrderById(orderId);
//
//        if (order == null) {
//            finish();
//            return;
//        }

        // --- 1. Get Data from Intent ---
        if (getIntent().getExtras() != null) {
            currentOrder = (OrderHistoryResponse.OrderData) getIntent().getSerializableExtra("ORDER_DATA");
        }

        if (currentOrder == null) {
            finish();
            return;
        }

        AppBarLayout appBarLayout = findViewById(R.id.app_bar_layout);
        ViewCompat.setOnApplyWindowInsetsListener(appBarLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });

        View confirmReturnBar = findViewById(R.id.recycler_order_items);
        ViewCompat.setOnApplyWindowInsetsListener(confirmReturnBar, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), systemBars.bottom);
            return insets;
        });

        // --- 3. Setup Header / Toolbar ---
        TextView headerTitle = findViewById(R.id.header_title);
        headerTitle.setText("Order Details");
        findViewById(R.id.header_back_arrow).setOnClickListener(v -> finish());

        invoicenolayout = findViewById(R.id.invoicenolayout);
        tvbilledno = findViewById(R.id.tv_billed_no);

        // Hide/Show specific icons as per requirement
        findViewById(R.id.search_bar_layout).setVisibility(View.GONE);
        findViewById(R.id.toolbarlayout).setVisibility(View.VISIBLE);
        findViewById(R.id.icon_scan).setVisibility(View.GONE);
        cartButtonLayout = findViewById(R.id.cart_button_layout);
        iconPower = findViewById(R.id.icon_power);
        cartButtonLayout.setVisibility(View.GONE);
        iconPower.setVisibility(View.GONE);

        // --- 4. Bind UI Data ---
        setupOrderInfo();

        // --- 5. Setup RecyclerView ---
        recyclerView = findViewById(R.id.recycler_order_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Use empty list if items is null
        adapter = new OrderDetailsAdapter(
                currentOrder.items != null ? currentOrder.items : new ArrayList<>(),
                currentOrder.status
        );
        recyclerView.setAdapter(adapter);
    }

    private void setupOrderInfo() {
        TextView tvBillAmount = findViewById(R.id.tv_bill_amount);
        TextView tvSalespersonTitle = findViewById(R.id.tv_salesperson_title);
        TextView tvStatusText = findViewById(R.id.tv_status_text);

        // [HIGHLIGHT] Fetch total amount safely from orderSummary
        if (currentOrder.orderSummary != null) {
            tvBillAmount.setText(String.format(Locale.US, ": ₹%.2f", currentOrder.orderSummary.orderValue));
            Log.d("Totalvalues", "Totolvalue:" + currentOrder.orderSummary.orderValue);
        }

        // [HIGHLIGHT] JSON replaced user with shop logic
        if (currentOrder.useDetails != null) {
            String name = currentOrder.useDetails.getFullName();
            String title = "Salesperson " + name + " Orders";
            tvSalespersonTitle.setText(title);
        } else {
            tvSalespersonTitle.setText("Salesperson Orders");
        }

        // Status Logic
//        if (currentOrder.status == 1) {
//            tvStatusText.setText("Billed");
//            tvStatusText.setTextColor(getColor(R.color.green));
//            invoicenolayout.setVisibility(View.VISIBLE);
//            tvbilledno.setText(": " + currentOrder.getinvoiceId());
//        } else if (currentOrder.status == 0) {
//            {
//                tvStatusText.setText("Order Placed");
//                tvStatusText.setTextColor(getColor(R.color.infoBarBlue));
//            }
//        }
//        if (currentOrder.status == 1) {
//
//            tvStatusText.setText("Billed");
//            tvStatusText.setTextColor(getColor(R.color.green));
//
//            invoicenolayout.setVisibility(View.VISIBLE);
//            tvbilledno.setText(": " + currentOrder.getinvoiceId());
//
//        } else if (currentOrder.status == 0) {
//
//            tvStatusText.setText("Order Placed");
//            tvStatusText.setTextColor(getColor(R.color.infoBarBlue));
//
//            invoicenolayout.setVisibility(View.VISIBLE);
//            tvbilledno.setText(": " + currentOrder.getinvoiceId());
//        }
        if (currentOrder.status == 1) {

            tvStatusText.setText("Billed");
            tvStatusText.setTextColor(getColor(R.color.green));

            invoicenolayout.setVisibility(View.VISIBLE);
            tvbilledno.setText(": " + currentOrder.getinvoiceId());

        } else if (currentOrder.status == 0) {

            tvStatusText.setText("Order Placed");
            tvStatusText.setTextColor(getColor(R.color.infoBarBlue));

            invoicenolayout.setVisibility(View.VISIBLE);


            // SHOW ORDER ID HERE
            tvbilledno.setText(": " + currentOrder.orderId);
        }

        Log.d("OrderStatusCheck", "Status: " + currentOrder.status);
        Log.d("OrderStatusCheck", "InvoiceNo: " + currentOrder.invoiceNo);
        Log.d("OrderStatusCheck", "OrderId: " + currentOrder.orderId);
//    private void setupOrderInfo() {
//        TextView tvBillAmount = findViewById(R.id.tv_bill_amount);
//        TextView tvSalespersonTitle = findViewById(R.id.tv_salesperson_title); // ID for "Salesperson Orders"
//        TextView tvStatusText = findViewById(R.id.tv_status_text); // ID for "Billed" text
//
//        // 1. Bill Amount (3 Decimals)
//        tvBillAmount.setText(String.format(Locale.US, ": ₹%.2f", currentOrder.totalAmount));
//
//        // 2. Salesperson Name
//        if (currentOrder.user != null) {
//            // Example: "Rahul Sharma Orders"
//            String title = "Salesperson "+currentOrder.user.getFullName() + " Orders";
//            // Assuming you have a TextView for the title in your layout
//            tvSalespersonTitle.setText(title);
//        }
//
//        // 3. Status Logic (0=Placed, 1=Billed)
//        if (currentOrder.status == 1) {
//            tvStatusText.setText("Billed");
//            tvStatusText.setTextColor(getColor(R.color.textGreen)); // Define green in colors.xml
//            invoicenolayout.setVisibility(View.VISIBLE);
//            tvbilledno.setText(": "+currentOrder.getinvoiceId());
//            tvStatusText.setTextColor(getColor(R.color.black));
//            // Set icon tint if needed programmatically or via XML
//        } else {
//            tvStatusText.setText("Order Placed");
//            tvStatusText.setTextColor(getColor(R.color.infoBarBlue)); // Define blue/orange
//        }
//    }
    }
}



//        // --- 3. Setup Toolbar ---
//        searchBarLayout = findViewById(R.id.search_bar_layout);
//        toolbarLayout = findViewById(R.id.toolbarlayout);
//        headerTitle = findViewById(R.id.header_title);
//        headerBackArrow = findViewById(R.id.header_back_arrow);
//        iconScan = findViewById(R.id.icon_scan);
//        cartButtonLayout = findViewById(R.id.cart_button_layout);
//        iconPower = findViewById(R.id.icon_power);
//
//        // --- Apply your requirements ---
//
//        // Hide Search Bar
//        searchBarLayout.setVisibility(View.GONE);
//
//        // Show Back/Title layout
//        toolbarLayout.setVisibility(View.VISIBLE);
//
//        // Set Title
//        if (order.getId() != null) {
//           // headerTitle.setText(order.getId());
//            headerTitle.setText("Order Details");
//        } else {
//            headerTitle.setText("Order Details");
//        }
//
//        // Set Back Arrow Click
//        headerBackArrow.setOnClickListener(v -> finish());
//
//        // Hide Scan Icon
//        iconScan.setVisibility(View.GONE);
//
//        // Show Cart and Power (they are visible by default, but this is explicit)
//        cartButtonLayout.setVisibility(View.VISIBLE);
//        iconPower.setVisibility(View.VISIBLE);
//
//        // --- 4. Bind Summary Data ---
//        TextView tvBillAmount = findViewById(R.id.tv_bill_amount);
//        tvBillAmount.setText(String.format(Locale.getDefault(), " : ₹%,.2f", order.orderValue));
//
//        // --- 5. Setup RecyclerView ---
//        recyclerView = findViewById(R.id.recycler_order_items);
//        adapter = new OrderDetailsAdapter(order.getItems());
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);
//    }
//}