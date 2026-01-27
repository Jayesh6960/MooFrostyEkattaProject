package com.example.moofrosty.ui.attendance.leave;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moofrosty.R;
import com.google.android.material.tabs.TabLayout;

public class LeaveActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    Toolbar toolbar ;
    ImageView btnBack;
    ImageView btnMenu;
    TextView tvTitle;
    TextView tvDate ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_leave);
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        windowInsetsController.setAppearanceLightStatusBars(true);

        btnBack = findViewById(R.id.btn_back);
        toolbar = findViewById(R.id.dashboard_toolbar);
        setSupportActionBar(toolbar);
        btnMenu = findViewById(R.id.btn_menu);
        tvTitle = findViewById(R.id.tv_title);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.app_bar_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.fragment_container), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), systemBars.bottom+16);
            return insets;
        });

        tvTitle.setText("Leave");
        btnBack.setVisibility(View.VISIBLE);
        btnMenu.setVisibility(View.GONE);
        btnBack.setOnClickListener(v -> onBackPressed());

        tabLayout = findViewById(R.id.tab_layout);

        // 2. Setup Tabs Manually
        tabLayout.addTab(tabLayout.newTab().setText("Apply Leave"));
        tabLayout.addTab(tabLayout.newTab().setText("Leave History"));

        // 3. Load Default Fragment (Apply Leave)
        loadFragment(new ApplyLeaveFragment());

        // 4. Tab Selection Listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    loadFragment(new ApplyLeaveFragment());
                } else {
                    loadFragment(new LeaveHistoryFragment());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }
}