package com.example.moofrosty;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;

public class DamageExpiryActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    ImageView headerbackarrow;
    AppBarLayout appbarlayout;
    androidx.appcompat.widget.Toolbar toolbars;
    TextView headertitle;
    ImageButton iconscan;
    LinearLayout titlelayout;
    TextInputLayout searbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_damage_expiry);
        searbar =findViewById(R.id.search_bar_layout);
        titlelayout = findViewById(R.id.toolbarlayout);
        searbar.setVisibility(View.GONE);
        titlelayout.setVisibility(View.VISIBLE);
        appbarlayout = findViewById(R.id.app_bar_layout);
        ViewCompat.setOnApplyWindowInsetsListener(appbarlayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });

        String title = getIntent().getStringExtra("ACTIVITY_TITLE");

        toolbars = findViewById(R.id.toolbars);
        headertitle = findViewById(R.id.header_title);
        iconscan = findViewById(R.id.icon_scan);
        iconscan.setVisibility(View.GONE);
        headertitle.setText(title);
        headertitle.setTextColor(getResources().getColor(R.color.black));

        headerbackarrow = findViewById(R.id.header_back_arrow);
        tabLayout = findViewById(R.id.tab_layout);
        headerbackarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setupCustomTabs();
        loadFragment(new AllRequestsFragment());

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updateTabAppearance(tab, true); // Highlight selected tab

                Fragment selectedFragment = null;
                int position = tab.getPosition();
                if (position == 0) {
                    selectedFragment = new AllRequestsFragment();
                } else if (position == 1) {
                    selectedFragment = new SalesEdgeFragment();
                } else if (position == 2) {
                    selectedFragment = new ShikharFragment();
                }

                if (selectedFragment != null) {
                    loadFragment(selectedFragment);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                updateTabAppearance(tab, false); // Un-highlight unselected tab
            }

            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupCustomTabs() {
        // Add tabs with custom views
        tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView("All")));
        tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView("SalesEdge")));
        tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView("Shikhar")));

        // Select the first tab initially
        TabLayout.Tab firstTab = tabLayout.getTabAt(0);
        if (firstTab != null) {
            firstTab.select();
            updateTabAppearance(firstTab, true);
        }
    }

    private View createTabView(String text) {
        View view = getLayoutInflater().inflate(R.layout.custom_tab_item, null);
        TextView tv = view.findViewById(R.id.tab_text);
        tv.setText(text);
        return view;
    }

    private void updateTabAppearance(TabLayout.Tab tab, boolean isSelected) {
        View view = tab.getCustomView();
        if (view != null) {
            TextView tv = view.findViewById(R.id.tab_text);
            if (isSelected) {
                tv.setBackgroundResource(R.drawable.tab_selected_bg);
                tv.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            } else {
                tv.setBackgroundResource(R.drawable.tab_unselected_bg);
                tv.setTextColor(ContextCompat.getColor(this, R.color.grey));
            }
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}