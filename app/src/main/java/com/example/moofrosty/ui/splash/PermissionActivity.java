package com.example.moofrosty.ui.splash;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.moofrosty.ui.login.LoginActivity;
import com.example.moofrosty.R;

public class PermissionActivity extends AppCompatActivity {

    private static final int LOCATION_REQ = 100;
    private static final int BACKGROUND_LOCATION_REQ = 101;
    private static final int CAMERA_REQ = 102;
    private static final int CONTACT_REQ = 103;

    private AppCompatButton btnAllow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        btnAllow = findViewById(R.id.btnAllow);
        btnAllow.setOnClickListener(v -> startPermissionFlow());
    }
//updated code  in the code  Date 26-01-2026
    //updated changes Color Permission data
    //Update  Request Button color(Allow and Open Permission )
    @Override
    protected void onResume() {
        super.onResume();

        // Check if GPS is ON
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null && !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please enable GPS to continue.", Toast.LENGTH_LONG).show();
            return;
        }

        if (allPermissionsGranted()) {
            goNext();
        }
    }

    // ================= CHECK ALL PERMISSIONS =================
    private boolean allPermissionsGranted() {
        boolean fineLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean backgroundLocation = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            backgroundLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }
        return fineLocation
                && backgroundLocation
                && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    // ================= PERMISSION FLOW =================
    private void startPermissionFlow() {
        checkLocationPermission();
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            checkBackgroundLocationPermission();
            return;
        }
        requestPermission("Location Permission Required",
                "Moofrosty needs location for attendance and live tracking.",
                Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_REQ);
    }

    private void checkBackgroundLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                checkCameraPermission();
                return;
            }
            requestPermission("Background Location Permission Required",
                    "Background location is needed for live tracking.",
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION, BACKGROUND_LOCATION_REQ);
        } else {
            checkCameraPermission();
        }
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            checkContactsPermission();
            return;
        }
        requestPermission("Camera Permission Required",
                "Camera is required to upload work images.",
                Manifest.permission.CAMERA, CAMERA_REQ);
    }

    private void checkContactsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            goNext();
            return;
        }
        requestPermission("Contacts Permission Required",
                "Contacts permission is needed to select phone numbers.",
                Manifest.permission.READ_CONTACTS, CONTACT_REQ);
    }

    // ================= COMMON REQUEST =================
    private void requestPermission(String title, String message, String permission, int code) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(title)
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("Allow", (d, w) ->
                            ActivityCompat.requestPermissions(this, new String[]{permission}, code))
                    .create();

            dialog.show();

            // Change the Positive ("Allow") button color AFTER dialog is shown
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(ContextCompat.getColor(this, R.color.bottom_nav_color));

        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, code);
        }
    }

    // ================= RESULT =================
    @Override
    public void onRequestPermissionsResult(int code, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(code, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startPermissionFlow();
        } else {
            showMandatoryDialog();
        }
    }

    private void showMandatoryDialog() {

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Permission Required")
                .setMessage("All permissions are mandatory to use Moofrosty.")
                .setCancelable(false)
                .setPositiveButton("Try Again", (d, w) -> startPermissionFlow())
                .create();

        dialog.show();

        // Change "Try Again" button color AFTER show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.bottom_nav_color));
    }
    private void goNext() {
        Toast.makeText(this, "All permissions granted", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
