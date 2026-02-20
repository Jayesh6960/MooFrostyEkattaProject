package com.example.moofrosty.ui.splash;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.moofrosty.R;
import com.example.moofrosty.data.local.SessionManager;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.Task;
//Updated Date 27-01-2026
//After Start  Login Application we have to Turn On and the Turn Off // Permission remain
public class BaseActivity extends AppCompatActivity {

    protected SessionManager sessionManager;

    private static final int LOCATION_PERMISSION_REQUEST = 101;
    private static final int GPS_REQUEST = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(this);
    }

    @Override
    protected void onResume() {

        super.onResume();
        if (sessionManager.isLocationAndPermissionsEnabled()) {
            onLocationReady();
        } else {
            checkPermissionAndGps();
        }

//        checkPermissionAndGps();
    }

    // -------------------- STEP 1: CHECK PERMISSION --------------------

    private void checkPermissionAndGps() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Show Android system permission popup
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST
            );

        } else {
            // Permission granted → now check GPS
            checkGpsEnabled();
        }
    }

    // -------------------- STEP 2: HANDLE PERMISSION RESULT --------------------

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Permission allowed → check GPS
                checkGpsEnabled();

            } else {
                Toast.makeText(this,
                        "Location permission is required to continue",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    // -------------------- STEP 3: CHECK GPS & PROMPT SYSTEM POPUP --------------------

    private void checkGpsEnabled() {

        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        boolean isGpsOn = false;
        if (locationManager != null) {
            isGpsOn = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }

        if (!isGpsOn) {
            // Show SYSTEM GPS enable popup
            promptTurnOnGps();
        } else {
            // GPS already ON → continue app
            onLocationReady();
        }
    }

    private void showPermissionPopup() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Permissions Required")
                .setMessage("All permissions and GPS are mandatory. Please enable them to continue using Moofrosty.")
                .setCancelable(false)
                .setPositiveButton("Open Permissions", (dialogInterface, which) -> {
                    startActivity(new Intent(this, PermissionActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    finish();
                })
                .create();

        dialog.show();

        // Set Positive button color AFTER show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.bottom_nav_color));
    }

}
//package com.example.moofrosty.ui.splash;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.LocationManager;
//import android.os.Bundle;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import com.example.moofrosty.data.local.SessionManager;
//import com.google.android.gms.common.api.ResolvableApiException;
//import com.google.android.gms.location.*;
//
//import com.google.android.gms.tasks.Task;
//
//public abstract class BaseActivity extends AppCompatActivity {
//
//    protected SessionManager sessionManager;
//
//    private static final int LOCATION_PERMISSION_REQUEST = 201;
//    private static final int GPS_REQUEST = 202;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        sessionManager = new SessionManager(this);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        checkPermissionAndGps();
//    }
//
//    private void checkPermissionAndGps() {
//
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(
//                    this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    LOCATION_PERMISSION_REQUEST);
//        } else {
//            checkGpsEnabled();
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == LOCATION_PERMISSION_REQUEST) {
//            if (grantResults.length > 0
//                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                checkGpsEnabled();
//            } else {
//                Toast.makeText(this,
//                        "Location permission required",
//                        Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//    private void checkGpsEnabled() {
//
//        LocationManager lm =
//                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//        boolean gpsOn = lm != null &&
//                lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
//
//        if (!gpsOn) {
//            promptTurnOnGps();
//        } else {
//            onLocationReady();
//        }
//    }
//
//    private void promptTurnOnGps() {
//
//        LocationRequest request =
//                LocationRequest.create()
//                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//        LocationSettingsRequest.Builder builder =
//                new LocationSettingsRequest.Builder()
//                        .addLocationRequest(request)
//                        .setAlwaysShow(true);
//
//        SettingsClient client = LocationServices.getSettingsClient(this);
//        Task<LocationSettingsResponse> task =
//                client.checkLocationSettings(builder.build());
//
//        task.addOnSuccessListener(r -> onLocationReady());
//
//        task.addOnFailureListener(e -> {
//            if (e instanceof ResolvableApiException) {
//                try {
//                    ((ResolvableApiException) e)
//                            .startResolutionForResult(this, GPS_REQUEST);
//                } catch (Exception ignored) {
//                }
//            }
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode,
//                                    int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == GPS_REQUEST) {
//            if (resultCode == RESULT_OK) {
//                onLocationReady();
//            } else {
//                Toast.makeText(this,
//                        "GPS is required to continue",
//                        Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//    protected void onLocationReady() {
//        sessionManager.setLocationAndPermissionsEnabled(true);
//        // Child activity continues
//    }
//}
