package com.example.moofrosty.ui.enterstoreorders.takeorder;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
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

import com.example.moofrosty.ui.enterstoreorders.ActionPointActivitys;
import com.example.moofrosty.ui.enterstoreorders.mission.MissionFragment;
import com.example.moofrosty.R;
import com.example.moofrosty.ui.enterstoreorders.ordersdetails.OrdersFragment;
import com.example.moofrosty.ui.enterstoreorders.shopfront.ShopFrontFragment;
import com.example.moofrosty.ui.cart.CartActivity;
import com.example.moofrosty.ui.cart.CartViewModel;
import com.example.moofrosty.ui.filter.FilterViewModel;
import com.example.moofrosty.ui.login.LoginActivity;
import com.example.moofrosty.ui.menu.MenuFragment;
import com.example.moofrosty.ui.newstorecreation.NewStoreActivity;
import com.example.moofrosty.ui.store.StoreProfileActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

public class TakeOrderActivity extends AppCompatActivity {

        private AppBarLayout appBarLayout;
        private LinearLayout infoBar,top_barContainer;
        private BottomNavigationView bottomNav;
        private RelativeLayout relativescreen;
        LinearLayout top_bar_container;
        ImageButton iconPower;

        private FilterViewModel filterViewModel;

        // --- NEWLY ADDED ---
        private CartViewModel cartViewModel;
        private ImageButton iconCart;
        private TextView cartBadge, tvcartitemcount, tvcarttotalprice;
        private FrameLayout floatingCartBar; // Find it here

        FragmentManager fragmentManager;
        TextInputEditText searchbar;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_take_order);

            WindowInsetsControllerCompat windowInsetsController =
                    WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
            // false = "Not Light" = White Icons
            // true  = "Light"     = Black Icons
            windowInsetsController.setAppearanceLightStatusBars(false);

            bottomNav = findViewById(R.id.bottom_navigation);
            appBarLayout = findViewById(R.id.app_bar_layout);
            infoBar = findViewById(R.id.info_bar);
            relativescreen = findViewById(R.id.relativelayoutmenu);
            top_barContainer = findViewById(R.id.top_bar_container);

            // --- NEW Views ---
            iconCart = findViewById(R.id.icon_cart);
            cartBadge = findViewById(R.id.cart_badge);
            floatingCartBar = findViewById(R.id.floating_cart_bar);
            iconPower = findViewById(R.id.icon_power);
            tvcartitemcount = findViewById(R.id.tv_cart_item_count);
            tvcarttotalprice = findViewById(R.id.tv_cart_total_price);
            fragmentManager = getSupportFragmentManager();


            ViewCompat.setOnApplyWindowInsetsListener(top_barContainer, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom());
                return insets;
            });
            ViewCompat.setOnApplyWindowInsetsListener(bottomNav, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), systemBars.bottom);
                return insets;
            });

            filterViewModel = new ViewModelProvider(this).get(FilterViewModel.class);

            cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

            iconCart.setOnClickListener(v -> openCartFragment());
            floatingCartBar.setOnClickListener(v -> openCartFragment()); // Add listener
            cartViewModel.getCartTotals().observe(this, totals -> {
                if (totals.uniqueItemCount == 0) {
                    cartBadge.setVisibility(View.GONE);
                    floatingCartBar.setVisibility(View.GONE);
                } else {
                    cartBadge.setVisibility(View.VISIBLE);
                    cartBadge.setText(String.valueOf(totals.uniqueItemCount));
                    floatingCartBar.setVisibility(View.VISIBLE);
                    tvcartitemcount.setText(totals.totalUnitCount+" items");
                    tvcarttotalprice.setText("₹"+totals.totalPrice);

                }
            });
            bottomNav.setOnItemSelectedListener(navListener);
            // Load the default fragment
            if (savedInstanceState == null) {
                loadFragment(new ShopFrontFragment());
                bottomNav.setSelectedItemId(R.id.nav_shop_front);
            }

            iconPower.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TakeOrderActivity.this, StoreProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        searchbar = findViewById(R.id.search_bar);
        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Get the current fragment
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                // Check if it is the TakeOrderFragment
                if (currentFragment instanceof TakeOrderFragment) {
                    // Call the search method on the fragment
                    ((TakeOrderFragment) currentFragment).onSearchQuery(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });

        }
            private final BottomNavigationView.OnItemSelectedListener navListener =
                item -> {
                    Fragment selectedFragment = null;
                    boolean isMenuFragment = false;

                    int itemId = item.getItemId();
                    if (itemId == R.id.nav_take_order) {
                        selectedFragment = new TakeOrderFragment();
                    } else if (itemId == R.id.nav_shop_front) {
                         selectedFragment = new ShopFrontFragment();
                    } else if (itemId == R.id.nav_orders) {
                        selectedFragment = new OrdersFragment();
                    } else if (itemId == R.id.nav_mission) {
                         selectedFragment = new MissionFragment();
                    } else if (itemId == R.id.nav_menu) {
                        selectedFragment = new MenuFragment();
                        isMenuFragment = true;
                    }

                    if (selectedFragment == null) {
                        selectedFragment = new TakeOrderFragment(); // Default
                    }

                    if (isMenuFragment) {
                        appBarLayout.setVisibility(View.GONE);
                        infoBar.setVisibility(View.GONE);
                        relativescreen.setVisibility(View.VISIBLE);
                    } else {
                        appBarLayout.setVisibility(View.VISIBLE);
                        infoBar.setVisibility(View.VISIBLE);
                        relativescreen.setVisibility(View.GONE);
                    }

            loadFragment(selectedFragment);
            return true;
        };

