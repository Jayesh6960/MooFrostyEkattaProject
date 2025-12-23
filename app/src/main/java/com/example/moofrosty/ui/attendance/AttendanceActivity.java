package com.example.moofrosty.ui.attendance;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moofrosty.R;
import com.google.android.material.button.MaterialButton;

public class AttendanceActivity extends AppCompatActivity {

    private AttendanceViewModel viewModel;
    private MaterialButton btnPunch;
    private TextView tvStatusMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_attendance);
        // 1. Init Views
        ImageView btnBack = findViewById(R.id.btn_back);
        TextView tvTitle = findViewById(R.id.tv_toolbar_title);
        RecyclerView recyclerView = findViewById(R.id.recycler_attendance_menu);
        btnPunch = findViewById(R.id.btn_punch);
        tvStatusMessage = findViewById(R.id.tv_status_message);
    //    Button btnMarkAttendance = findViewById(R.id.btn_mark_attendance);

        // 2. Window Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvTitle.setText("I-Teams");
        btnBack.setOnClickListener(v -> finish());

        // 4. Setup MVVM
        viewModel = new ViewModelProvider(this).get(AttendanceViewModel.class);

        // 5. Setup Grid (2 Columns)
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // 2 columns like screenshot

        viewModel.getMenuItems().observe(this, items -> {
            AttendanceMenuAdapter adapter = new AttendanceMenuAdapter(this, items);
            recyclerView.setAdapter(adapter);
        });

        viewModel.getPunchState().observe(this, state -> {
            updatePunchButtonUI(state);
        });

        btnPunch.setOnClickListener(v -> {
            viewModel.performPunch();
        });


//        // 6. Mark Attendance Button
//        btnMarkAttendance.setOnClickListener(v -> {
//            Toast.makeText(this, "Marking Attendance...", Toast.LENGTH_SHORT).show();
//            // Open Mark Attendance Activity later
//        });
    }

    private void updatePunchButtonUI(int state) {
        switch (state) {
            case 0: // Ready to Punch In
                btnPunch.setText("Punch In");
                btnPunch.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50"))); // Green
                btnPunch.setEnabled(true);
                tvStatusMessage.setText("Tap to mark your arrival");
                break;

            case 1: // Ready to Punch Out
                btnPunch.setText("Punch Out");
                btnPunch.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F44336"))); // Red
                btnPunch.setEnabled(true);
                tvStatusMessage.setText("Tap when leaving for the day");
                Toast.makeText(this, "Punch In Successful", Toast.LENGTH_SHORT).show();
                break;

            case 2: // Attendance Completed
                btnPunch.setText("Attendance Marked");
                btnPunch.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY)); // Grey
                btnPunch.setEnabled(false);
                tvStatusMessage.setText("You have completed attendance for today");
                Toast.makeText(this, "Punch Out Successful", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}