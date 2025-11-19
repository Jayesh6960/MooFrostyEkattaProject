package com.example.moofrosty;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class ActionPointActivitys extends AppCompatActivity {
    private BarChart barChartSales, barChartAssortment;
    private Spinner spinnerPlg;
    //private Switch switchMode;
    private AppCompatButton btnQps, btnBtpr, btnNext;
    private TextView tvEsp, tvTa, tvTlsd;
    //  private ImageButton icCar,iconscan,iconpower;
    LinearLayout headerLayout;
//        TextInputLayout search_bar_layout;
//        LinearLayout toolbarlayout;
//        ImageView header_back_arrow;
//        TextView header_title;
    private TextInputLayout searchBarLayout;
    private LinearLayout toolbarLayout;
    private ImageView headerBackArrow;
    private TextView headerTitle;
    private ImageButton iconCart, iconScan, iconPower;
    AppBarLayout appbarlayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_action_point_activitys);
        appbarlayout = findViewById(R.id.app_bar_layout);
        ViewCompat.setOnApplyWindowInsetsListener(appbarlayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });
        initToolbar();
        initViews();

//            // Get views from included layout
//            search_bar_layout = toolbarRoot.findViewById(R.id.search_bar_layout);
//            iconscan = toolbarRoot.findViewById(R.id.icon_scan);
//            iconpower = toolbarRoot.findViewById(R.id.icon_power);
//            icCar = toolbarRoot.findViewById(R.id.icon_cart);
//
//            toolbarlayout = toolbarRoot.findViewById(R.id.toolbarlayout);
//            header_back_arrow = toolbarRoot.findViewById(R.id.header_back_arrow);
//            header_title = toolbarRoot.findViewById(R.id.header_title);
//
//// Hide search UI
////            search_bar_layout.setVisibility(View.GONE);
////            iconscan.setVisibility(View.GONE);
////            icCar.setVisibility(View.GONE);
//
//// Show title bar
//         //   toolbarlayout.setVisibility(View.VISIBLE);
//            header_title.setText("Amul IceCream Club");
//
//// Back arrow click
//            header_back_arrow.setOnClickListener(v -> finish());

//            // === Initialize all Views ===
//            barChartSales = findViewById(R.id.barChartSales);
//            barChartAssortment = findViewById(R.id.barChartAssortment);
//            spinnerPlg = findViewById(R.id.spinner_plg);
//            //  switchMode = findViewById(R.id.switch_mode);
//            btnQps = findViewById(R.id.btn_qps);
//            btnBtpr = findViewById(R.id.btn_btpr);
//            btnNext = findViewById(R.id.btn_next);
//            tvEsp = findViewById(R.id.tv_esp);
//            tvTa = findViewById(R.id.tv_ta);
//            tvTlsd = findViewById(R.id.tv_tlsd);
        //updated made in the cark activity
        setupSpinner();
        setupBarChart(barChartSales, "Sales Performance");
        setupBarChart(barChartAssortment, "Assortment Performance");
        // === Button Click Listeners ===
        btnQps.setOnClickListener(v -> showMessage("QPS button clicked"));
        btnBtpr.setOnClickListener(v -> showMessage("BTPR button clicked"));

        // ✅ Next button now navigates to DrawableActivity
        btnNext.setOnClickListener(v -> {
            Intent intent = new Intent(ActionPointActivitys.this, TakeOrderActivity.class);
            startActivity(intent);
            finish(); // optional — removes this screen from back stack
        });
        // === Switch Toggle Listener ===
        //        switchMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
        //            if (isChecked) showMessage("Switch ON");
        //            else showMessage("Switch OFF");
        //        });
        iconCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActionPointActivitys.this, CartActivity.class);
                startActivity(intent);
            }
        });
    }

