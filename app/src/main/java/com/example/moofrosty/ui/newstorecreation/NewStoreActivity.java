package com.example.moofrosty.ui.newstorecreation;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moofrosty.R;
import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.core.utils.NetworkUtil;
import com.example.moofrosty.data.local.SessionManager;
import com.example.moofrosty.data.model.StoreListResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class NewStoreActivity extends AppCompatActivity {

    private NewStoreListViewModel viewModel;
    private TextView tvDate, tvEmpty, tvTitle;
    private RecyclerView recyclerView;
    private NewStoreListAdapter adapter;
    private SessionManager sessionManager;
    private ProgressBar progressBar;
    ImageView btnBack ,btnMenu;
    FloatingActionButton fabAdd;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_store);
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        windowInsetsController.setAppearanceLightStatusBars(true);

        fabAdd = findViewById(R.id.fab_add_store);
        toolbar = findViewById(R.id.dashboard_toolbar);
        setSupportActionBar(toolbar);
        btnBack = findViewById(R.id.btn_back);
        btnMenu = findViewById(R.id.btn_menu);
        tvTitle = findViewById(R.id.tv_title);
        tvDate = findViewById(R.id.tv_date_picker);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.app_bar_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.frame_newStore), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), systemBars.bottom+16);
            return insets;
        });

        tvTitle.setText("New Store Creation");
        btnBack.setVisibility(View.VISIBLE);
        btnMenu.setVisibility(View.GONE);
        tvDate.setVisibility(View.VISIBLE);


        sessionManager = new SessionManager(this);
        // 1. Init Views
//        ImageView btnBack = findViewById(R.id.btn_back);
//        TextView tvTitle = findViewById(R.id.tv_toolbar_title);
//        tvDate = findViewById(R.id.tv_date_picker);
        tvEmpty = findViewById(R.id.tv_empty_state);
        recyclerView = findViewById(R.id.recycler_new_stores);
        progressBar = findViewById(R.id.progress_bar);


        // 2. Setup ViewModel
        viewModel = new ViewModelProvider(this).get(NewStoreListViewModel.class);

        // 3. Setup Toolbar
        tvTitle.setText("New Store Creation");
        btnBack.setOnClickListener(v -> finish());

        // 4. Setup Recycler
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tvDate.setOnClickListener(v -> showDatePicker());

        // 6. FAB Logic
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(NewStoreActivity.this, StoreOtpVerificationActivity.class);
            startActivity(intent);
        });

        // 7. Observers
        viewModel.getSelectedDate().observe(this, date -> tvDate.setText(date));

        // --- API & LOADING LOGIC ---
        viewModel.getStoreList().observe(this, resource -> {
            if (resource == null) return;

            switch (resource.status) {
                case LOADING:
                    progressBar.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    tvEmpty.setVisibility(View.GONE);
                    break;

                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    if (resource.data != null && !resource.data.isEmpty()) {
                        recyclerView.setVisibility(View.VISIBLE);
                        tvEmpty.setVisibility(View.GONE);
                        adapter = new NewStoreListAdapter(resource.data, this::showStoreDetailsPopup);
                        recyclerView.setAdapter(adapter);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        tvEmpty.setVisibility(View.VISIBLE);
                    }
                    break;

                case ERROR:
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    tvEmpty.setVisibility(View.VISIBLE);
                    tvEmpty.setText(resource.message != null ? resource.message : "Something went wrong");
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        // 8. Initial Load with Network Check
        checkNetworkAndLoad();
    }

    private void checkNetworkAndLoad() {
        if (NetworkUtil.isNetworkAvailable(this)) {
            // Passing token starts the fetch
            viewModel.setToken(sessionManager.getToken());
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
            // Optional: Show a retry button or empty state
        }
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, R.style.CustomDatePickerTheme,
                (view, year, month, dayOfMonth) -> {
                    if (NetworkUtil.isNetworkAvailable(this)) {
                        viewModel.setDate(year, month, dayOfMonth);
                    } else {
                        Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        );
        dialog.setOnShowListener(d -> {
            dialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
                    .setTextColor(ContextCompat.getColor(this, R.color.Purple_Color));
            dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
                    .setTextColor(ContextCompat.getColor(this, R.color.Purple_Color));
        });
        dialog.show();
    }

    private void showStoreDetailsPopup(StoreListResponse.StoreModel item) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_store_details);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }

        TextView tvProposalId = dialog.findViewById(R.id.tv_proposal_id);
        TextView tvStoreId = dialog.findViewById(R.id.tv_store_id);
        TextView tvStoreName = dialog.findViewById(R.id.tv_store_name);
        TextView tvMobile = dialog.findViewById(R.id.tv_mobile);
        TextView tvStatus = dialog.findViewById(R.id.tv_status);
        TextView tvReason = dialog.findViewById(R.id.tv_reason);
        TextView tvAddress = dialog.findViewById(R.id.tv_address);
        TextView tvDate = dialog.findViewById(R.id.tv_created_date);
        TextView tvBeat = dialog.findViewById(R.id.tv_beat_desc);
        TextView tvBusiness = dialog.findViewById(R.id.tv_business);

        tvProposalId.setText("--" + item.getShopId());
        tvStoreId.setText(""+(item.getShopId() == 0 ? "1" : item.getShopId()));
        tvStoreName.setText(item.getStoreName());
        tvMobile.setText(item.getMobileNumber());

        String statusText = "PENDING";
        if (item.getStatus() == 1) statusText = "APPROVED";
        else if (item.getStatus() == 2) statusText = "REJECTED";
        tvStatus.setText(statusText);

        tvReason.setText("NA");
        tvAddress.setText(item.getAddress());

        tvDate.setText(item.getCreatedAt().substring(0, 10));
//        tvBeat.setText("Baba To Kranti Chowk");//current data not updated from the beakend
        tvBeat.setText(item.getBeat().getBeatNameFrom() + " To " + item.getBeat().getBeatNameTo());
        Log.d("showStoreDetailsPopup: ", "showStoreDetailsPopup: "+item.getBeatId());
        tvBusiness.setText(item.getShopKyc().getDocumentType());

        dialog.show();
    }
}
//        // 2. Setup ViewModel
//        viewModel = new ViewModelProvider(this).get(NewStoreListViewModel.class);
//
//        // 3. Setup Toolbar
//        tvTitle.setText("New Store Creation");
//        btnBack.setOnClickListener(v -> finish());
//
//        // 4. Setup Recycler
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        // 5. Date Picker Logic
//        tvDate.setOnClickListener(v -> showDatePicker());
//
//        // 6. FAB Logic
//        fabAdd.setOnClickListener(v -> {
//            // Opens the Create Store Form
//            // Ensure you have CreateStoreFormActivity created or change this line
//            Intent intent = new Intent(NewStoreActivity.this, StoreOtpVerificationActivity.class);
//            startActivity(intent);
//            Toast.makeText(this, "new screen open", Toast.LENGTH_SHORT).show();
//        });
//
//        // 7. Observers
//        viewModel.getSelectedDate().observe(this, date -> tvDate.setText(date));
//
//        viewModel.getStoreList().observe(this, list -> {
//            if (list != null) {
//                adapter = new NewStoreListAdapter(list);
//                recyclerView.setAdapter(adapter);
//            }
//        });
//    }
//
//    private void showDatePicker() {
//        Calendar cal = Calendar.getInstance();
//        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
//            viewModel.setDate(year, month, dayOfMonth);
//        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
//    }
//}