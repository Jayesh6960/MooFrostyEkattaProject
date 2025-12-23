package com.example.moofrosty.ui.store;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.moofrosty.R;
import com.example.moofrosty.data.model.Store;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class StoreProfileActivity extends AppCompatActivity {


    private StoreProfileViewModel viewModel;
    private Store currentStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_store_profile);
        if (getIntent().getExtras() != null) {
            currentStore = (Store) getIntent().getSerializableExtra("STORE_DATA");
        }

        // 2. Setup ViewModel
        viewModel = new ViewModelProvider(this).get(StoreProfileViewModel.class);
        if (currentStore != null) {
            viewModel.setStore(currentStore);
        }

        // 3. Init Views
        ImageView btnBack = findViewById(R.id.btn_back);
        TextView tvTitle = findViewById(R.id.tv_toolbar_title);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager2 viewPager = findViewById(R.id.view_pager);
        Button btnEnterStore = findViewById(R.id.btn_enter_store);

        // 4. Window Insets (Padding for StatusBar/NavBar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_container), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 5. Setup Toolbar
        if (currentStore != null) {
            tvTitle.setText(currentStore.getName() + " - HULI");
        }
        btnBack.setOnClickListener(v -> finish());

        // 6. Setup Tabs & ViewPager
        StorePagerAdapter adapter = new StorePagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) tab.setText("Store Profile");
            else tab.setText("Order History");
        }).attach();

        // 7. Enter Store Button Logic
        btnEnterStore.setOnClickListener(v -> {
            Toast.makeText(this, "Entering Store: " + currentStore.getName(), Toast.LENGTH_SHORT).show();
            // Navigate to Order Taking screen or similar
        });
    }
}