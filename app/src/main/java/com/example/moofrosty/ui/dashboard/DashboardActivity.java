package com.example.moofrosty.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
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

import com.example.moofrosty.data.local.SessionManager;
import com.example.moofrosty.ui.attendance.AttendanceActivity;
import com.example.moofrosty.R;
import com.example.moofrosty.ui.login.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class DashboardActivity extends AppCompatActivity {

   // private List<DashboardItem> currentListForDetail = new ArrayList<>();
  //  private DashboardViewModel viewModel;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNav;
   // private View topBarContainer, homeContainer, myBeatContainer;
    private ImageView btnMenu;
    SessionManager sessionManager;


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

//        // 2. Initialize Views
//        drawerLayout = findViewById(R.id.drawer_layout);
//        navigationView = findViewById(R.id.nav_view);
//        bottomNav = findViewById(R.id.bottom_navigation);
//        topBarContainer = findViewById(R.id.top_bar_container);
//        //relativeScreen = findViewById(R.id.relativelayoutmenu);
//        btnMenu = findViewById(R.id.btn_menu);
//
//        homeContainer = findViewById(R.id.home_container);
//        myBeatContainer = findViewById(R.id.text_my_beat);
//
//        tvMocDropdown = findViewById(R.id.tv_moc_dropdown);
//        tvTotalIncentives = findViewById(R.id.tv_total_incentives);
//        tvViewMore = findViewById(R.id.tv_view_more);
//        recyclerView = findViewById(R.id.dashboard_recycler);
//
//        // 3. Apply Window Insets (Your requested logic)
//        ViewCompat.setOnApplyWindowInsetsListener(topBarContainer, (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            // Added padding to top to avoid status bar overlap
//            v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom());
//            return insets;
//        });
//
//        ViewCompat.setOnApplyWindowInsetsListener(bottomNav, (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), systemBars.bottom);
//            return insets;
//        });
//
//        // 4. MVVM Setup
//        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
//
//        // Grid setup: 2 columns
////        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.HORIZONTAL, false);
//        recyclerView.setLayoutManager(gridLayoutManager);
//        // Observer
//        viewModel.getDashboardItems().observe(this, items -> {
//            // YOU MUST SAVE THE DATA HERE
//            currentListForDetail = items;
//
//            DashboardAdapter adapter = new DashboardAdapter(items);
//            recyclerView.setAdapter(adapter);
//        });
//
//        viewModel.getTotalIncentives().observe(this, value -> {
//            if(tvTotalIncentives != null) tvTotalIncentives.setText(value);
//        });
//
//        // 5. Drawer / Menu Logic
//        btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
//
//        navigationView.setNavigationItemSelectedListener(item -> {
//            // Handle Drawer clicks here
//            drawerLayout.closeDrawer(GravityCompat.START);
//            return true;
//        });
//
////        bottomNav.setOnItemSelectedListener(item -> {
////            // Handle Bottom Nav clicks here
////            return true;
////        });
//        bottomNav.setOnItemSelectedListener(item -> {
//            int id = item.getItemId();
//            if (id == R.id.nav_home) {
//                homeContainer.setVisibility(View.VISIBLE);
//                myBeatContainer.setVisibility(View.GONE);
//                return true;
//            } else if (id == R.id.nav_beat) {
//                homeContainer.setVisibility(View.GONE);
//                myBeatContainer.setVisibility(View.VISIBLE);
//                return true;
//            }
//            return false;
//        });
//
//        tvMocDropdown.setOnClickListener(v -> {
//            PopupMenu popup = new PopupMenu(DashboardActivity.this, v);
//            // Adding specific requested date ranges
//            popup.getMenu().add("MOC 12 (01 Dec - 31 Dec)");
//            popup.getMenu().add("MOC 11 (01 Nov - 30 Nov)");
//            popup.getMenu().add("MOC 10 (01 Oct - 31 Oct)");
//
//            popup.setOnMenuItemClickListener(menuItem -> {
//                String fullTitle = menuItem.getTitle().toString();
//                // Extract "MOC 12" to show in the header
//                String shortTitle = fullTitle.split("\\(")[0].trim();
//                tvMocDropdown.setText(shortTitle);
//
//                // Pass full title to Repo or just MOC name?
//                // Repo uses "MOC 12" string match.
//                viewModel.loadData(shortTitle);
//                return true;
//            });
//            popup.show();
//        });
//
//        // --- VIEW MORE LOGIC ---
//        tvViewMore.setOnClickListener(v -> {
//            if (currentListForDetail == null || currentListForDetail.isEmpty()) {
//                Toast.makeText(DashboardActivity.this, "No data to show", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            Intent intent = new Intent(DashboardActivity.this, ViewMoreDetailsActivity.class);
//            // Pass the data
//            intent.putExtra("DATA_LIST", (Serializable) currentListForDetail);
//            // Pass the Title (e.g., "MOC 12")
//            intent.putExtra("TITLE", tvMocDropdown.getText().toString());
//            startActivity(intent);
//        });

        // 1. Init Views
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        bottomNav = findViewById(R.id.bottom_navigation);
        btnMenu = findViewById(R.id.btn_menu);

        // 2. Window Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.top_bar_container), (v, insets) -> {
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

        sessionManager = new SessionManager(this);

        // 4. Click Listeners
        btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

            navigationView.setNavigationItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.menu_iteams) {
                    Intent intent = new Intent(DashboardActivity.this, AttendanceActivity.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.menu_logout) {
                    sessionManager.logout();
                    Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                    startActivity(intent);
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
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}