package com.example.moofrosty.ui.attendance.support;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.moofrosty.R;

import java.util.Calendar;

public class SupportAttendance extends AppCompatActivity {

    private TextView supportEmail, supportContact, salesContact;
    private CardView cardMailSupport, cardCallSupport;
    //changes form the whole card to  only id  connected
    private Toolbar toolbar;
    ImageView btnBack;
    ImageView btnMenu;
    TextView tvTitle;
    TextView tvDate;

    // -------- SCENARIO CONTROL --------
    // true = Enable timing restriction
    // false = Disable restriction (allow call anytime)
    private boolean ENABLE_SUPPORT_TIME_RESTRICTION = true;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_support_attendace);

        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        windowInsetsController.setAppearanceLightStatusBars(true);

        btnBack = findViewById(R.id.btn_back);
        toolbar = findViewById(R.id.dashboard_toolbar);
        setSupportActionBar(toolbar);

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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), systemBars.bottom + 16);
            return insets;
        });

        tvTitle.setText("Support");
        btnBack.setVisibility(View.VISIBLE);
        btnMenu.setVisibility(View.GONE);
        btnBack.setOnClickListener(v -> onBackPressed());

        // ----- CARD & TEXT REFERENCES -----
        cardMailSupport = findViewById(R.id.cardMailSupport);
        cardCallSupport = findViewById(R.id.cardCallSupport);

        supportEmail = findViewById(R.id.support_email);
        supportContact = findViewById(R.id.support_call);
//        salesContact = findViewById(R.id.salescontact);

        // ----- MAIL SUPPORT CLICK -----
        supportEmail.setOnClickListener(v -> {

            String email = supportEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Email not available", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + email));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Support Request");

            try {
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show();
            }
        });

        // ----- CALL SUPPORT CLICK -----
        supportContact.setOnClickListener(v -> {

            String number = supportContact.getText().toString().trim();

            if (number.isEmpty()) {
                Toast.makeText(this, "Contact number not available", Toast.LENGTH_SHORT).show();
                return;
            }

            // ---------- SCENARIO 1 : WITHOUT TIME RESTRICTION ----------
            // Uncomment this block if you want to allow calls anytime

///*
    Intent intent = new Intent(Intent.ACTION_DIAL);
    intent.setData(Uri.parse("tel:" + number));
    startActivity(intent);
//*/

            // ---------- SCENARIO 2 : WITH TIME RESTRICTION ----------

//            Calendar calendar = Calendar.getInstance();
//            int hour = calendar.get(Calendar.HOUR_OF_DAY);
//            int minute = calendar.get(Calendar.MINUTE);
//
//            int currentTime = hour * 60 + minute;
//
//            int startTime = (9 * 60) + 45;   // 9:45 AM
//            int endTime = (17 * 60);         // 5:00 PM
//
//            if (currentTime >= startTime && currentTime <= endTime) {
//
//                Intent intent = new Intent(Intent.ACTION_DIAL);
//                intent.setData(Uri.parse("tel:" + number));
//                startActivity(intent);
//
//            } else {
//
//                Toast.makeText(
//                        this,
//                        "Support is available between 9:45 AM to 5:00 PM",
//                        Toast.LENGTH_LONG
//                ).show();
//            }

        });

        // ----- SALES CONTACT CLICK -----
        // Not required from client end
//        salesContact.setOnClickListener(v -> {
//            String number = salesContact.getText().toString().trim();
//
//            if (number.isEmpty()) {
//                Toast.makeText(this, "Sales contact not available", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            Intent intent = new Intent(Intent.ACTION_DIAL);
//            intent.setData(Uri.parse("tel:" + number));
//            startActivity(intent);
//        });

    }
}