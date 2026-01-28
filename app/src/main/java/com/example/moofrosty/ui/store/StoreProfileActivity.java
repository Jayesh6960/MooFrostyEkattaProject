package com.example.moofrosty.ui.store;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.moofrosty.R;
import com.example.moofrosty.core.utils.NetworkUtil;
import com.example.moofrosty.data.local.SessionManager;
import com.example.moofrosty.data.model.AttendanceStatusResponse;
import com.example.moofrosty.data.model.Store;
import com.example.moofrosty.ui.enterstoreorders.ActionPointActivitys;
import com.example.moofrosty.ui.enterstoreorders.takeorder.TakeOrderActivity;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class StoreProfileActivity extends AppCompatActivity {


    private StoreProfileViewModel viewModel;
    private Store currentStore;
    private FusedLocationProviderClient fusedLocationClient;
    private ProgressDialog progressDialog;
    private SessionManager sessionManager;

    ImageView btnBack;
    TextView tvTitle;
    TabLayout tabLayout;
    ViewPager2 viewPager;
    Button btnEnterStore;

//    // Permissions Launcher
//    private final ActivityResultLauncher<String[]> locationPermissionRequest =
//            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
//                Boolean fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
//                Boolean coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);
//                if (fineLocationGranted != null && fineLocationGranted) {
//                    fetchLocationAndProceed();
//                } else {
//                    Toast.makeText(this, "Location permission is required to enter store", Toast.LENGTH_SHORT).show();
//                }
//            });
            // 1. Permission Launcher
            private final ActivityResultLauncher<String[]> locationPermissionRequest =
                    registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                        Boolean fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                        Boolean coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);

                        if (fineLocationGranted != null && fineLocationGranted) {
                            // Permission Granted, NOW check GPS
                            checkGpsAndProceed();
                        } else {
                            Toast.makeText(this, "Location permission is required to enter store", Toast.LENGTH_SHORT).show();
                        }
                    });

                // 2. GPS Resolution Launcher (Handles the "Turn on Location" popup result)
                private final ActivityResultLauncher<IntentSenderRequest> gpsResolutionLauncher =
                        registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), result -> {
                            if (result.getResultCode() == RESULT_OK) {
                                // User clicked "OK" to turn on GPS
                                fetchLocationAndProceed();
                            } else {
                                // User clicked "No Thanks"
                                Toast.makeText(this, "GPS is required to check-in.", Toast.LENGTH_SHORT).show();
                            }
                        });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_store_profile);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);
        if (getIntent().getExtras() != null) {
            currentStore = (Store) getIntent().getSerializableExtra("STORE_DATA");
        }

        // 2. Setup ViewModel
        viewModel = new ViewModelProvider(this).get(StoreProfileViewModel.class);
        if (currentStore != null) {
            viewModel.setStore(currentStore);
        }

        // 3. Init Views
         btnBack = findViewById(R.id.btn_back);
         tvTitle = findViewById(R.id.tv_toolbar_title);
         tabLayout = findViewById(R.id.tab_layout);
         viewPager = findViewById(R.id.view_pager);
         btnEnterStore = findViewById(R.id.btn_enter_store);
        sessionManager = new SessionManager(this);

        // 4. Window Insets (Padding for StatusBar/NavBar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_container), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        // 5. Setup Toolbar
