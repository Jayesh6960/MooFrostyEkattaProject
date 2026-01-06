package com.example.moofrosty.ui.newstorecreation;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moofrosty.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class NewStoreActivity extends AppCompatActivity {

    private NewStoreListViewModel viewModel;
    private TextView tvDate;
    private RecyclerView recyclerView;
    private NewStoreListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_store);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Init Views
        ImageView btnBack = findViewById(R.id.btn_back);
        TextView tvTitle = findViewById(R.id.tv_toolbar_title);
        tvDate = findViewById(R.id.tv_date_picker);
        recyclerView = findViewById(R.id.recycler_new_stores);
        FloatingActionButton fabAdd = findViewById(R.id.fab_add_store);

        // 2. Setup ViewModel
        viewModel = new ViewModelProvider(this).get(NewStoreListViewModel.class);

        // 3. Setup Toolbar
        tvTitle.setText("New Store Creation");
        btnBack.setOnClickListener(v -> finish());

        // 4. Setup Recycler
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 5. Date Picker Logic
        tvDate.setOnClickListener(v -> showDatePicker());

        // 6. FAB Logic
        fabAdd.setOnClickListener(v -> {
            // Opens the Create Store Form
            // Ensure you have CreateStoreFormActivity created or change this line
            Intent intent = new Intent(NewStoreActivity.this, StoreOtpVerificationActivity.class);
            startActivity(intent);
            Toast.makeText(this, "new screen open", Toast.LENGTH_SHORT).show();
        });

        // 7. Observers
        viewModel.getSelectedDate().observe(this, date -> tvDate.setText(date));

        viewModel.getStoreList().observe(this, list -> {
            if (list != null) {
                adapter = new NewStoreListAdapter(list);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            viewModel.setDate(year, month, dayOfMonth);
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }
}