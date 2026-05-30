package com.example.moofrosty.ui.ATMSummary;

import static com.example.moofrosty.core.network.Resource.Status.ERROR;
import static com.example.moofrosty.core.network.Resource.Status.LOADING;
import static com.example.moofrosty.core.network.Resource.Status.SUCCESS;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moofrosty.R;
import com.google.android.material.appbar.AppBarLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AtmSummaryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StoreAdapter adapter;
    private AtmViewModel viewModel;

    private ProgressBar progressBar;
    private AppBarLayout appBarLayout;
    private androidx.appcompat.widget.Toolbar toolbar;
    private ImageView btn_back;

    // 🔹 Header Views
    private TextView tvTitle, salespersonName, currentDate;

    // 🔹 Summary
    private TextView totalTime, outlets, averageTime;

    // 🔹 Today Activity
    private TextView attendance, storeInTime, storeOutTime, totalSales, actual,totalOutlet,targeted;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        setContentView(R.layout.activity_atmactivity);

        // ✅ Status bar setup
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));

        WindowInsetsControllerCompat controller =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());

        if (controller != null) {
            controller.setAppearanceLightStatusBars(true);
        }

//        initViews();
//        setupToolbar();
//        setupRecycler();


//        viewModel = new ViewModelProvider(this).get(AtmViewModel.class);
//        observeData();





    }

    // 🔥 Initialize all views
//    private void initViews() {
//        progressBar = findViewById(R.id.progressBar);
//        appBarLayout = findViewById(R.id.app_bar_layout);
//        toolbar = findViewById(R.id.dashboard_toolbar);
//
//        btn_back = findViewById(R.id.btn_back);
//        tvTitle = findViewById(R.id.tv_title);
//
//        salespersonName = findViewById(R.id.Salespersonname);
//        currentDate = findViewById(R.id.currentdate);
//
//        totalTime = findViewById(R.id.totaltime);
//        actual = findViewById(R.id.actual);
//        totalOutlet = findViewById(R.id.totaloutlet);
//        targeted = findViewById(R.id.targeted);
//
//        averageTime = findViewById(R.id.Averagetime);
//
//        attendance = findViewById(R.id.punchinattendance);
//        storeInTime = findViewById(R.id.Storeintime);
//        storeOutTime = findViewById(R.id.Storeouttime);
//        totalSales = findViewById(R.id.totalsales);
//    }

    // 🔥 Toolbar setup
    private void setupToolbar() {
        setSupportActionBar(toolbar);

        tvTitle.setText("Today's Atm Summary");
        tvTitle.setGravity(Gravity.CENTER_HORIZONTAL);

        btn_back.setVisibility(View.VISIBLE);
        btn_back.setOnClickListener(v ->
                getOnBackPressedDispatcher().onBackPressed()
        );

        // Set current date
        String today = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(new Date());
        currentDate.setText(today);
    }
    // 🔥 Recycler setup
//    private void setupRecycler() {
//        recyclerView = findViewById(R.id.storeRecyclerView);
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setNestedScrollingEnabled(false);
//
//        adapter = new StoreAdapter(new ArrayList<>());
//        recyclerView.setAdapter(adapter);
//    }

    // 🔥 Observe API data
//    private void observeData() {
//
//        viewModel.getStoreList().observe(this, resource -> {
//
//            if (resource == null) return;
//
//            switch (resource.status) {
//
//                case LOADING:
//                    progressBar.setVisibility(View.VISIBLE);
//                    break;
//
////                case SUCCESS:
////                    progressBar.setVisibility(View.GONE);
////
////                    if (resource.data != null && !resource.data.isEmpty()) {
////
////                        // ✅ Update RecyclerView
////                        adapter.updateList(resource.data);vv
////
////                        // ✅ Update Header / Summary from first item
////                        StoreModel first = resource.data.get(0);
////
////                        totalSales.setText("Total Day Sales: ₹" + first.getDaySales());
////                        totalOutlet.setText("Total Lines: " + first.getNumberOfLines());
////                        storeInTime.setText("First Check-in: " + first.getInTime());
////                        storeOutTime.setText("Last Check-out: " + first.getOutTime());
////
////                        // Dummy summary (replace with real API if available)
////                        attendance.setText("Attendance: Present");
////                        totalTime.setText("5h 30m");
////                        outlets.setText(String.valueOf(resource.data.size()));
////                        averageTime.setText("30m");
////
////                    } else {
////                        Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
////                    }
////                    break;
//                case SUCCESS:
//                    progressBar.setVisibility(View.GONE);
//
//                    ArrayList<StoreModel> dummyList = new ArrayList<>();
//                dummyList.add(new StoreModel(
//        "Vaibhav Kulkarni",
//        "Current Date",
//        "Total Time",
//        "Outlets",
//        "Average Time",
//        "Pravin Super Market",
//        300,
//        1200,
//        5,
//        "09:30 AM",
//        "06:00 PM"
//));
//                    dummyList.add(new StoreModel(
//                            "Vaibhav Kulkarni",
//                            "Current Date",
//                            "Total Time",
//                            "Outlets",
//                            "Average Time",
//                            "Patil Super Market",
//                            300,
//                            1200,
//                            5,
//                            "09:30 AM",
//                            "06:00 PM"
//                    ));
//                    dummyList.add(new StoreModel(
//                            "Vaibhav Kulkarni",
//                            "Current Date",
//                            "Total Time",
//                            "Outlets",
//                            "Average Time",
//                            "Swami  Super Market",
//                            300,
//                            1200,
//                            5,
//                            "09:30 AM",
//                            "06:00 PM"
//                    ));
//                    dummyList.add(new StoreModel(
//                            "Vaibhav Kulkarni",
//                            "Current Date",
//                            "Total Time",
//                            "Outlets",
//                            "Average Time",
//                            "Swami Samarth  Super Market",
//                            300,
//                            1200,
//                            5,
//                            "09:30 AM",
//                            "06:00 PM"
//                    ));
//
//
////
////dummyList.add(new StoreModel(
////        "Amit Deshmukh",
////        "Current Date",
////        "Total Time",
////        "Outlets",
////        "Average Time",
////        "Ganesh Kirana Store",
////        400,
////        1500,
////        6,
////        "09:15 AM",
////        "06:30 PM"
////));
//
//                    adapter.updateList(dummyList);
//                    StoreModel first = dummyList.get(0);
//                    salespersonName.setText(first.getSalesperson());
//                    currentDate.setText(first.getCurrentdate());
//                    totalTime.setText(first.getTotaltime());
////                    outlets.setText(first.getOutlets(3,10));
//                    averageTime.setText(first.getAveragetime());
//                    totalSales.setText("₹"+String.valueOf(first.getDaySales()));
////                    totalOutlet.setText("Total Lines: " + first.getOutlets(3,5));
//                    storeInTime.setText(first.getInTime());
//                    storeOutTime.setText( first.getOutTime());
//
//
//                    actual.setText(String.valueOf(first.getActualOutlets()));
//                    targeted.setText("/" + first.getTotalOutlets());
//
//                    attendance.setText("Absent");
//                    totalTime.setText(first.getTotaltime());
////                    outlets.setText(String.valueOf(dummyList.size()));
//                    averageTime.setText(first.getAveragetime());
//
//                    break;
//
//                case ERROR:
//                    progressBar.setVisibility(View.GONE);
//
//                    Toast.makeText(this,
//                            resource.message != null ? resource.message : "Something went wrong",
//                            Toast.LENGTH_SHORT).show();
//                    break;
//            }
//        });
//    }

}