//        if (currentStore != null) {
//            tvTitle.setText(currentStore.getStoreName() + " - HULI");
//        }
//        btnBack.setOnClickListener(v -> finish());
//
//        // 6. Setup Tabs & ViewPager
//        StorePagerAdapter adapter = new StorePagerAdapter(this);
//        viewPager.setAdapter(adapter);
//
//        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
//            if (position == 0) tab.setText("Store Profile");
//            else tab.setText("Order History");
//        }).attach();
//
//            // 7. Enter Store Button Logic
//            btnEnterStore.setOnClickListener(v -> {
//                Toast.makeText(this, "Entering Store: " + currentStore.getStoreName(), Toast.LENGTH_SHORT).show();
//                // Navigate to Order Taking screen or similar
//            });

        // 3. Init Views
        setupViews();

        // 4. Observers
        observeViewModel();
    }

    private void setupViews() {


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_container), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (currentStore != null) {
            tvTitle.setText(currentStore.getStoreName() + " - HULI");
        }
        btnBack.setOnClickListener(v -> finish());

        // Setup Tabs
        StorePagerAdapter adapter = new StorePagerAdapter(this);
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) tab.setText("Store Profile");
            else tab.setText("Order History");
        }).attach();

        // --- ENTER STORE CLICK ---
        btnEnterStore.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                locationPermissionRequest.launch(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                });
            } else {
            //    fetchLocationAndProceed();
                checkGpsAndProceed();
            }
        });
    }

    private void observeViewModel() {
        // Observe the API Resource Status
        viewModel.getCheckInStatus().observe(this, resource -> {
            switch (resource.status) {
                case LOADING:
                    progressDialog.setMessage("Checking In...");
                    if (!progressDialog.isShowing()) {
                        progressDialog.show();
                    }
                    break;

                case SUCCESS:
                    progressDialog.dismiss();
                    Toast.makeText(this, resource.data, Toast.LENGTH_SHORT).show(); // "Check-in added successfully"

                    // Navigate to next screen
                    sessionManager.saveShopId(currentStore.getShopId());
                    Intent intent = new Intent(this, TakeOrderActivity.class); // Replace with your actual activity
                    intent.putExtra("STORE_DATA", currentStore);
                    startActivity(intent);
                    break;

                case ERROR:
                    progressDialog.dismiss();
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        // Observe Geofence Alert
        viewModel.getGeofenceAlert().observe(this, data -> {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            showGeofenceDialog(data);
        });
    }

    private void checkGpsAndProceed() {
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).build();

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        // GPS IS ALREADY ON
        task.addOnSuccessListener(this, locationSettingsResponse -> {
            fetchLocationAndProceed();
        });

        // GPS IS OFF
        task.addOnFailureListener(this, e -> {
            if (e instanceof ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed by showing the user a dialog.
                ResolvableApiException resolvable = (ResolvableApiException) e;
                IntentSenderRequest intentSenderRequest = new IntentSenderRequest.Builder(resolvable.getResolution()).build();
                gpsResolutionLauncher.launch(intentSenderRequest);
            } else {
                Toast.makeText(this, "GPS is off and cannot be enabled automatically.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void fetchLocationAndProceed() {
        progressDialog.setMessage("Fetching Location...");
        progressDialog.show();

        // High accuracy request
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
                .setWaitForAccurateLocation(true)
                .setMaxUpdates(1)
                .build();

        fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                // Keep dialog open because ViewModel will either dismiss it (on error) or update it (on loading API)

                AttendanceStatusResponse attendanceStatusResponse = new AttendanceStatusResponse();

                if (locationResult != null && locationResult.getLastLocation() != null) {
                    Location loc = locationResult.getLastLocation();
                    // --- CHECK DATA ---
                    // 1. Attendance Check (Ideally get this from SharedPrefs/Session)
                   //  boolean isAttendanceMarked = sessionManager.isAttendanceMarked();
          //          boolean isAttendanceMarked = true; // Hardcoded for now per your request
                    boolean isAttendanceMarked = sessionManager.isAttendanceMarked();
                   // boolean isAttendanceMarked = attendanceStatusResponse.isPresent();
                    Log.d("ispresent","ispresent"+isAttendanceMarked);

                    if(isAttendanceMarked){
                        boolean isNetAvailable = NetworkUtil.isNetworkAvailable(StoreProfileActivity.this);
                        Log.d("ispresent","ispresent2 ");
                        // 3. Get Token
                        String token = sessionManager.getToken();
                        // --- PASS ALL TO VIEWMODEL ---
                        viewModel.onEnterStoreClicked(loc, isNetAvailable, isAttendanceMarked, token);
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(StoreProfileActivity.this, "User Not Marked Attendace", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(StoreProfileActivity.this, "Failed to get location. Ensure GPS is on.", Toast.LENGTH_SHORT).show();
                }
            }
        }, Looper.getMainLooper());
    }

//    private void showGeofenceDialog(StoreProfileViewModel.GeofenceData data) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this)
//        builder.setTitle("Geofencing Alert!")
//                .setMessage("Distance from Store: " + String.format("%.2f", data.distance) + "m\n\n" +
//                        "Current Lat: " + data.currentLat + "\n" +
//                        "Current Lng: " + data.currentLng + "\n\n" +
//                        "Please take order within 50m of store location.")
//                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
//                .setIcon(R.drawable.ic_error_icon_geofensing)
//                        .create();
//
//        builder.setOnShowListener(d -> {
//            builder.getButton(AlertDialog.BUTTON_POSITIVE)
//                    .setTextColor(getResources().getColor(R.color.Purple_Color));
//        });
//
//        builder.show();
//    }

    private void showGeofenceDialog(StoreProfileViewModel.GeofenceData data) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Geofencing Alert!")
                .setMessage(
                        "Distance from Store: " + String.format("%.2f", data.distance) + "m\n\n" +
                                "Current Lat: " + data.currentLat + "\n" +
                                "Current Lng: " + data.currentLng + "\n\n" +
                                "Please take order within 100m of store location."
                )
                .setPositiveButton("OK", null)
                .setIcon(R.drawable.ic_error_icon_geofensing)
                .create();
        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(getResources().getColor(R.color.Purple_Color));
        });
        dialog.show();
    }
}