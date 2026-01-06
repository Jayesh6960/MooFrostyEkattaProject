package com.example.moofrosty.ui.newstorecreation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.moofrosty.R;
import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.core.utils.NetworkUtil;
import com.example.moofrosty.data.local.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class StoreOtpVerificationActivity extends AppCompatActivity {

    private StoreVerificationViewModel viewModel;
    private SessionManager sessionManager;

    private TextInputEditText etMobile, etOtp;
    private TextInputLayout layoutOtp;
    private MaterialButton btnSendVerification, btnSubmitOtp;
    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_store_otp_verification);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // 1. Init
        // 1. Init
        viewModel = new ViewModelProvider(this).get(StoreVerificationViewModel.class);
        sessionManager = new SessionManager(this);

        ImageView btnBack = findViewById(R.id.btn_back);
        TextView tvTitle = findViewById(R.id.tv_toolbar_title);
        etMobile = findViewById(R.id.et_mobile);
        etOtp = findViewById(R.id.et_otp);
        layoutOtp = findViewById(R.id.layout_otp);
        btnSendVerification = findViewById(R.id.btn_send_verification);
        btnSubmitOtp = findViewById(R.id.btn_submit_otp);
        tvStatus = findViewById(R.id.tv_status);

        tvTitle.setText("Registration");
        btnBack.setOnClickListener(v -> finish());

        // 2. Click Listener: Send Code
        btnSendVerification.setOnClickListener(v -> {
            String mobile = etMobile.getText().toString().trim();
            if (mobile.length() < 10) {
                etMobile.setError("Enter valid mobile number");
                return;
            }

            if (NetworkUtil.isNetworkAvailable(this)) {
                String token = sessionManager.getToken();
                if(!token.isEmpty()) {
                    viewModel.verifyNumber(token, mobile);
                } else {
                    Toast.makeText(this, "Session Expired", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        });

        // 3. Click Listener: Submit OTP
        btnSubmitOtp.setOnClickListener(v -> {
            String otp = etOtp.getText().toString().trim();
            if (otp.length() < 4) {
                etOtp.setError("Enter Valid OTP");
                return;
            }

            // --- SUCCESS LOGIC ---
            Toast.makeText(this, "OTP Verified Successfully!", Toast.LENGTH_SHORT).show();

            // Get the verified mobile number
            String verifiedMobile = etMobile.getText().toString().trim();

            // Navigate to CreateStoreWizardActivity and pass the number
            Intent intent = new Intent(StoreOtpVerificationActivity.this, CreateStoreWizardActivity.class);
            intent.putExtra("MOBILE_NUMBER", verifiedMobile);
            startActivity(intent);
            finish(); // Close verification screen so user can't go back to it
        });

        // 4. Observe API Result
        viewModel.getCheckResult().observe(this, resource -> {
            if (resource != null) {
                if (resource.status == Resource.Status.LOADING) {
                    btnSendVerification.setEnabled(false);
                    btnSendVerification.setText("Checking...");
                } else if (resource.status == Resource.Status.SUCCESS) {
                    btnSendVerification.setEnabled(true);
                    btnSendVerification.setText("Send Verification Code");

                    if (resource.data != null) {
                        if (resource.data.isExists()) {
                            String storeName = (resource.data.getData() != null) ? resource.data.getData().getStoreName() : "Unknown Store";
                            Toast.makeText(this, "Number exists for store: " + storeName, Toast.LENGTH_LONG).show();

                            tvStatus.setText("Mobile number already registered for " + storeName);
                            tvStatus.setTextColor(getColor(android.R.color.holo_red_dark));
                            tvStatus.setVisibility(View.VISIBLE);
                        } else {
                            showOtpScreen();
                        }
                    }
                } else {
                    btnSendVerification.setEnabled(true);
                    btnSendVerification.setText("Send Verification Code");
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showOtpScreen() {
        btnSendVerification.setVisibility(View.GONE);
        etMobile.setEnabled(false); // Lock the number so it can't be changed
        layoutOtp.setVisibility(View.VISIBLE);
        btnSubmitOtp.setVisibility(View.VISIBLE);

        tvStatus.setText("Number Available. Please enter OTP to verify.");
        tvStatus.setTextColor(getColor(R.color.Purple_Color));
        tvStatus.setVisibility(View.VISIBLE);

        etOtp.requestFocus();
    }
}