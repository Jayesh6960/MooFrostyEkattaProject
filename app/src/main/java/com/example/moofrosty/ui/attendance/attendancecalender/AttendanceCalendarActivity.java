package com.example.moofrosty.ui.attendance.attendancecalender;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class AttendanceCalendarActivity extends AppCompatActivity {

    private AttendanceCalendarViewModel viewModel;
    private CalendarView calendarView;
    private SessionManager sessionManager;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    ImageView btnBack;
    ImageView btnMenu;
    TextView tvTitle;
    TextView tvDate ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_attendance_calendar);
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        windowInsetsController.setAppearanceLightStatusBars(true);

        btnBack = findViewById(R.id.btn_back);
        toolbar = findViewById(R.id.dashboard_toolbar);
        setSupportActionBar(toolbar);
        btnMenu = findViewById(R.id.btn_menu);
        tvTitle = findViewById(R.id.tv_title);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.app_bar_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), systemBars.bottom+16);
            return insets;
        });

        tvTitle.setText("Attendance Calender");
        btnBack.setVisibility(View.VISIBLE);
        btnMenu.setVisibility(View.GONE);
        btnBack.setOnClickListener(v -> onBackPressed());

        calendarView = findViewById(R.id.attendanceCalender); // The Applandeo View
        progressBar = findViewById(R.id.progressBar);

        sessionManager = new SessionManager(this);
        viewModel = new ViewModelProvider(this).get(AttendanceCalendarViewModel.class);


        // 2. Fetch Data
        String token = sessionManager.getToken();
        if (!token.isEmpty()) {
            viewModel.fetchLeaves(token);
        } else {
            Toast.makeText(this, "Session Expired", Toast.LENGTH_SHORT).show();
        }

        // 3. Observe and Populate Calendar
//        viewModel.getLeaveData().observe(this, resource -> {
//            if (resource != null) {
//                if (resource.status == Resource.Status.SUCCESS && resource.data != null) {
//                //    populateCalendarEvents(resource.data.getData());
//                    // --- COMBINING ALL EVENTS HERE ---
//                    List<EventDay> allEvents = new ArrayList<>();
//
//                    // 1. Get Leaves
//                    if (resource.data.getData() != null) {
//                        allEvents.addAll(getLeaveEvents(resource.data.getData()));
//                    }
//
//                    // 2. Get Holidays
//                    if (resource.data.getHolidays() != null) {
//                        allEvents.addAll(getHolidayEvents(resource.data.getHolidays()));
//                    }
//
//                    // 3. Get Attendance
//                    if (resource.data.getUserAttendance() != null) {
//                        allEvents.addAll(getAttendanceEvents(resource.data.getUserAttendance()));
//                    }
//
//                    // Set everything to calendar
//                    calendarView.setEvents(allEvents);
//                } else if (resource.status == Resource.Status.ERROR) {
//                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//Updated Date 26-01-2026
        viewModel.getLeaveData().observe(this, resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case LOADING:
                        progressBar.setVisibility(View.VISIBLE);
                        break;
//                    case SUCCESS:
//                        progressBar.setVisibility(View.GONE);
//
//                        if (resource.data != null) {
//                            List<EventDay> allEvents = new ArrayList<>();
//                            Calendar today = Calendar.getInstance();
//                            boolean todayHandled = false;
//
//                            // 1. Leaves
//                            if (resource.data.getData() != null) {
//                                for (EventDay e : getLeaveEvents(resource.data.getData())) {
//                                    if (isSameDay(e.getCalendar(), today)) {
//                                        todayHandled = true;
//                                    }
//                                    allEvents.add(e);
//                                }
//                            }
//
//                            // 2. Holidays
//                            if (resource.data.getHolidays() != null) {
//                                for (EventDay e : getHolidayEvents(resource.data.getHolidays())) {
//                                    if (isSameDay(e.getCalendar(), today)) {
//                                        todayHandled = true;
//                                    }
//                                    allEvents.add(e);
//                                }
//                            }
//
//                            // 3. Attendance
//                            if (resource.data.getUserAttendance() != null) {
//                                for (EventDay e : getAttendanceEvents(resource.data.getUserAttendance())) {
//                                    if (isSameDay(e.getCalendar(), today)) {
//                                        todayHandled = true;
//                                    }
//                                    allEvents.add(e);
//                                }
//                            }
//
//                            // 🔥 Always overlay TODAY background on top
//                            allEvents.add(new EventDay(today, R.drawable.today_background));
//
//                            calendarView.setEvents(allEvents);
//                        }
//                        break;
                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);

                        if (resource.data != null) {
                            List<EventDay> allEvents = new ArrayList<>();
                            if (resource.data.getData() != null) {
                                allEvents.addAll(getLeaveEvents(resource.data.getData()));
                            }
                            if (resource.data.getHolidays() != null) {
                                allEvents.addAll(getHolidayEvents(resource.data.getHolidays()));
                            }
                            if (resource.data.getUserAttendance() != null) {
                                allEvents.addAll(getAttendanceEvents(resource.data.getUserAttendance()));
                            }
                            calendarView.setEvents(allEvents);
                        }
                        break;

                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


    }
   // Collection<? extends EventDay>
    private List<EventDay> getLeaveEvents(List<LeaveResponse.UserLeaveData> leaveList) {
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
                                    iconRes = R.drawable.cl; // Replace with R.drawable.ml
                                    break;
                                case "2": // Loss of Pay
                                    iconRes = R.drawable.ml; // Replace with R.drawable.ic_lwp
                                    break;
                                case "3": // Casual Leave
                                    iconRes = R.drawable.lwp; // Replace with R.drawable.cl
                                    break;
                                default:
                                    iconRes = android.R.drawable.star_on; // Default
                                    break;
                            }

                            // Add Event to List
                            // Use your Leave icon here
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
        return events;
    }
    private boolean isSameDay(Calendar c1, Calendar c2) {
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
                c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
    }

        // --- METHOD 2: Process Holidays ---
        private List<EventDay> getHolidayEvents(List<LeaveResponse.HolidayData> holidayList) {
            List<EventDay> events = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);//it will store  the data in the yy-mm/dd formate

            for (LeaveResponse.HolidayData holiday : holidayList) {
                try {
                    Date date = sdf.parse(holiday.getDate());
                    Log.d("date", "getHolidayEvents: "+date);
                    if (date != null) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        // Use your holiday icon here
                        events.add(new EventDay(calendar, R.drawable.holidayicon));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return events;
        }

        // --- METHOD 3: Process Attendance ---
        private List<EventDay> getAttendanceEvents(List<LeaveResponse.AttendanceData> attendanceList) {
            List<EventDay> events = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Log.d("sdf", "sdf"+sdf);
            // Set to prevent duplicate icons (since user punches IN and OUT on the same day)
            Set<String> processedDates = new HashSet<>();

            for (LeaveResponse.AttendanceData att : attendanceList) {
                if (!processedDates.contains(att.getDate())) {
                    try {
                        Date date = sdf.parse(att.getDate());
                        if (date != null) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date);
                            // Use your Present icon here
                            events.add(new EventDay(calendar, R.drawable.presenticon));
                            processedDates.add(att.getDate());
                        }
                    } catch (ParseException e) { e.printStackTrace(); }
                }
            }
            return events;
        }

}