package com.example.moofrosty.ui.newstorecreation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.moofrosty.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

public class CreateStoreWizardActivity extends AppCompatActivity {

    private CreateStoreViewModel viewModel;
    private int currentStep = 1;

    // Step UI
    private TextView tvBadge1, tvBadge2, tvBadge3;
    private View lineStep1, lineStep2;

    // Location vars
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    Toolbar toolbar ;
    ImageView btnBack;
    ImageView btnMenu;
    TextView tvTitle;
    TextView tvDate ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
        setContentView(R.layout.activity_create_store_wizard);
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        windowInsetsController.setAppearanceLightStatusBars(true);

        toolbar = findViewById(R.id.dashboard_toolbar);
        setSupportActionBar(toolbar);
        btnBack = findViewById(R.id.btn_back);
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.wizard_container), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), systemBars.bottom+16);
            return insets;
        });

        tvTitle.setText("Registration");
        btnBack.setVisibility(View.VISIBLE);
        btnMenu.setVisibility(View.GONE);
        btnBack.setOnClickListener(v -> onBackPressed());

        viewModel = new ViewModelProvider(this).get(CreateStoreViewModel.class);
        if(getIntent().hasExtra("MOBILE_NUMBER")) {
            viewModel.mobileNumber = getIntent().getStringExtra("MOBILE_NUMBER");
        }

        // Step Views
        tvBadge1 = findViewById(R.id.tvBadge1);
        tvBadge2 = findViewById(R.id.tvBadge2);
        tvBadge3 = findViewById(R.id.tvBadge3);
        lineStep1 = findViewById(R.id.lineStep1);
        lineStep2 = findViewById(R.id.lineStep2);

        // Initialize Location Client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        checkLocationPermissionAndStart();
        // Initial Fragment

        if (savedInstanceState == null) {
            loadStep(1);
        }
        updateStepUI(1);
        loadFragment(new Step1OwnerFragment());
    }

    private void updateStepUI(int step) {

        // Reset all badges
        tvBadge1.setBackgroundResource(R.drawable.grey_bg_storeregister);
        tvBadge2.setBackgroundResource(R.drawable.grey_bg_storeregister);
        tvBadge3.setBackgroundResource(R.drawable.grey_bg_storeregister);

        // Reset lines
        lineStep1.setBackgroundResource(R.color.grey);
        lineStep2.setBackgroundResource(R.color.grey);

        // Step 1 active
        if (step >= 1) {
            tvBadge1.setBackgroundResource(R.drawable.green_bg_storeregister);
        }

        // Step 2 active
        if (step >= 2) {
            tvBadge2.setBackgroundResource(R.drawable.green_bg_storeregister);
            lineStep1.setBackgroundResource(R.color.green);
        }

        // Step 3 active
        if (step >= 3) {
            tvBadge3.setBackgroundResource(R.drawable.green_bg_storeregister);
            lineStep2.setBackgroundResource(R.color.green);
        }
    }

    // --- LOCATION LOGIC START ---
    private final ActivityResultLauncher<String[]> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                Boolean fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                Boolean coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);
                if (fineLocationGranted != null && fineLocationGranted) {
                    startLocationUpdates();
                } else if (coarseLocationGranted != null && coarseLocationGranted) {
                    startLocationUpdates();
                } else {
                    Toast.makeText(this, "Location permission needed for tagging store", Toast.LENGTH_SHORT).show();
                }
            });

    private void checkLocationPermissionAndStart() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermissionRequest.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        } else {
            startLocationUpdates();
        }
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(2000)
                .setMaxUpdateDelayMillis(10000)
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) return;
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        // Update ViewModel with actual Lat/Long
                        String latLongStr = location.getLatitude() + "," + location.getLongitude();
                        viewModel.latLong = latLongStr;
                    }
                }
            }
        };

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

//    public void nextStep() {
//        Fragment fragment = null;
//
//        if (currentStep == 1) fragment = new Step2ShopFragment();
//        else if (currentStep == 2) fragment = new Step3KycSelectionFragment();
//        else if (currentStep == 3) fragment = new Step4DocUploadFragment();
//
//        if (fragment != null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.wizard_container, fragment)
//                    .addToBackStack(null)
//                    .commit();
//        }
//    }

    public void nextStep() {
        if (currentStep < 4) {
            currentStep++;
            loadStep(currentStep);
        }
    }

    private void loadStep(int step) {
        Fragment fragment = null;

        if (step == 1) fragment = new Step1OwnerFragment();
        else if (step == 2) fragment = new Step2ShopFragment();
        else if (step == 3) fragment = new Step3KycSelectionFragment();
        else if (step == 4) fragment = new Step4DocUploadFragment();

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.wizard_container, fragment)
                    .addToBackStack(String.valueOf(step))
                    .commit();
        }
    }

    // 🔥 FIXED BACK HANDLING
    @Override
    public void onBackPressed() {
        if (currentStep > 1) {
            currentStep--; // 🔥 THIS WAS MISSING
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

//    private void handleBack() {
//        if(currentStep > 1) {
//            currentStep--;
//            getSupportFragmentManager().popBackStack();
//        } else {
//            finish();
//        }
//    }

//    private void handleBack() {
//        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
//            getSupportFragmentManager().popBackStack();
//        } else {
//            finish();
//        }
//    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.wizard_container, fragment)
                .commit();
    }

//    @Override public void onBackPressed() { handleBack(); }

}