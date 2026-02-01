package com.example.moofrosty.ui.enterstoreorders.takeorder;

import static com.example.moofrosty.core.network.Resource.Status.ERROR;
import static com.example.moofrosty.core.network.Resource.Status.LOADING;
import static com.example.moofrosty.core.network.Resource.Status.SUCCESS;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.moofrosty.R;
import com.example.moofrosty.data.model.OrderMissedViewModel;
import com.example.moofrosty.ui.cart.CartActivity;
import com.example.moofrosty.ui.cart.CartViewModel;
import com.example.moofrosty.ui.enterstoreorders.mission.MissionFragment;
import com.example.moofrosty.ui.enterstoreorders.ordersdetails.OrdersFragment;
import com.example.moofrosty.ui.enterstoreorders.shopfront.ShopFrontFragment;
import com.example.moofrosty.ui.filter.FilterViewModel;
import com.example.moofrosty.ui.menu.MenuFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class TakeOrderActivity extends AppCompatActivity {

    private AppBarLayout appBarLayout;
    private LinearLayout infoBar, top_barContainer;
    private BottomNavigationView bottomNav;
    private RelativeLayout relativescreen;
    private ImageButton iconPower, iconCart;
    private FrameLayout floatingCartBar;
    private TextView cartBadge, tvcartitemcount, tvcarttotalprice;
    private TextInputEditText searchbar;

    private FragmentManager fragmentManager;

    private FilterViewModel filterViewModel;
    private CartViewModel cartViewModel;

    // ✅ CORRECT ViewModel
    private OrderMissedViewModel orderMissedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_take_order);

        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        windowInsetsController.setAppearanceLightStatusBars(false);

        bottomNav = findViewById(R.id.bottom_navigation);
        appBarLayout = findViewById(R.id.app_bar_layout);
        infoBar = findViewById(R.id.info_bar);
        relativescreen = findViewById(R.id.relativelayoutmenu);
        top_barContainer = findViewById(R.id.top_bar_container);

        iconCart = findViewById(R.id.icon_cart);
        cartBadge = findViewById(R.id.cart_badge);
        floatingCartBar = findViewById(R.id.floating_cart_bar);
        iconPower = findViewById(R.id.icon_power);
        tvcartitemcount = findViewById(R.id.tv_cart_item_count);
        tvcarttotalprice = findViewById(R.id.tv_cart_total_price);
        searchbar = findViewById(R.id.search_bar);

        fragmentManager = getSupportFragmentManager();

        filterViewModel = new ViewModelProvider(this).get(FilterViewModel.class);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        // ✅ Initialize OrderMissedViewModel
        orderMissedViewModel =
                new ViewModelProvider(this).get(OrderMissedViewModel.class);

        iconPower.setOnClickListener(v -> showOrderMissedBottomSheet());

        iconCart.setOnClickListener(v -> openCartFragment());
        floatingCartBar.setOnClickListener(v -> openCartFragment());

        cartViewModel.getCartTotals().observe(this, totals -> {
            if (totals.uniqueItemCount == 0) {
                cartBadge.setVisibility(View.GONE);
                floatingCartBar.setVisibility(View.GONE);
            } else {
                cartBadge.setVisibility(View.VISIBLE);
                cartBadge.setText(String.valueOf(totals.uniqueItemCount));
                floatingCartBar.setVisibility(View.VISIBLE);
                tvcartitemcount.setText(totals.totalUnitCount + " items");
                tvcarttotalprice.setText("₹" + totals.totalPrice);
            }
        });

        bottomNav.setOnItemSelectedListener(navListener);

        if (savedInstanceState == null) {
            loadFragment(new ShopFrontFragment());
            bottomNav.setSelectedItemId(R.id.nav_shop_front);
        }
    }

    // ---------------- ORDER MISSED BOTTOM SHEET ----------------

    private void showOrderMissedBottomSheet() {

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = getLayoutInflater()
                .inflate(R.layout.bottom_sheet_order_missed_material, null);

        ImageView ivClose = view.findViewById(R.id.ivClose);
        AutoCompleteTextView spReason = view.findViewById(R.id.spReason);
        TextInputLayout tilOtherReason = view.findViewById(R.id.tvStore);
        TextInputEditText etOtherReason = view.findViewById(R.id.tvBeat);
        MaterialButton btnConfirm = view.findViewById(R.id.btnConfirm);

        tilOtherReason.setVisibility(View.GONE);

        String[] reasons = {
                "Store Closed",
                "Owner Not Available",
                "Out of Stock",
                "Other"
        };

        spReason.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                reasons
        ));

        spReason.setOnItemClickListener((parent, v, position, id) -> {
            if ("Other".equalsIgnoreCase(reasons[position])) {
                tilOtherReason.setVisibility(View.VISIBLE);
            } else {
                tilOtherReason.setVisibility(View.GONE);
                etOtherReason.setText("");
            }
        });

        ivClose.setOnClickListener(v -> dialog.dismiss());

        btnConfirm.setOnClickListener(v -> {

            String selectedReason = spReason.getText().toString().trim();
            String finalReason = selectedReason;

            if (selectedReason.isEmpty()) {
                Toast.makeText(this, "Please select a reason", Toast.LENGTH_SHORT).show();
                return;
            }

            if ("Other".equalsIgnoreCase(selectedReason)) {
                String otherReason = etOtherReason.getText().toString().trim();
                if (otherReason.isEmpty()) {
                    Toast.makeText(this, "Please enter the reason", Toast.LENGTH_SHORT).show();
                    return;
                }
                finalReason = otherReason;
            }

            // ✅ ViewModel call
            orderMissedViewModel.markOrderMissed("ORDER_ID", finalReason);
        });

        dialog.setContentView(view);
        dialog.show();

        // ✅ Observe ViewModel
        orderMissedViewModel.getOrderMissedResult()
                .observe(this, resource -> {

                    if (resource == null) return;

                    switch (resource.status) {

                        case LOADING:
                            btnConfirm.setEnabled(false);
                            break;

                        case SUCCESS:
                            btnConfirm.setEnabled(true);
                            Toast.makeText(
                                    this,
                                    resource.data.getMessage(),
                                    Toast.LENGTH_SHORT
                            ).show();
                            dialog.dismiss();
                            break;

                        case ERROR:
                            btnConfirm.setEnabled(true);
                            Toast.makeText(
                                    this,
                                    resource.message,
                                    Toast.LENGTH_SHORT
                            ).show();
                            break;
                    }
                });
    }

    // ---------------- NAV / HELPERS ----------------

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }

    private void openCartFragment() {
        startActivity(new Intent(this, CartActivity.class));
    }

    private final BottomNavigationView.OnItemSelectedListener navListener = item -> {
        Fragment fragment = null;

        if (item.getItemId() == R.id.nav_shop_front) fragment = new ShopFrontFragment();
        else if (item.getItemId() == R.id.nav_take_order) fragment = new TakeOrderFragment();
        else if (item.getItemId() == R.id.nav_orders) fragment = new OrdersFragment();
        else if (item.getItemId() == R.id.nav_mission) fragment = new MissionFragment();
        else if (item.getItemId() == R.id.nav_menu) fragment = new MenuFragment();

        if (fragment != null) loadFragment(fragment);
        return true;
    };
}
