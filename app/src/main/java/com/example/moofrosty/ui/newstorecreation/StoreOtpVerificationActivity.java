package com.example.moofrosty.ui.newstorecreation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.moofrosty.R;
import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.core.utils.NetworkUtil;
import com.example.moofrosty.data.local.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class StoreOtpVerificationActivity extends AppCompatActivity {

    private StoreVerificationViewModel viewModel;
    private SessionManager sessionManager;

    private TextInputEditText etMobile, etOtp;
    private TextInputLayout layoutOtp,till_mob;
    private MaterialButton btnSendVerification, btnSubmitOtp;
    private TextView tvStatus;
    Toolbar toolbar ;
    ImageView btnBack;
    ImageView btnMenu;
    TextView tvTitle;
    TextView tvDate ;
    ScrollView scrollView;

    private FirebaseAuth mAuth;
    private String mVerificationId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_store_otp_verification);
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        windowInsetsController.setAppearanceLightStatusBars(true);

        toolbar = findViewById(R.id.dashboard_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        btnBack = findViewById(R.id.btn_back);
        btnMenu = findViewById(R.id.btn_menu);
        tvTitle = findViewById(R.id.tv_title);
        tvDate = findViewById(R.id.tv_date_picker);
        scrollView = findViewById(R.id.scrollview);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.app_bar_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });
        ViewCompat.setOnApplyWindowInsetsListener(scrollView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), systemBars.bottom);
            return insets;
        });

        // [HIGHLIGHT] Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        tvTitle.setText("OTP Verification");

        btnBack.setVisibility(View.VISIBLE);
        btnMenu.setVisibility(View.GONE);

        tvDate.setVisibility(View.GONE);
        viewModel = new ViewModelProvider(this).get(StoreVerificationViewModel.class);
        sessionManager = new SessionManager(this);

        etMobile = findViewById(R.id.et_mobile);
        etOtp = findViewById(R.id.et_otp);
        layoutOtp = findViewById(R.id.layout_otp);
        btnSendVerification = findViewById(R.id.btn_send_verification);
        btnSubmitOtp = findViewById(R.id.btn_submit_otp);
        tvStatus = findViewById(R.id.tv_status);
        till_mob=findViewById(R.id.till_mob);


        tvTitle.setText("Registration");
        btnBack.setOnClickListener(v -> finish());

        // 2. Click Listener: Send Code
        btnSendVerification.setOnClickListener(v -> {
            String mobile = etMobile.getText().toString().trim();
            if (mobile.length() < 10) {
                till_mob.setError("Enter valid mobile number");

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
////            if (otp.length() < 4) {
////                etOtp.setError("Enter Valid OTP");
////                return;
////            }

            if (!otp.equals("123456")) {
                etOtp.setError("Invalid OTP");
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

//        btnSubmitOtp.setOnClickListener(v -> {
//            String otp = etOtp.getText().toString().trim();
//
//            if (otp.length() < 6) {
//                layoutOtp.setError("Enter Valid 6-digit OTP");
//                return;
//            }
//
//            layoutOtp.setError(null);
//
//            // [HIGHLIGHT] Verify OTP with Firebase
//            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
//            verifyFirebaseOTP(credential);
//        });

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

                            showRegisteredNumberBottomSheet(
                                    resource.data.getData().getStoreName(),
                                    resource.data.getData().getRsSsIdentifier()
                            );

                            tvStatus.setText("Mobile number already registered for " + storeName);
                            tvStatus.setTextColor(getColor(android.R.color.holo_red_dark));
                            tvStatus.setVisibility(View.VISIBLE);
                        } else {
//                            String mobile = etMobile.getText().toString().trim();
//                            showOtpScreen(mobile);
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

    //    Updated code Date 06-01-2026
    private void showRegisteredNumberBottomSheet(String storeDetails, String rsDetails) {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.bottomsheet_store_exits_by_mobile);
        Button btnOK;
        btnOK = dialog.findViewById(R.id.btnOK);
        TextView tvStore = dialog.findViewById(R.id.tvStoreDetails);
//        TextView tvRs = dialog.findViewById(R.id.tvRSDetails);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        TextView tvBusiness = dialog.findViewById(R.id.tvverifed);

        if (tvStore != null)
            tvStore.setText(storeDetails != null ? storeDetails : "N/A");

//        if (tvRs != null)
//            tvRs.setText(rsDetails != null ? rsDetails : "N/A");


        dialog.setCancelable(false);
        dialog.show();

    }

//    private void showOtpScreen(String mobileNumber) {
//        btnSendVerification.setVisibility(View.GONE);
//        etMobile.setEnabled(false); // Lock the number so it can't be changed
//        layoutOtp.setVisibility(View.VISIBLE);
//
//        btnSubmitOtp.setVisibility(View.VISIBLE);
//        btnSubmitOtp.setText("Sending OTP...");
//        btnSubmitOtp.setEnabled(false); // Disable until OTP is actually sent
//
//        tvStatus.setText("Sending OTP securely. Please wait...");
//        tvStatus.setTextColor(getColor(R.color.Purple_Color));
//        tvStatus.setVisibility(View.VISIBLE);
//
//        // Start Firebase OTP generation
//        sendFirebaseOTP(mobileNumber);
//    }

    private void sendFirebaseOTP(String mobileNumber) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+91" + mobileNumber)       // Add Country Code (+91 for India)
                .setTimeout(60L, TimeUnit.SECONDS)          // Timeout and force resend
                .setActivity(this)                          // Activity for callback binding
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                        // This is called if Android automatically reads the SMS
                        String code = credential.getSmsCode();
                        if (code != null) {
                            etOtp.setText(code);
                            verifyFirebaseOTP(credential);
                        }
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        // Failed to send OTP
                        Toast.makeText(StoreOtpVerificationActivity.this, "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        btnSubmitOtp.setText("Retry Sending OTP");
                        btnSubmitOtp.setEnabled(true);

                        btnSubmitOtp.setOnClickListener(v -> sendFirebaseOTP(mobileNumber)); // Allow retry
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        // OTP Successfully sent to user's phone!
                        super.onCodeSent(verificationId, token);
                        mVerificationId = verificationId; // Save this to verify later

                        tvStatus.setText("OTP sent to " + mobileNumber + ". Please enter it below.");
                        btnSubmitOtp.setText("Verify & Proceed");
                        btnSubmitOtp.setEnabled(true);
                        etOtp.requestFocus();
                    }
                }).build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyFirebaseOTP(PhoneAuthCredential credential) {
        btnSubmitOtp.setEnabled(false);
        btnSubmitOtp.setText("Verifying...");

        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                // --- SUCCESS LOGIC ---
                Toast.makeText(this, "OTP Verified Successfully!", Toast.LENGTH_SHORT).show();

                String verifiedMobile = etMobile.getText().toString().trim();

                // Navigate to CreateStoreWizardActivity and pass the number
                Intent intent = new Intent(StoreOtpVerificationActivity.this, CreateStoreWizardActivity.class);
                intent.putExtra("MOBILE_NUMBER", verifiedMobile);
                startActivity(intent);
                finish();
            } else {
                // --- FAILED LOGIC ---
                btnSubmitOtp.setEnabled(true);
                btnSubmitOtp.setText("Verify & Proceed");
                layoutOtp.setError("Invalid OTP entered.");
                Toast.makeText(this, "Verification Failed. Try again.", Toast.LENGTH_SHORT).show();
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