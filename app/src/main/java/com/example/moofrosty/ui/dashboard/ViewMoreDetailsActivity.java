package com.example.moofrosty.ui.dashboard;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moofrosty.R;
import com.example.moofrosty.data.model.DashboardItem;

import java.util.List;

public class ViewMoreDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_more_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView btnBack = findViewById(R.id.btn_back);
        TextView tvTitle = findViewById(R.id.tv_detail_title);
        RecyclerView recyclerView = findViewById(R.id.detail_recycler);

        // 1. Get the Data passed from DashboardActivity
        List<DashboardItem> dataList = null;
        String title = "";

        if (getIntent().getExtras() != null) {
            dataList = (List<DashboardItem>) getIntent().getSerializableExtra("DATA_LIST");
            title = getIntent().getStringExtra("TITLE");
        }

        // 2. Set the Title
        if (title != null && !title.isEmpty()) {
            tvTitle.setText(title + " Dashboard");
        } else {
            tvTitle.setText("Dashboard Details");
        }

        // 3. Setup Back Button
        btnBack.setOnClickListener(v -> finish());

        // 4. Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 5. Check if we actually received data
        if (dataList != null && !dataList.isEmpty()) {
            DetailAdapter adapter = new DetailAdapter(dataList);
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No Data Received", Toast.LENGTH_SHORT).show();
        }
    }
}