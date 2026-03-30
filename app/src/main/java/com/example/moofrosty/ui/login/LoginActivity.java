package com.example.moofrosty.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.moofrosty.core.utils.NetworkUtil;
import com.example.moofrosty.data.local.SessionManager;
import com.example.moofrosty.data.model.LoginResponse;
import com.example.moofrosty.ui.dashboard.DashboardActivity;
import com.example.moofrosty.ui.enterstoreorders.ActionPointActivitys;
import com.example.moofrosty.R;
import com.example.moofrosty.ui.splash.BaseActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
//access to the Beat manager
public class LoginActivity extends BaseActivity {

    private TextInputEditText emailEditText, passwordEditText;
    private MaterialButton loginButton;
    private LoginViewModel loginViewModel;
    private SessionManager sessionManager;
    private View loadingLayout;
//New Code Updated Latest Date Updated 24-03-2026
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Init Components
        sessionManager = new SessionManager(this);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // 2. Bind Views
        emailEditText = findViewById(R.id.username);                   ///    1
        passwordEditText = findViewById(R.id.password_toggle);
        loginButton = findViewById(R.id.btn_login);
        loadingLayout = findViewById(R.id.loadingLayout);
//        // 3. Observe ViewModel (The "Neat" Part)
//        loginViewModel.getLoginResult().observe(this, resource -> {
//            if (resource != null) {
//                switch (resource.status) {
//                    case LOADING:
//                        setLoadingState(true);
//                        break;
//
//                    case SUCCESS:
//                        setLoadingState(false);
//                        Log.d("logindata","logindataget"+resource.data.getData());
//                        if (resource.data != null && resource.data.getData() != null) {
//                            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
//
//                            // Save Session
//                            String token = resource.data.getData().getToken();
//                            String name = resource.data.getData().getName(); // Assuming API returns name
//                            sessionManager.saveLoginSession(token, name != null ? name : "User");
//
//                            // Navigate
//                            navigateToDashboard();
//                        } else {
//                            Toast.makeText(this, "Login failed: No data received", Toast.LENGTH_SHORT).show();
//                        }
//                        break;
//
//                    case ERROR:
//                        Log.d("logindata","logindataget"+resource.message);
//                        setLoadingState(false);
//                        Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//        });
//
//        // 4. Click Listener
//        loginButton.setOnClickListener(v -> handleLogin());

//        loginViewModel.getLoginResult().observe(this, resource -> {
//            if (resource != null) {
//                switch (resource.status) {
//                    case LOADING:
//                        setLoadingState(true);
//                        break;
//
//                    case SUCCESS:
//                        setLoadingState(false);
//                        setLoadingState(false);
//                        LoginResponse resp = resource.data;
//                        if (resp != null) {
//
//                            String token = resp.getToken();
//                            String fullJson = new Gson().toJson(resp);
//                            sessionManager.saveLoginSession(token, fullJson);
//                            sessionManager.saveUserId(resp.getUser().getId());
//                            Log.d("sessonpass","checkpass"+token+" "+fullJson);
//
//                            boolean isPresent = false;
//                            boolean isPresentOut = false;
//                            if (resp.getAttendanceStatus() != null) {
//                                isPresent = resp.getAttendanceStatus().isPresent();
//                                isPresentOut = resp.getAttendanceStatus().isPresentOut(); // [HIGHLIGHT] Add this
//                            }
//                            Log.d("isPresent","isPresent"+isPresent);
//
//                            sessionManager.saveIsPresent(isPresent);
//                            sessionManager.saveIsPresentOut(isPresentOut);
//                       //     Log.d("sessonpassid","checkpass" + sessionManager.saveUserId(resp.getUser().getId()));
//
//                            navigateToDashboard();
//                        } else {
//                            Toast.makeText(this, "Login failed: No response data", Toast.LENGTH_SHORT).show();
//                        }
//                        break;
//
////                    case ERROR:
////                        setLoadingState(false);
////                        String errorMsg = resource.message != null ? resource.message : "";
////                        if (errorMsg.contains("401")
////                                || errorMsg.toLowerCase().contains("unauthorized")
////                                || errorMsg.toLowerCase().contains("invalid")) {
////                            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
////                        } else {
////                            Toast.makeText(this, "Login failed. Please try again", Toast.LENGTH_SHORT).show();
////                        }
////                        break;
////                    case ERROR:
////                        setLoadingState(false);
////
////                        String errorMsg = resource.message != null ? resource.message.toLowerCase() : "";
////
////                        if (errorMsg.contains("401")
////                                || errorMsg.contains("unauthorized")
////                                || errorMsg.contains("invalid")) {
////
////                            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
////
////                        } else if (errorMsg.contains("inactive") || errorMsg.contains("deactivated")) {
////
////                            Toast.makeText(this, "Your account has been deactivated. Please contact admin.", Toast.LENGTH_LONG).show();
////
////                            // 🔴 Force Logout
////                            logoutUser();
////
////                        } else {
////                            Toast.makeText(this, "User is inactive. Please contact admin.", Toast.LENGTH_SHORT).show();
////                        }
////                        break;
//                    case ERROR:
//                        setLoadingState(false);
//
//                        String errorMsg = resource.message != null ? resource.message.toLowerCase() : "";
//                        Log.d("errormassage", "onCreate: "+errorMsg);
//
//                        if (errorMsg.contains("401")
//                                || errorMsg.contains("unauthorized")
//                                || errorMsg.contains("invalid")) {
//
//                            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
//
//                        } else if (errorMsg.contains("") || errorMsg.contains("false")) {
//
//                            // ✅ Show message
//                            Toast.makeText(this, "Your account has been deactivated. Please contact admin.", Toast.LENGTH_LONG).show();
//
//                            // 🔴 ALWAYS logout
//                            logoutUser();
//
//                        } else {
//
//                            // 🔴 IMPORTANT: If backend is inconsistent, still logout for safety
//                            Toast.makeText(this, "User is inactive. Please contact admin.", Toast.LENGTH_SHORT).show();
//
//                            logoutUser(); // ✅ Add this line
//                        }
//                        break;
//                }
//            }
//        });