//    private void setupSpinner() {
//        ArrayAdapter<String> plgAdapter = new ArrayAdapter<>(
//                this,
//                android.R.layout.simple_spinner_dropdown_item,
//                new String[]{"PLG 1", "PLG 2", "PLG 3"}
//        );
//        spinnerPlg.setAdapter(plgAdapter);
//    }
//
//    private void setupBarChart(BarChart chart, String label) {
//        ArrayList<BarEntry> entries = new ArrayList<>();
//        entries.add(new BarEntry(1, 45));
//        entries.add(new BarEntry(2, 67));
//        entries.add(new BarEntry(3, 23));
//        BarDataSet dataSet = new BarDataSet(entries, label);
//        dataSet.setColor(getColor(R.color.blue));
//        dataSet.setValueTextColor(getColor(android.R.color.black));
//        dataSet.setValueTextSize(12f);
//        BarData data = new BarData(dataSet);
//        data.setBarWidth(0.5f);
//        chart.setData(data);
//        chart.getDescription().setEnabled(false);
//        chart.getAxisRight().setEnabled(false);
//        chart.getAxisLeft().setDrawGridLines(false);
//        chart.getXAxis().setDrawGridLines(false);
//        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
//        chart.getXAxis().setGranularity(1f);
//        chart.getXAxis().setLabelCount(entries.size());
//        chart.getLegend().setTextColor(getColor(android.R.color.black));
//        chart.animateY(1000);
//        chart.invalidate();
//    }
//
//    private void showMessage(String msg) {
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
//    }

    private void initToolbar() {
        View toolbarRoot = findViewById(R.id.laxmilayout);
        searchBarLayout = toolbarRoot.findViewById(R.id.search_bar_layout);
        iconScan = toolbarRoot.findViewById(R.id.icon_scan);
        iconPower = toolbarRoot.findViewById(R.id.icon_power);
        iconCart = toolbarRoot.findViewById(R.id.icon_cart);
        toolbarLayout = toolbarRoot.findViewById(R.id.toolbarlayout);
        headerBackArrow = toolbarRoot.findViewById(R.id.header_back_arrow);
        headerTitle = toolbarRoot.findViewById(R.id.header_title);
        // Use title bar (hide search bar)
        searchBarLayout.setVisibility(View.GONE);
        iconScan.setVisibility(View.GONE);
        iconCart.setVisibility(View.GONE);
        toolbarLayout.setVisibility(View.VISIBLE);
        headerTitle.setText("Amul IceCream Club");

        headerBackArrow.setOnClickListener(v -> showExitDialog());
    }
    @Override
    public void onBackPressed() {
        showExitDialog();
    }
    private void initViews() {
        barChartSales = findViewById(R.id.barChartSales);
        barChartAssortment = findViewById(R.id.barChartAssortment);
        spinnerPlg = findViewById(R.id.spinner_plg);

        btnQps = findViewById(R.id.btn_qps);
        btnBtpr = findViewById(R.id.btn_btpr);
        btnNext = findViewById(R.id.btn_next);

        tvEsp = findViewById(R.id.tv_esp);
        tvTa = findViewById(R.id.tv_ta);
        tvTlsd = findViewById(R.id.tv_tlsd);
    }

    private void setupSpinner() {
        ArrayAdapter<String> plgAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"PLG 1", "PLG 2", "PLG 3"}
        );
        spinnerPlg.setAdapter(plgAdapter);
    }
    private void setupBarChart(BarChart chart, String label) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, 45));
        entries.add(new BarEntry(2, 67));
        entries.add(new BarEntry(3, 23));
        BarDataSet dataSet = new BarDataSet(entries, label);
        dataSet.setColor(getColor(R.color.blue));
        dataSet.setValueTextColor(getColor(android.R.color.black));
        dataSet.setValueTextSize(12f);

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.5f);

        chart.setData(data);
        chart.getDescription().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setDrawGridLines(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        chart.animateY(1000);
        chart.invalidate();
    }
    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private void showExitDialog() {
        AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this)
                .setTitle("Exit App")
                .setMessage("Do you want to exit?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    finish();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(getResources().getColor(R.color.colorPrimary));// color  used to set  the text color of the button
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(getResources().getColor(R.color.colorPrimary));
    }
}