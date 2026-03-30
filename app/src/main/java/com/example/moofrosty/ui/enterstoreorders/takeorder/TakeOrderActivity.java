    package com.example.moofrosty.ui.enterstoreorders.takeorder;

    import android.app.ProgressDialog;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.os.Bundle;
    import android.text.Editable;
    import android.text.TextWatcher;
    import android.util.Log;
    import android.view.LayoutInflater;
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
    //import androidx.activity.OnBackPressedCallback;
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

    import com.example.moofrosty.core.utils.NetworkUtil;
    import com.example.moofrosty.data.local.SessionManager;
    import com.example.moofrosty.data.model.Store;
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
    import com.google.android.material.bottomsheet.BottomSheetDialog;
    import com.google.android.material.button.MaterialButton;
    import com.google.android.material.textfield.TextInputEditText;
    import com.google.android.material.textfield.TextInputLayout;

    import java.text.SimpleDateFormat;
    import java.util.Date;
    import java.util.Locale;
//Logs already Store at the backend Side in then database
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

            private TakeOrderActivityViewModel takeOrderViewModel;
            private ImageButton iconCart;
            private TextView cartBadge, tvcartitemcount, tvcarttotalprice;
            private FrameLayout floatingCartBar; // Find it here

            FragmentManager fragmentManager;
            TextInputEditText searchbar;
            private SessionManager sessionManager;
            private Store currentStore;
            private ProgressDialog progressDialog;


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

                // --- Init Managers & Data ---
                sessionManager = new SessionManager(this);
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Checking Out...");
                progressDialog.setCancelable(false);

                // Retrieve Store Object passed from previous activity
                if (getIntent().getSerializableExtra("STORE_DATA") != null) {
                    currentStore = (Store) getIntent().getSerializableExtra("STORE_DATA");
                }


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

                takeOrderViewModel = new ViewModelProvider(this).get(TakeOrderActivityViewModel.class);

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
//            iconPower.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    showCheckOutDialog();
//                }
//            });

//                iconPower.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        Intent intent = new Intent(TakeOrderActivity.this, StoreProfileActivity.class);
////                        startActivity(intent);
//                        Intent intent = new Intent(TakeOrderActivity.this, StoreProfileActivity.class);
//                        if (getIntent().getSerializableExtra("STORE_DATA") != null) {
//                            intent.putExtra("STORE_DATA", getIntent().getSerializableExtra("STORE_DATA"));
//                        }
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        cartViewModel.clearCart();
//                        finish();
//                        showCheckOutDialog();
//                    }
//                });
            iconPower.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Save distance first
                    // Show dialog if required
                    showCheckOutDialog();

//                    Intent intent = new Intent(TakeOrderActivity.this, StoreProfileActivity.class);
//
//                    if (getIntent().getSerializableExtra("STORE_DATA") != null) {
//                        intent.putExtra("STORE_DATA",
//                                getIntent().getSerializableExtra("STORE_DATA"));
//                    }
//
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                            Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                    cartViewModel.clearCart();
//
//                    startActivity(intent);
//                    finish();
                }
            });
