package com.example.moofrosty.ui.dashboard;

import static com.example.moofrosty.core.network.Resource.Status.ERROR;
import static com.example.moofrosty.core.network.Resource.Status.LOADING;
import static com.example.moofrosty.core.network.Resource.Status.SUCCESS;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.moofrosty.core.network.ApiClient;
import com.example.moofrosty.core.network.ApiService;
import com.example.moofrosty.data.local.SessionManager;
import com.example.moofrosty.data.model.LoginResponse;
import com.example.moofrosty.data.model.UserDetailResponse;
import com.example.moofrosty.data.model.UserStatusResponse;
import com.example.moofrosty.ui.attendance.AttendanceActivity;
import com.example.moofrosty.R;
import com.example.moofrosty.ui.login.LoginActivity;
import com.example.moofrosty.ui.login.LoginViewModel;
import com.example.moofrosty.ui.newstorecreation.NewStoreActivity;
import com.example.moofrosty.ui.splash.BaseActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends BaseActivity{

   // private List<DashboardItem> currentListForDetail = new ArrayList<>();
  //  private DashboardViewModel viewModel;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNav;
   // private View topBarContainer, homeContainer, myBeatContainer;
    private ImageView btnMenu;
    SessionManager sessionManager;
    private TextView tvTitle;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;
    DashboardViewModel dashboardViewModel;



    // Dynamic Views
//    private TextView tvMocDropdown, tvTotalIncentives, tvViewMore;
//    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        windowInsetsController.setAppearanceLightStatusBars(true); // Changed to true for dark text on white status bar

        // 1. Init Views
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        bottomNav = findViewById(R.id.bottom_navigation);
        btnMenu = findViewById(R.id.btn_menu);
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        observeUserStatus();
        runnable = new Runnable() {
            @Override
            public void run() {
                String token = "Bearer " + sessionManager.getToken();
                dashboardViewModel.checkUserStatus(token);
                handler.postDelayed(this, 5000); // every 2 minutes
            }
        };


        // 2. Window Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.app_bar_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });

        ViewCompat.setOnApplyWindowInsetsListener(bottomNav, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), systemBars.bottom);
            return insets;
        });

        // 3. Load Default Fragment (Home)
        if (savedInstanceState == null) {
            loadFragment(new DashboardHomeFragment());
        }
        tvTitle = findViewById(R.id.tv_title);
        sessionManager = new SessionManager(this);
        // 3. Get Name and Set Text
        String userName = sessionManager.getUserFullName();
        Log.d("usernamedetail", "detailsname: " + userName);
        tvTitle.setText(userName);
        updateNavHeader();
        // 4. Click Listeners
        btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

            navigationView.setNavigationItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.menu_iteams) {
                    Intent intent = new Intent(DashboardActivity.this, AttendanceActivity.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.menu_store) {
                    // Open the New Store Creation List/History Page
                    Intent intent = new Intent(DashboardActivity.this, NewStoreActivity.class);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                } else if (id == R.id.menu_logout) {
                    sessionManager.logout();
                    Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
         //       Toast.makeText(this, "Clicked: " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            });

        // 5. Bottom Nav Switching
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                loadFragment(new DashboardHomeFragment());
                return true;
            } else if (id == R.id.nav_beat) {
                loadFragment(new DashboardMyBeatFragment());
                return true;
            }
            return false;
        });

        setupBackPressHandler();
    }
    @Override
    protected void onResume() {
        super.onResume();
//        checkUserActiveStatus();
        handler.post(runnable);
    }


    private void setupBackPressHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // 1. If Drawer is Open -> Close it
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return; // Stop here
                }

                // 2. Check current Tab
                int selectedItemId = bottomNav.getSelectedItemId();

                if (selectedItemId == R.id.nav_home) {
                    // We are on Home Tab -> Show Exit Dialog
                    showExitDialog();
                } else if (selectedItemId == R.id.nav_beat) {
                    // We are on Beat Tab -> Go back to Home Tab
                    bottomNav.setSelectedItemId(R.id.nav_home);
                } else {
                    // Fallback for any other future tabs -> Go Home
                    bottomNav.setSelectedItemId(R.id.nav_home);
                }
            }
        });
    }