//        @Override
//        public void onBackPressed() {
//            if (fragmentManager.getBackStackEntryCount() > 0) {
//                // Go back one fragment
//                fragmentManager.popBackStack();
//
//                // OR: use super.onBackPressed(); which is cleaner
//                // super.onBackPressed();
//
//            } else if (bottomNav.getSelectedItemId() == R.id.nav_shop_front) {
//                // You are on the final root screen (Shop Front)
//                // This will finish the activity (close the app/go back to the previous app).
//                Intent intent = new Intent(TakeOrderActivity.this,ActionPointActivitys.class);
//                startActivity(intent);
//            }
//            else {
//                // This covers the case where the fragment stack is empty
//                // AND you are not on the Shop Front (which should not happen with a bottom nav, but is a good fallback)
//                super.onBackPressed();
//            }
//
//        }

    @Override
    public void onBackPressed() {
        // 1. Check if the fragment back stack is NOT empty
        if (fragmentManager.getBackStackEntryCount() > 0) {
            // 2. Pop the fragment off the stack
            fragmentManager.popBackStack();
            // 3. IMPORTANT: Update the BottomNavigationView immediately
            //    (You must implement the method below)
            updateBottomNavigationSelection();

        } else {
            // 4. If the stack is empty, perform the default back action (finish the activity)
            super.onBackPressed();
        }
    }

    private void updateBottomNavigationSelection() {
        // 1. Get the currently visible fragment
        //    (You must know the ID of your fragment container)
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if (currentFragment != null) {

            // 2. Check the type of the visible fragment and select the matching Bottom Nav item
            if (currentFragment instanceof ShopFrontFragment) {
                Intent intent = new Intent(TakeOrderActivity.this, ActionPointActivitys.class);
                startActivity(intent);
            } else if (currentFragment instanceof TakeOrderFragment) {
                bottomNav.setSelectedItemId(R.id.nav_shop_front);
            }
            else if (currentFragment instanceof OrdersFragment) {
                bottomNav.setSelectedItemId(R.id.nav_take_order);
            }
            else if (currentFragment instanceof MissionFragment) {
                bottomNav.setSelectedItemId(R.id.nav_orders);
            }
            else if (currentFragment instanceof MenuFragment) {
                bottomNav.setSelectedItemId(R.id.nav_shop_front);
            }
        }
    }

        private void showExitDialog() {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Exit App")
                    .setMessage("Do you want to exit?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        finish();
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .create()
                    .show();
        }

        private void loadFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack("TakeOrderFragment");
        fragmentTransaction.commit();
    }

        private void openCartFragment() {
//            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//            if (!(currentFragment instanceof CartFragment)) {
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_container, new CartFragment())
//                        .addToBackStack(null) // So user can press back
//                        .commit();
//            }

            Intent intent = new Intent(TakeOrderActivity.this, CartActivity.class);
            startActivity(intent);
        }
}