        loginViewModel.getLoginResult().observe(this, resource -> {
            if (resource != null) {

                switch (resource.status) {

                    case LOADING:
                        setLoadingState(true);
                        break;

                    case SUCCESS:
                        setLoadingState(false);

                        LoginResponse resp = resource.data;

                        if (resp != null && resp.getUser() != null) {

                            // 🔴 CHECK USER STATUS (MOST IMPORTANT)
                            String status = String.valueOf(resp.getUser().getStatus()); // "0" or "1"
                            Log.d("Status", "Login status: " + status);

                            if ("0".equals(status)) {
                                Toast.makeText(this,
                                        "Your account has been deactivated. Please contact admin.",
                                        Toast.LENGTH_LONG).show();

                                logoutUser();
                                return; //
                            }

                            // ✅ Continue Login (Only if Active)
                            String token = resp.getToken();
                            String fullJson = new Gson().toJson(resp);

                            sessionManager.saveLoginSession(token, fullJson);
                            sessionManager.saveUserId(resp.getUser().getId());

                            Log.d("session", "token: " + token);

                            boolean isPresent = false;
                            boolean isPresentOut = false;

                            if (resp.getAttendanceStatus() != null) {
                                isPresent = resp.getAttendanceStatus().isPresent();
                                isPresentOut = resp.getAttendanceStatus().isPresentOut();
                            }

                            sessionManager.saveIsPresent(isPresent);
                            sessionManager.saveIsPresentOut(isPresentOut);

                            navigateToDashboard();

                        } else {
                            Toast.makeText(this, "Login failed: No response data", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case ERROR:
                        setLoadingState(false);

                        String errorMsg = resource.message != null ? resource.message.toLowerCase() : "";
                        Log.d("errormessage", errorMsg);

                        if (errorMsg.contains("401")
                                || errorMsg.contains("unauthorized")
                                || errorMsg.contains("invalid")) {

                            // ❌ Wrong credentials
                            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();

                        } else if (errorMsg.contains("token") || errorMsg.contains("expired")) {

                            // 🔴 Session expired
                            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_LONG).show();
                            logoutUser();

                        } else {

                            // ✅ Generic error (NO deactivation here)
                            Toast.makeText(this, "User Deactivate  . Please Contact Admin.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });

        loginButton.setOnClickListener(v -> handleLogin());
    }

//private void logoutUser() {
//
//    sessionManager.logout(); // clear all data
//
//    Intent intent = new Intent(this, LoginActivity.class);
//    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//    startActivity(intent);
//    finishAffinity(); // ✅ ensures all activities closed
//}

    private void handleLogin() {
            String email = emailEditText.getText() != null ? emailEditText.getText().toString().trim() : "";/// 2
//        Log.d("Emailladdres", "Emailladdres"+email);
//        String mobile = emailEditText.getText() != null ? emailEditText.getText().toString().trim() : "";/// 2

        String password = passwordEditText.getText() != null ? passwordEditText.getText().toString().trim() : "";

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both Username and Password", Toast.LENGTH_SHORT).show();
            return;
        }
//
//        if (mobile.isEmpty() || password.isEmpty()) {
//            Toast.makeText(this, "Please enter both Username and Password", Toast.LENGTH_SHORT).show();
//            return;
//        }

        // Network Check
        if (NetworkUtil.isNetworkAvailable(this)) {
            loginViewModel.login(email, password);
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

//    private void setLoadingState(boolean isLoading) {
//        if (isLoading) {
//            loginButton.setEnabled(false);
//            loginButton.setText("Please Wait...");
//        } else {
//            loginButton.setEnabled(true);
//            loginButton.setText("Login");
//        }
//    }

    private void setLoadingState(boolean isLoading) {
        if (isLoading) {
            loadingLayout.setVisibility(View.VISIBLE);
            loginButton.setEnabled(false);
            emailEditText.setEnabled(false);
            passwordEditText.setEnabled(false);
            loginButton.setEnabled(false);
            loginButton.setText("Please Wait...");
        } else {
            loadingLayout.setVisibility(View.GONE);
            loginButton.setEnabled(true);
            emailEditText.setEnabled(true);
            passwordEditText.setEnabled(true);
            loginButton.setEnabled(true);
            loginButton.setText("Login");
        }
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class); // Or ActionPointActivitys.class
        startActivity(intent);
        finish();
    }



//        usernameEditText = findViewById(R.id.username);
//        passwordEditText = findViewById(R.id.password_toggle);
//        loginButton = findViewById(R.id.btn_login);
//
//        // Set click listener for the login button
//        loginButton.setOnClickListener(v -> handleLogin());
//    }
//    private void handleLogin() {
//        String username = usernameEditText.getText() != null ? usernameEditText.getText().toString().trim() : "";
//        String password = passwordEditText.getText() != null ? passwordEditText.getText().toString().trim() : "";
//
//        if (username.isEmpty() || password.isEmpty()) {
//            Toast.makeText(this, "Please enter both Username and Password", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (username.equals("admin") && password.equals("1234")) {
//            Log.d("LOGIN", "Login Successful for user: " + username);
//            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(LoginActivity.this, ActionPointActivitys.class);
//            intent.putExtra("username", username);
//            startActivity(intent);
//            finish();
//        } else {
//            Toast.makeText(this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
//        }
//    }
}