//    private void checkUserActiveStatus() {
//
//        int userId = sessionManager.getUserId();
//        Log.d("StatusCheck123", "Checking status for userId: " + userId);
//
//        ApiService apiService = ApiClient.getApi();
//        Call<LoginResponse> call = apiService.getUserById(userId);
//
//        Log.d("StatusCheck", "API CALL STARTED");
//
//        call.enqueue(new Callback<LoginResponse>() {
//
//            @Override
//            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
//
//                Log.d("StatusCheck", "API RESPONSE RECEIVED");
//                Log.d("StatusCheck", "Response Code: " + response.code());
//
//                if (response.isSuccessful() && response.body() != null) {
//
//                    Log.d("StatusCheckgsondata", "Response Body: " + new Gson().toJson(response.body()));
//
//                    if (response.body().getData() != null) {
//
//                        String status = response.body().getData().getStatus();
//                        Log.d("StatusCheck", "Server status: " + status);
//
//                        if ("0".equals(status)) {
//
//                            Log.d("StatusCheck", "USER IS DEACTIVATED → LOGOUT");
//
//                            Toast.makeText(DashboardActivity.this,
//                                    "Your account has been deactivated by admin.",
//                                    Toast.LENGTH_LONG).show();
//
//                            logoutUser();
//                        } else {
//                            Log.d("StatusCheck", "USER IS ACTIVE");
//                        }
//
//                    } else {
//                        Log.d("StatusCheck", "Data is NULL in response");
//                    }
//
//                } else {
//                    Log.d("StatusCheck", "Response NOT successful");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<LoginResponse> call, Throwable t) {
//
//                Log.d("StatusCheck", "API FAILED: " + t.getMessage());
//            }
//        });
//    }



//    private void checkUserActiveStatus() {
//
//        String token = "Bearer " + sessionManager.getToken(); // 🔴 IMPORTANT
//
//        ApiService apiService = ApiClient.getApi();
//        Call<UserDetailResponse> call = apiService.getUserDetail(token);
//
//        Log.d("StatusCheck", "Token: " + token);
//        Log.d("StatusCheck", "URL: " + call.request().url());
//
//        call.enqueue(new Callback<UserDetailResponse>() {
//
//            @Override
//            public void onResponse(Call<UserDetailResponse> call, Response<UserDetailResponse> response) {
//
//                Log.d("StatusCheck", "Code: " + response.code());
//
//                if (response.isSuccessful() && response.body() != null
//                        && response.body().getData() != null) {
//
//                    String status = response.body().getData().getstatus();
//
//                    Log.d("StatusCheck", "Server status: " + status);
//
//                    if ("0".equals(status)) {
//
//                        Log.d("StatusCheck", "DEACTIVATED → LOGOUT");
//
//                        Toast.makeText(DashboardActivity.this,
//                                "Your account has been deactivated by admin.",
//                                Toast.LENGTH_LONG).show();
//
//                        logoutUser(); // 🔴 FORCE LOGOUT
//                    }
//
//                } else {
//                    Log.d("StatusCheck", "Data NULL or invalid response");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserDetailResponse> call, Throwable t) {
//                Log.d("StatusCheck", "Error: " + t.getMessage());
//            }
//        });
//    }
    private void showExitDialog() {
        // 1. Create the dialog but don't show it yet
        AlertDialog dialog = new android.app.AlertDialog.Builder(this)
                 .setTitle("Exit App")
                 .setMessage("Do you want to exit?")
                 .setPositiveButton("Yes", (d, which) -> {
                     finish();
                 })
                 .setNegativeButton("No", (d, which) -> {
                     d.dismiss();
                 })
                 .create();
        dialog.setOnShowListener(d -> {
            dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(ContextCompat.getColor(this, R.color.Purple_Color));
            dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(ContextCompat.getColor(this, R.color.Purple_Color));
        });
        dialog.show();
    }

    private void updateNavHeader() {
        View headerView = navigationView.getHeaderView(0);
        TextView tvHeaderName = headerView.findViewById(R.id.headerlayoutnamedashboard);
        TextView tvHeaderMobile = headerView.findViewById(R.id.headerlayoutnumberdashboard);
        String fullName = sessionManager.getUserFullName();
        String mobile = sessionManager.getUserMobile();
        tvHeaderName.setText(fullName);
        tvHeaderMobile.setText(mobile);
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
//    runnable = new Runnable() {
//        @Override
//        public void run() {
//
//            String token = "Bearer " + sessionManager.getToken();
//
//            Log.d("StatusCheck", "Calling API...");
//
//             // ✅ THIS LINE IS REQUIRED
//
//            handler.postDelayed(this, 5000);
//        }
//    };

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private void observeUserStatus() {

        dashboardViewModel.getUserStatus().observe(this, resource -> {

            if (resource != null) {

                switch (resource.status) {

                    case SUCCESS:

                        UserDetailResponse resp = resource.data;

                        if (resp != null && resp.getData() != null) {

                            String status = resp.getData().getstatus(); // ✅ FIXED

                            Log.d("StatusCheck", "Final Status: " + status);

                            if ("0".equals(status)) {

                                Toast.makeText(this,
                                        "User is inactive. Please contact admin",
                                        Toast.LENGTH_LONG).show();
                                logoutUser();
                            }
                        }
                        break;

                    case ERROR:
                        Log.d("StatusCheck", "Error: " + resource.message);
                        break;
                }
            }
        });
    }

}



