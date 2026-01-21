package com.example.moofrosty.ui.splash;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.moofrosty.data.local.SessionManager;
import com.example.moofrosty.ui.dashboard.DashboardActivity;
import com.example.moofrosty.ui.login.LoginActivity;
import com.example.moofrosty.R;

public class SplashActivity extends BaseActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sessionManager = new SessionManager(this);
        ImageView logo = findViewById(R.id.splashLogo);
        // Load and start fade-in animation
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logo.startAnimation(fadeIn);
        // Delay for 3 seconds, then navigate to MainActivity
        new Handler().postDelayed(() -> {
            if (sessionManager.isLoggedIn()) {
                // User is already logged in -> Go to Dashboard
                startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
            } else {
                // No session found -> Go to Login
                startActivity(new Intent(SplashActivity.this, PermissionActivity.class));
            }
            finish(); // Prevent user from returning to splash screen
        }, 3000);


    }
}