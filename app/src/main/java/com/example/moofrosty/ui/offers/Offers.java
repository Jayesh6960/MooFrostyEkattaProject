package com.example.moofrosty.ui.offers;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.moofrosty.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;

public class Offers extends AppCompatActivity {

    private TabLayout tabLayout;

    ImageView headerBackArrow;
    ImageButton iconScan;
    TextView headerTitle;
    LinearLayout titleLayout;
    TextInputLayout searchBarLayout;
    AppBarLayout appBarLayout;
    FrameLayout frame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_offers);

        initViews();
        handleInsets();
        setupToolbar();
        setupTabs();
        // Load default fragment → All Offers
        loadFragment(new AllOffersFragment());
       // loadFragment(new com.example.moofrosty.ui.offers.TopOffersFragment());
    }

    private void initViews() {
        tabLayout = findViewById(R.id.tab_layout);
        headerBackArrow = findViewById(R.id.header_back_arrow);
        iconScan = findViewById(R.id.icon_scan);
        headerTitle = findViewById(R.id.header_title);
        titleLayout = findViewById(R.id.toolbarlayout);
        searchBarLayout = findViewById(R.id.search_bar_layout);
        appBarLayout = findViewById(R.id.app_bar_layout);
        frame =findViewById(R.id.fragment_container);
    }

    private void handleInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(appBarLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });
        ViewCompat.setOnApplyWindowInsetsListener(frame, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), systemBars.bottom);
            return insets;
        });
    }

    private void setupToolbar() {

        searchBarLayout.setVisibility(View.GONE);
        titleLayout.setVisibility(View.VISIBLE);
        iconScan.setVisibility(View.GONE);

        headerTitle.setText("Offers");
        headerTitle.setTextColor(ContextCompat.getColor(this, R.color.black));

        headerBackArrow.setOnClickListener(v -> finish());
    }

    private void setupTabs() {

        tabLayout.removeAllTabs();

        tabLayout.addTab(tabLayout.newTab().setText("All Offers"));
        tabLayout.addTab(tabLayout.newTab().setText("Top Offers"));

        // Highlight first tab
        TabLayout.Tab firstTab = tabLayout.getTabAt(0);
        if (firstTab != null) firstTab.select();

        // Tab click listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                Fragment fragment = null;

                if (tab.getPosition() == 0) {
                    fragment = new AllOffersFragment();
                } else if (tab.getPosition() == 1) {
                    fragment = new TopOffersFragment();
                }

                if (fragment != null){
                    loadFragment(fragment);
                }
                else {
                    loadFragment(fragment);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
