package com.example.moofrosty.ui.attendance.attendancecalender;

import android.os.Bundle;

import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.example.moofrosty.R;
import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.data.local.SessionManager;
import com.example.moofrosty.data.model.LeaveResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AttendanceCalendarActivity extends AppCompatActivity {

    private AttendanceCalendarViewModel viewModel;
    private CalendarView calendarView;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_attendance_calendar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
// 1. Init Views
        ImageView btnBack = findViewById(R.id.btn_back);
        calendarView = findViewById(R.id.attendanceCalender); // The Applandeo View

        sessionManager = new SessionManager(this);
        viewModel = new ViewModelProvider(this).get(AttendanceCalendarViewModel.class);

        btnBack.setOnClickListener(v -> finish());

        // 2. Fetch Data
        String token = sessionManager.getToken();
        if (!token.isEmpty()) {
            viewModel.fetchLeaves(token);
        } else {
            Toast.makeText(this, "Session Expired", Toast.LENGTH_SHORT).show();
        }

        // 3. Observe and Populate Calendar
        viewModel.getLeaveData().observe(this, resource -> {
            if (resource != null) {
                if (resource.status == Resource.Status.SUCCESS && resource.data != null) {
                    populateCalendarEvents(resource.data.getData());
                } else if (resource.status == Resource.Status.ERROR) {
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void populateCalendarEvents(List<LeaveResponse.UserLeaveData> leaveList) {
        List<EventDay> events = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        if (leaveList != null) {
            for (LeaveResponse.UserLeaveData leave : leaveList) {
                // Only show if Approved (Status == 2)
                if (leave.getLeaveStatus() == 2) {
                    try {
                        Date date = sdf.parse(leave.getStartDate());
                        if (date != null) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date);

                            int iconRes = 0;

                            // Map Leave Type to Drawable
                            switch (leave.getLeaveType()) {
                                case "1": // Medical Leave
                                    iconRes = R.drawable.ml; // Replace with R.drawable.ml
                                    break;
                                case "2": // Loss of Pay
                                    iconRes =R.drawable.lwp; // Replace with R.drawable.ic_lwp
                                    break;
                                case "3": // Casual Leave
                                    iconRes = R.drawable.cl; // Replace with R.drawable.cl
                                    break;
                                default:
                                    iconRes = android.R.drawable.star_on; // Default
                                    break;
                            }

                            // Add Event to List
                            if (iconRes != 0) {
                                events.add(new EventDay(calendar, iconRes));
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // Set all events to the calendar
        calendarView.setEvents(events);
    }
}