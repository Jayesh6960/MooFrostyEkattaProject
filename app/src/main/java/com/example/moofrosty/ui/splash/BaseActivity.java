package com.example.moofrosty.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.moofrosty.R;
import com.example.moofrosty.data.local.SessionManager;
//Updateed BAse activity and update the code accordinly
public class BaseActivity extends AppCompatActivity {

    protected SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(this);
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (!sessionManager.isLocationAndPermissionsEnabled()) {
            showPermissionPopup();
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
