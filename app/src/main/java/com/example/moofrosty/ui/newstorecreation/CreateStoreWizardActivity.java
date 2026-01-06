package com.example.moofrosty.ui.newstorecreation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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

    // Location vars
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_store_wizard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(CreateStoreViewModel.class);
        if(getIntent().hasExtra("MOBILE_NUMBER")) {
            viewModel.mobileNumber = getIntent().getStringExtra("MOBILE_NUMBER");
        }

        // Initialize Location Client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        checkLocationPermissionAndStart();

        ImageView btnBack = findViewById(R.id.btn_back);
        TextView tvTitle = findViewById(R.id.tv_toolbar_title);
        tvTitle.setText("Store Registration");
        btnBack.setOnClickListener(v -> handleBack());

        loadFragment(new Step1OwnerFragment());
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

    public void nextStep() {
        currentStep++;
        Fragment fragment = null;
        if(currentStep == 2) fragment = new Step2ShopFragment();
        else if(currentStep == 3) fragment = new Step3KycSelectionFragment();
        else if(currentStep == 4) fragment = new Step4DocUploadFragment();

        if(fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.wizard_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void handleBack() {
        if(currentStep > 1) {
            currentStep--;
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.wizard_container, fragment)
                .commit();
    }

    @Override public void onBackPressed() { handleBack(); }

}