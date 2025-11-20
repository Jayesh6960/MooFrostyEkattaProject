package com.example.moofrosty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

    public class TakeOrderActivity extends AppCompatActivity {

        private AppBarLayout appBarLayout;
        private LinearLayout infoBar,top_barContainer;
        private BottomNavigationView bottomNav;
        private RelativeLayout relativescreen;
        LinearLayout top_bar_container;

        private FilterViewModel filterViewModel;

        // --- NEWLY ADDED ---
        private CartViewModel cartViewModel;
        private ImageButton iconCart,icScan;
        private TextView cartBadge;
        private FrameLayout floatingCartBar; // Find it here

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
            setContentView(R.layout.activity_take_order);

            bottomNav = findViewById(R.id.bottom_navigation);
            appBarLayout = findViewById(R.id.app_bar_layout);
            infoBar = findViewById(R.id.info_bar);
            relativescreen = findViewById(R.id.relativelayoutmenu);
            top_barContainer = findViewById(R.id.top_bar_container);
            icScan=findViewById(R.id.icon_scan);

            // --- NEW Views ---
            iconCart = findViewById(R.id.icon_cart);
            cartBadge = findViewById(R.id.cart_badge);
            floatingCartBar = findViewById(R.id.floating_cart_bar);

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
            //new Code updated the in the Takeorder Activity
            icScan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    QRcode fragment = new QRcode();

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            });

            filterViewModel = new ViewModelProvider(this).get(FilterViewModel.class);

            cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

            iconCart.setOnClickListener(v -> openCartFragment());
            floatingCartBar.setOnClickListener(v -> openCartFragment()); // Add listener
            cartViewModel.getCartTotals().observe(this, totals -> {
                if (totals.uniqueItemCount == 0) {
                    cartBadge.setVisibility(View.GONE);
                } else {
                    cartBadge.setVisibility(View.VISIBLE);
                    cartBadge.setText(String.valueOf(totals.uniqueItemCount));
                }
            });
            bottomNav.setOnItemSelectedListener(navListener);
            // Load the default fragment
            if (savedInstanceState == null) {
                loadFragment(new ShopFrontFragment());
                bottomNav.setSelectedItemId(R.id.nav_shop_front);
            }
        }
            private final BottomNavigationView.OnItemSelectedListener navListener =
                item -> {
                    Fragment selectedFragment = null;
                    boolean isMenuFragment = false;

                    int itemId = item.getItemId();
                    if (itemId == R.id.nav_take_order) {
                        selectedFragment = new TakeOrderFragment();//have created the class  used to create the constrctor and store  call as  per  constructor
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
                        selectedFragment = new ShopFrontFragment(); // Default
                    }

                    if (isMenuFragment) {
                        appBarLayout.setVisibility(View.GONE);
                        infoBar.setVisibility(View.GONE);
                        relativescreen.setVisibility(View.VISIBLE);// check the visibility of the code
                    } else {
                        appBarLayout.setVisibility(View.VISIBLE);
                        infoBar.setVisibility(View.VISIBLE);
                        relativescreen.setVisibility(View.GONE);
                    }

            loadFragment(selectedFragment);
            return true;
        };

        @Override
        public void onBackPressed() {
            // (like the "View More" CategoriesFragment)
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                super.onBackPressed();}
            else if (bottomNav.getSelectedItemId() == R.id.nav_shop_front) {
                showExitDialog();}
            else {
                super.onBackPressed();}
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
            // we keep the one fregment constant  and make teh changing  in the  every single screen
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
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
            Intent intent = new Intent(TakeOrderActivity.this,CartActivity.class);
            startActivity(intent);
        }
}