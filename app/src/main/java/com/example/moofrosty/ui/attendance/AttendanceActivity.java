package com.example.moofrosty.ui.attendance;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moofrosty.R;
import com.example.moofrosty.core.utils.NetworkUtil;
import com.example.moofrosty.data.local.SessionManager;
import com.example.moofrosty.data.model.AttendanceStatusResponse;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AttendanceActivity extends AppCompatActivity {

    private AttendanceViewModel viewModel;
    private SessionManager sessionManager;
    private MaterialButton btnPunch;
    private ProgressBar progressBar;
    private TextView tvStatusMessage;

    private AttendanceStatusResponse lastStatusData;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    private int currentPunchState = 0;

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

        // 2. Window Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvTitle.setText("Attendance");
        btnBack.setOnClickListener(v -> finish());

        sessionManager = new SessionManager(this);

        // 3. Setup RecyclerView (Menu)
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // 4. Setup ViewModel
        viewModel = new ViewModelProvider(this).get(AttendanceViewModel.class);

        // --- OBSERVERS ---

        // A. Menu Items (for the Grid)
        viewModel.getMenuItems().observe(this, items -> {
            AttendanceMenuAdapter adapter = new AttendanceMenuAdapter(this, items);
            recyclerView.setAdapter(adapter);
        });

        // B. Status Response (Updates the Button UI on load)
        viewModel.getStatusResponse().observe(this, resource -> {
            switch (resource.status) {
                case LOADING:
                    btnPunch.setText("Loading...");
                    btnPunch.setEnabled(false);
                    break;
                case SUCCESS:
                    if (resource.data != null) {
                        updateUIBasedOnStatus(resource.data);
                    }
                    break;
                case ERROR:
                    btnPunch.setText("Retry");
                    btnPunch.setEnabled(true);
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        // C. Punch Result (After clicking the button)
        viewModel.getPunchResult().observe(this, resource -> {
            switch (resource.status) {
                case LOADING:
                    btnPunch.setText("Processing...");
                    btnPunch.setEnabled(false);
                    break;
                case SUCCESS:
                    Toast.makeText(this, resource.data, Toast.LENGTH_SHORT).show();
                    // Reload status to refresh UI (switch from In -> Out)
                    viewModel.checkTodayStatus();
                    break;
                case ERROR:
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show();
                    // Reload status to reset button state
                    viewModel.checkTodayStatus();
                    break;
            }
        });

        // 5. Initial Load
        checkNetworkAndLoad();

        // 6. Button Click
        btnPunch.setOnClickListener(v -> handlePunchClick());
    }

    private void checkNetworkAndLoad() {
        if (NetworkUtil.isNetworkAvailable(this)) {
            viewModel.setToken(sessionManager.getToken());
            viewModel.checkTodayStatus();
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
            btnPunch.setEnabled(false);
            btnPunch.setText("No Internet");
        }
    }

    // Updates UI based on Simplified Logic (0=In, 1=Out, 2=Done, 4=Sun, 5=Holiday)
    private void updateUIBasedOnStatus(AttendanceStatusResponse data) {
        int state = viewModel.calculatePunchState(data);

        // Default Enable
        btnPunch.setEnabled(true);

        switch (state) {
            case 0: // Ready to Punch In
                btnPunch.setText("Punch In");
                btnPunch.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50"))); // Green
                tvStatusMessage.setText("Tap to mark your arrival");
                break;

            case 1: // Ready to Punch Out
                btnPunch.setText("Punch Out");
                btnPunch.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F44336"))); // Red
                tvStatusMessage.setText("Punched In at " + data.intime);
                break;

            case 2: // Attendance Completed
                btnPunch.setText("Attendance Marked");
                btnPunch.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                btnPunch.setEnabled(false);
                tvStatusMessage.setText("In: " + data.intime + " | Out: " + data.outtime);
                break;

            case 4: // Sunday
                btnPunch.setText("SUNDAY");
                btnPunch.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                btnPunch.setEnabled(false);
                tvStatusMessage.setText("Enjoy your holiday!");
                break;

            case 5: // Holiday or Leave
                String msg = data.isHoliday ? "Holiday" : "On Leave";
                btnPunch.setText(msg);
                btnPunch.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                btnPunch.setEnabled(false);
                tvStatusMessage.setText("Attendance not required today.");
                break;
        }
    }

    private void handlePunchClick() {
        // 1. Check Internet
        if (!NetworkUtil.isNetworkAvailable(this)) {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Check Permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        // 3. Check GPS Enabled
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please Enable GPS", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            return;
        }

        // 4. Fetch Location
        Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (loc == null) {
            // Fallback to Network Provider
            loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if (loc != null) {
            String coords = loc.getLatitude() + "," + loc.getLongitude();
            String addressStr = getAddressFromLocation(loc.getLatitude(), loc.getLongitude());

            // 5. Perform Punch
            viewModel.performPunch(addressStr, coords);
        } else {
            Toast.makeText(this, "Fetching Location... Try again in a moment.", Toast.LENGTH_SHORT).show();
            // In a real app, you would register a LocationListener here to wait for a fix
        }
    }

    private String getAddressFromLocation(double lat, double lng) {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Unknown Location";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                handlePunchClick(); // Retry click if permission granted
            } else {
                Toast.makeText(this, "Location permission is required to mark attendance", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

//        // 1. Init Views
//        ImageView btnBack = findViewById(R.id.btn_back);
//        TextView tvTitle = findViewById(R.id.tv_toolbar_title);
//        RecyclerView recyclerView = findViewById(R.id.recycler_attendance_menu);
//        btnPunch = findViewById(R.id.btn_punch);
//        tvStatusMessage = findViewById(R.id.tv_status_message);
//
//        // You might need these text views in XML to show times like your screenshot
//        // If not present, create them or ignore the setText lines below.
//        // tvPunchedInTime = findViewById(R.id.tv_punched_in_time);
//        // tvPunchedOutTime = findViewById(R.id.tv_punched_out_time);
//
//        // 2. Window Insets
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        tvTitle.setText("Attendance");
//        btnBack.setOnClickListener(v -> finish());
//
//        sessionManager = new SessionManager(this);
//
//        // 3. Setup Menu
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//        viewModel = new ViewModelProvider(this).get(AttendanceViewModel.class);
//
//        // 4. Observers
//        viewModel.getMenuItems().observe(this, items -> {
//            AttendanceMenuAdapter adapter = new AttendanceMenuAdapter(this, items);
//            recyclerView.setAdapter(adapter);
//        });
//
//        viewModel.getStatusResponse().observe(this, resource -> {
//            switch (resource.status) {
//                case LOADING:
//                    btnPunch.setText("Loading...");
//                    btnPunch.setEnabled(false);
//                    break;
//                case SUCCESS:
//                    lastStatusData = resource.data;
//                    updateUIBasedOnStatus(lastStatusData);
//                    break;
//                case ERROR:
//                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show();
//                    break;
//            }
//        });
//
//        viewModel.getPunchResult().observe(this, resource -> {
//            switch (resource.status) {
//                case LOADING:
//                    btnPunch.setText("Processing...");
//                    btnPunch.setEnabled(false);
//                    break;
//                case SUCCESS:
//                    Toast.makeText(this, resource.data, Toast.LENGTH_SHORT).show();
//                    // Refresh status to update button
//                    viewModel.checkTodayStatus();
//                    break;
//                case ERROR:
//                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show();
//                    // Revert UI to previous state if needed, or just reload
//                    viewModel.checkTodayStatus();
//                    break;
//            }
//        });
//
//        // 5. Load Data
//        checkNetworkAndLoad();
//
//        // 6. Click Listener
//        btnPunch.setOnClickListener(v -> handlePunchClick());
//    }
//
//    private void checkNetworkAndLoad() {
//        if (NetworkUtil.isNetworkAvailable(this)) {
//            viewModel.setToken(sessionManager.getToken());
//            viewModel.checkTodayStatus();
//        } else {
//            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
//            btnPunch.setEnabled(false);
//            btnPunch.setText("No Internet");
//        }
//    }
//
//    private void updateUIBasedOnStatus(AttendanceStatusResponse data) {
//        int state = viewModel.calculatePunchState(data);
//
//        // Reset button
//        btnPunch.setEnabled(true);
//
//        switch (state) {
//            case 0: // Ready to IN
//                btnPunch.setText("Punch In");
//                btnPunch.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50"))); // Green
//                tvStatusMessage.setText("Tap to mark your arrival");
//                break;
//
//            case 1: // Ready to OUT
//                btnPunch.setText("Punch Out");
//                btnPunch.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F44336"))); // Red
//                tvStatusMessage.setText("Punched In at " + data.intime);
//                break;
//
//            case 2: // DONE
//                btnPunch.setText("Attendance Marked");
//                btnPunch.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
//                btnPunch.setEnabled(false);
//                tvStatusMessage.setText("In: " + data.intime + " | Out: " + data.outtime);
//                break;
//
//            case 3: // WAIT 1 HOUR
//                btnPunch.setText("Wait 1 Hour");
//                btnPunch.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
//                btnPunch.setEnabled(false);
//                tvStatusMessage.setText("Punched In at " + data.intime + "\nWait 1 hr to punch out.");
//                break;
//
//            case 4: // SUNDAY
//                btnPunch.setText("SUNDAY");
//                btnPunch.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
//                btnPunch.setEnabled(false);
//                tvStatusMessage.setText("Enjoy your holiday!");
//                break;
//
//            case 5: // HOLIDAY/LEAVE
//                String msg = data.isHoliday ? "Holiday" : "On Leave";
//                btnPunch.setText(msg);
//                btnPunch.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
//                btnPunch.setEnabled(false);
//                tvStatusMessage.setText("Attendance not required.");
//                break;
//
//            case 6: // NOT BETWEEN TIME
//                btnPunch.setText("Out of Time");
//                btnPunch.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
//                btnPunch.setEnabled(false);
//                tvStatusMessage.setText("Punch allowed 5 AM to 11 PM");
//                break;
//        }
//    }
//
//    private void handlePunchClick() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
//            return;
//        }
//
//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            Toast.makeText(this, "Please Enable GPS", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Basic Location Fetching
//        Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        if (loc == null)
//            loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//
//        if (loc != null) {
//            String coords = loc.getLatitude() + "," + loc.getLongitude();
//            String addressStr = getAddressFromLocation(loc.getLatitude(), loc.getLongitude());
//
//            // Call ViewModel
//            viewModel.performPunch(addressStr, coords);
//        } else {
//            Toast.makeText(this, "Fetching Location...", Toast.LENGTH_SHORT).show();
//            // You might want to implement a LocationListener here for better accuracy
//            // For now, attempting punch with empty location if GPS fails immediately is risky,
//            // but requesting fresh location is complex without a listener.
//        }
//    }
//
//    private String getAddressFromLocation(double lat, double lng) {
//        try {
//            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
//            if (addresses != null && !addresses.isEmpty()) {
//                return addresses.get(0).getAddressLine(0);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return "Unknown Location";
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            handlePunchClick();
//        }
//    }
//}




        //     code /2
        // 1. Init Views
//        ImageView btnBack = findViewById(R.id.btn_back);
//        TextView tvTitle = findViewById(R.id.tv_toolbar_title);
//        RecyclerView recyclerView = findViewById(R.id.recycler_attendance_menu);
//        btnPunch = findViewById(R.id.btn_punch);
//        tvStatusMessage = findViewById(R.id.tv_status_message);
//        progressBar = new ProgressBar(this);
//        //    Button btnMarkAttendance = findViewById(R.id.btn_mark_attendance);
//
//        // 2. Window Insets
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        tvTitle.setText("Attendance");
//        btnBack.setOnClickListener(v -> finish());
//
//        sessionManager = new SessionManager(this);
//
//        // 3. Setup RecyclerView (Menu)
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//        // You can populate this adapter as you did before
//
//        // 4. Setup ViewModel
//        viewModel = new ViewModelProvider(this).get(AttendanceViewModel.class);
//
//        // --- A. MENU OBSERVER (This connects your Adapter) ---
//        viewModel.getMenuItems().observe(this, items -> {
//            AttendanceMenuAdapter adapter = new AttendanceMenuAdapter(this, items);
//            recyclerView.setAdapter(adapter);
//        });
//
//        // 5. Observers
//        observePunchState();
//        observePunchResult();
//
//        // 6. Initial Load
//        checkNetworkAndLoad();
//
//        // 7. Button Action
//        btnPunch.setOnClickListener(v -> handlePunchClick());
//    }
//
//    private void checkNetworkAndLoad() {
//        if (NetworkUtil.isNetworkAvailable(this)) {
//            viewModel.setToken(sessionManager.getToken());
//            viewModel.checkTodayStatus();
//        } else {
//            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
//            btnPunch.setEnabled(false);
//            btnPunch.setText("No Internet");
//        }
//    }
//
//    private void observePunchState() {
//        viewModel.getPunchState().observe(this, resource -> {
//            if (resource != null) {
//                switch (resource.status) {
//                    case LOADING:
//                        setLoading(true);
//                        break;
//                    case SUCCESS:
//                        setLoading(false);
//                        if (resource.data != null) {
//                            currentPunchState = resource.data;
//                            updatePunchButtonUI(currentPunchState);
//                        }
//                        break;
//                    case ERROR:
//                        setLoading(false);
//                        Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//        });
//    }
//
//    private void observePunchResult() {
//        viewModel.getPunchResult().observe(this, resource -> {
//            if (resource != null) {
//                switch (resource.status) {
//                    case LOADING:
//                        setLoading(true);
//                        break;
//                    case SUCCESS:
//                        setLoading(false);
//                        Toast.makeText(this, resource.data, Toast.LENGTH_SHORT).show();
//                        // Manually update state locally or recall API
//                        // If we were at state 0 (In), success means now we are at state 1 (Out)
//                        boolean wasPunchIn = (currentPunchState == 0);
//                        viewModel.refreshStateAfterSuccess(wasPunchIn);
//                        break;
//                    case ERROR:
//                        setLoading(false);
//                        Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//        });
//    }
//
//    private void handlePunchClick() {
//        if (!NetworkUtil.isNetworkAvailable(this)) {
//            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Example dummy location (Replace with real GPS logic later)
//        String myLocation = "Chhatrapati Sambhajinagar";
//        String myCoords = "19.876,75.343";
//
//        viewModel.performPunch(myLocation, myCoords);
//    }
//
//    private void updatePunchButtonUI(int state) {
//        btnPunch.setEnabled(true);
//        switch (state) {
//            case 0: // Ready to Punch In
//                btnPunch.setText("Punch In");
//                btnPunch.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50"))); // Green
//                tvStatusMessage.setText("Tap to mark your arrival");
//                break;
//
//            case 1: // Ready to Punch Out
//                btnPunch.setText("Punch Out");
//                btnPunch.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F44336"))); // Red
//                tvStatusMessage.setText("Tap when leaving for the day");
//                break;
//
//            case 2: // Attendance Completed
//                btnPunch.setText("Attendance Marked");
//                btnPunch.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
//                btnPunch.setEnabled(false);
//                tvStatusMessage.setText("You have completed attendance for today");
//                break;
//
//            case 3: // Holiday or Disabled
//                btnPunch.setText("Disabled / Holiday");
//                btnPunch.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
//                btnPunch.setEnabled(false);
//                tvStatusMessage.setText("Attendance not allowed at this time");
//                break;
//        }
//    }
//
//    private void setLoading(boolean isLoading) {
//        if (isLoading) {
//            btnPunch.setText("Please Wait...");
//            btnPunch.setEnabled(false);
//        } else {
//            // Text will be reset by updatePunchButtonUI based on state
//        }
//    }
//}




      //  code with no api belwo

//        tvTitle.setText("Attendance");
//        btnBack.setOnClickListener(v -> finish());
//
//        // 4. Setup MVVM
//        viewModel = new ViewModelProvider(this).get(AttendanceViewModel.class);
//
//        // 5. Setup Grid (2 Columns)
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // 2 columns like screenshot
//
//        viewModel.getMenuItems().observe(this, items -> {
//            AttendanceMenuAdapter adapter = new AttendanceMenuAdapter(this, items);
//            recyclerView.setAdapter(adapter);
//        });
//
//        viewModel.getPunchState().observe(this, state -> {
//            updatePunchButtonUI(state);
//        });
//
//        btnPunch.setOnClickListener(v -> {
//            viewModel.performPunch();
//        });
//
//
////        // 6. Mark Attendance Button
////        btnMarkAttendance.setOnClickListener(v -> {
////            Toast.makeText(this, "Marking Attendance...", Toast.LENGTH_SHORT).show();
////            // Open Mark Attendance Activity later
////        });
//    }
//
//    private void updatePunchButtonUI(int state) {
//        switch (state) {
//            case 0: // Ready to Punch In
//                btnPunch.setText("Punch In");
//                btnPunch.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50"))); // Green
//                btnPunch.setEnabled(true);
//                tvStatusMessage.setText("Tap to mark your arrival");
//                break;
//
//            case 1: // Ready to Punch Out
//                btnPunch.setText("Punch Out");
//                btnPunch.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F44336"))); // Red
//                btnPunch.setEnabled(true);
//                tvStatusMessage.setText("Tap when leaving for the day");
//                Toast.makeText(this, "Punch In Successful", Toast.LENGTH_SHORT).show();
//                break;
//
//            case 2: // Attendance Completed
//                btnPunch.setText("Attendance Marked");
//                btnPunch.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY)); // Grey
//                btnPunch.setEnabled(false);
//                tvStatusMessage.setText("You have completed attendance for today");
//                Toast.makeText(this, "Punch Out Successful", Toast.LENGTH_SHORT).show();
//                break;
//        }
//    }
//}