//            iconPower.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    // Check if cart has items (order taken)
//                    if (cartViewModel.getCheckoutResult() != null && !cartViewModel.getCheckoutResult().isInitialized()) {
//
//                        // ✅ Order already taken → Skip reason dialog
//                        logoutUser();
//
//                    } else {
//
//                        // ❌ No order taken → Show reason dialog
//                        showCheckOutDialog();
//                    }
//                }
//            });

            setupCheckOutObserver();
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

            getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    // 1. Get the current selected item ID from Bottom Nav
                    int selectedItemId = bottomNav.getSelectedItemId();

                    // 2. Logic: If NOT on Shop Front, go there. If ON Shop Front, show Toast.
                    if (selectedItemId == R.id.nav_shop_front) {
                        // We are on Home Tab -> Show message, do NOT exit
                        Toast.makeText(TakeOrderActivity.this, "Please Logout If You Want to Exit from Store", Toast.LENGTH_SHORT).show();
                    } else {
                        // We are on other tabs -> Navigate back to Shop Front
                        bottomNav.setSelectedItemId(R.id.nav_shop_front);
                    }
                }
            });
        }

        private void showCheckOutDialog() {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
            View sheetView = LayoutInflater.from(this).inflate(R.layout.bottom_layout_material, null); // Make sure layout name matches
            bottomSheetDialog.setContentView(sheetView);

            // Bind Views from Bottom Sheet
            TextView tvTitle = sheetView.findViewById(R.id.tvTitle); // Assuming ID is tvTitle
            ImageView ivClose = sheetView.findViewById(R.id.ivClose);
            TextInputEditText tvStore = sheetView.findViewById(R.id.tvStore);
            TextInputLayout tilBeat = sheetView.findViewById(R.id.tvBeat).getParent() instanceof TextInputLayout ? (TextInputLayout) sheetView.findViewById(R.id.tvBeat).getParent() : null; // Get layout to hide
            View beatView = sheetView.findViewById(R.id.tvBeat); // The edit text itself

            // Hide Beat Field as requested
            if(tilBeat != null) tilBeat.setVisibility(View.GONE);
            else if(beatView != null) ((View)beatView.getParent()).setVisibility(View.GONE); // Fallback

            AutoCompleteTextView spReason = sheetView.findViewById(R.id.spReason);
            MaterialButton btnConfirm = sheetView.findViewById(R.id.btnConfirm);

            // Populate Data
            if (currentStore != null) {
                tvStore.setText(currentStore.getStoreName());
            }

            // Setup Dropdown
            String[] reasons = {"Order Taken", "Stock Available", "Outlet is Closed", "Owner is not available"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, reasons);
            spReason.setAdapter(adapter);

            // Close Button
            ivClose.setOnClickListener(v -> bottomSheetDialog.dismiss());

            // Confirm Button
//            btnConfirm.setOnClickListener(v -> {
//                String selectedReason = spReason.getText().toString();
//
//                if (selectedReason.isEmpty()) {
//                    Toast.makeText(this, "Please select a reason", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (!NetworkUtil.isNetworkAvailable(this)) {
//                    Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (currentStore != null) {
//                    // Call API
//                    String token = sessionManager.getToken();
//                    takeOrderViewModel.performCheckOut(token, currentStore.getShopId(), selectedReason);
//                    bottomSheetDialog.dismiss();
//                } else {
//                    Toast.makeText(this, "Outlet Data Missing", Toast.LENGTH_SHORT).show();
//                }
//            });
            btnConfirm.setOnClickListener(v -> {

                String selectedReason = spReason.getText().toString();

                if (selectedReason.isEmpty()) {
                    Toast.makeText(this, "Please select a reason", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!NetworkUtil.isNetworkAvailable(this)) {
                    Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (currentStore != null) {

                    // ✅ Capture OUT time
                    String outTime = getCurrentDateTime();

                    SharedPreferences prefs = getSharedPreferences("store_data", MODE_PRIVATE);

                    prefs.edit()
                            .putString("store_out_time", outTime)
                            .apply();

                    Log.d("StoreTiming", "OUT Time Saved: " + outTime);

                    // ✅ Call API
                    String token = sessionManager.getToken();
                    takeOrderViewModel.performCheckOut(token, currentStore.getShopId(), selectedReason);

                    bottomSheetDialog.dismiss();

                } else {
                    Toast.makeText(this, "Outlet Data Missing", Toast.LENGTH_SHORT).show();
                }
            });


            bottomSheetDialog.show();
        }
//    private void logoutUser() {
//        Intent intent = new Intent(TakeOrderActivity.this, StoreProfileActivity.class);
//
//        if (getIntent().getSerializableExtra("STORE_DATA") != null) {
//            intent.putExtra("STORE_DATA", getIntent().getSerializableExtra("STORE_DATA"));
//        }
//
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        startActivity(intent);
//
//        cartViewModel.clearCart(); // clear cart after logout
//        finish();
//    }
        private String getCurrentDateTime() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return sdf.format(new Date());
        }

        private void setupCheckOutObserver() {
            takeOrderViewModel.getCheckoutStatus().observe(this, resource -> {
                switch (resource.status) {
                    case LOADING:
                        progressDialog.show();
                        break;

                    case SUCCESS:
                        progressDialog.dismiss();
                        // Show success message from API if available, else default
                        String msg = resource.data != null ? resource.data.getMessage() : "Checked Out Successfully";
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                        performExitLogic();
                        break;

                    case ERROR:
                        progressDialog.dismiss();
                        String errorMsg = resource.message;
                        // Handle specific error format from API (e.g. "The storein field is required")
                        Toast.makeText(this, "Something Error", Toast.LENGTH_LONG).show();
                        break;
                }
            });
        }

        private void performExitLogic() {
            // 1. Clear Cart
            cartViewModel.clearCart();

            // 2. Navigate to StoreProfile
            Intent intent = new Intent(TakeOrderActivity.this, StoreProfileActivity.class);
            if (currentStore != null) {
                intent.putExtra("STORE_DATA", currentStore);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
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
                        //    isMenuFragment = true;
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


    //    @Override
    //    public void onBackPressed() {
    //        // 1. Check if the fragment back stack is NOT empty
    //
    //        int selectedItemId = bottomNav.getSelectedItemId();
    //
    //        // 1. If currently on "Shop Front" (Home) -> Show Toast, Do NOT Exit
    //        if (selectedItemId == R.id.nav_shop_front) {
    //            Toast.makeText(this, "Please Logout If You Want to Exit from Store", Toast.LENGTH_SHORT).show();
    //        }
    //        // 2. If on any OTHER tab -> Go back to "Shop Front"
    //        else {
    //            bottomNav.setSelectedItemId(R.id.nav_shop_front);
    //        }
    ////        if (fragmentManager.getBackStackEntryCount() > 0) {
    ////            // 2. Pop the fragment off the stack
    ////            fragmentManager.popBackStack();
    ////            // 3. IMPORTANT: Update the BottomNavigationView immediately
    ////            //    (You must implement the method below)
    ////            updateBottomNavigationSelection();
    ////
    ////        } else {
    ////            // 4. If the stack is empty, perform the default back action (finish the activity)
    ////        //    super.onBackPressed();
    ////        }
    //    }

        private void updateBottomNavigationSelection() {
            // 1. Get the currently visible fragment
            //    (You must know the ID of your fragment container)
            Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);

            if (currentFragment != null) {

                // 2. Check the type of the visible fragment and select the matching Bottom Nav item
                if (currentFragment instanceof ShopFrontFragment) {
    //                Intent intent = new Intent(TakeOrderActivity.this, ActionPointActivitys.class);
    //                startActivity(intent);
                    Toast.makeText(this, "Please Logout If Uou Want to Exit from Store", Toast.LENGTH_SHORT).show();
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
    ///       fragmentTransaction.addToBackStack("TakeOrderFragment");                              ////  for not back for now
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