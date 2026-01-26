//package com.example.moofrosty.ui.attendance.profile;
//
//import android.os.Bundle;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.activity.EdgeToEdge;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//import androidx.fragment.app.Fragment;
//
//import com.example.moofrosty.R;
//import com.example.moofrosty.ToolbarHelper;
//import com.google.android.material.appbar.AppBarLayout;
//import com.google.android.material.tabs.TabLayout;
////Code Updated date 23-01-2026
//public class ProfileActivity extends AppCompatActivity {
//
//    private TabLayout tabLayout;
////    private TextView title;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_profile);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.app_bar_layout), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom());
//            return insets;
//        });
//        ImageView btnBack = findViewById(R.id.btn_back);
//        btnBack.setOnClickListener(v -> finish());
//        // TabLayout
//        tabLayout = findViewById(R.id.tabLayout);
//        ToolbarHelper.setupToolbar(this, "Profile", true, false);
////        title.setText("Profile Details");
//
//        // Load default fragment (Details)
//        loadFragment(new DetailsFragment());
//        // Tab selection listener
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(@NonNull TabLayout.Tab tab) {
//
//                Fragment fragment = null;
////Commnet Out file b out file  Not yet Chn
//                switch (tab.getPosition()) {
//                    case 0:
//                        fragment = new DetailsFragment();
//                        break;
//                    case 1:
//                        fragment = new BankDetailsFragment();
//                        break;
////                    case 1:
////                    //    fragment = new PositionFragment();
////                        break;
////                    case 2:
////                    //    fragment = new KYCFragment();
////                        break;
////                    case 3:
////                    //    fragment = new BankFragment();
////                        break;
////                    case 4:
////                    //    fragment = new SalaryFragment();
////                        break;
//                }
//
//                if (fragment != null) {
//                    loadFragment(fragment);
//                }
//            }
//
//            @Override
//            public void onTabUnselected(@NonNull TabLayout.Tab tab) {
//                // No action needed
//            }
//
//            @Override
//            public void onTabReselected(@NonNull TabLayout.Tab tab) {
//                // Optional: reload fragment
//            }
//        });
//    }
//
//    private void loadFragment(Fragment fragment) {
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.fragmentContainer, fragment)
//                .commit();
//    }
//}
package com.example.moofrosty.ui.attendance.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.moofrosty.R;
import com.example.moofrosty.ToolbarHelper;
import com.google.android.material.tabs.TabLayout;

// Code Updated date 23-01-2026 (FIXED VERSION)
public class ProfileActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private Toolbar toolbar;
    TextView tvTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // DO NOT use EdgeToEdge here – it breaks fixed toolbar
        setContentView(R.layout.activity_profile);

        // Setup Toolbar using your helper
//        ToolbarHelper.setupToolbar(this, "Profile", true, false);


        // Back button from toolbar layout
        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        toolbar = findViewById(R.id.dashboard_toolbar);
        setSupportActionBar(toolbar);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("Profile");
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(v -> finish());

        // TabLayout
        tabLayout = findViewById(R.id.tabLayout);

        // Load default fragment (Details tab)
        loadFragment(new DetailsFragment());

        // Select first tab by default
        TabLayout.Tab firstTab = tabLayout.getTabAt(0);
        if (firstTab != null) {
            firstTab.select();
        }

        // Tab selection listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(@NonNull TabLayout.Tab tab) {

                Fragment fragment = null;

                switch (tab.getPosition()) {
                    case 0:
                        fragment = new DetailsFragment();
                        break;

                    case 1:
                        fragment = new BankDetailsFragment();
                        break;
                }

                if (fragment != null) {
                    loadFragment(fragment);
                }
            }

            @Override
            public void onTabUnselected(@NonNull TabLayout.Tab tab) {
                // No action needed
            }

            @Override
            public void onTabReselected(@NonNull TabLayout.Tab tab) {
                // Optional: reload fragment if needed
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}
