package com.example.moofrosty.ui.attendance.profile;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.moofrosty.R;
import com.google.android.material.tabs.TabLayout;

public class ProfileActivity extends AppCompatActivity {

    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());
        // TabLayout
        tabLayout = findViewById(R.id.tabLayout);

        // Load default fragment (Details)
        loadFragment(new DetailsFragment());

        // Tab selection listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(@NonNull TabLayout.Tab tab) {

                Fragment fragment = null;
//are used to decleared the class name as the constctor
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new DetailsFragment();
                        break;
                    case 1:
                        fragment = new BankDetailsFragment();
                        break;
//                    case 1:
//                    //    fragment = new PositionFragment();
//                        break;
//                    case 2:
//                    //    fragment = new KYCFragment();
//                        break;
//                    case 3:
//                    //    fragment = new BankFragment();
//                        break;
//                    case 4:
//                    //    fragment = new SalaryFragment();
//                        break;
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
                // Optional: reload fragment
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