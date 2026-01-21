package com.example.moofrosty.ui.attendance.support;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moofrosty.R;

public class SupportAttendace extends AppCompatActivity {

    private TextView supportEmail, salesmancontact, supportcontact;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_attendace);

        // Initialize views
        supportEmail = findViewById(R.id.supportEmail);
        salesmancontact = findViewById(R.id.salesmancontact);
        supportcontact = findViewById(R.id.supportcontact);
        btnBack = findViewById(R.id.btn_back);

        // Back button
        btnBack.setOnClickListener(v -> onBackPressed());

        // 📧 Email click
        supportEmail.setOnClickListener(v -> openEmail());

        // 📞 Call TSF / Salesman
        salesmancontact.setOnClickListener(v ->
                openDialer(salesmancontact.getText().toString())
        );

        // 📞 Call Helpline
        supportcontact.setOnClickListener(v ->
                openDialer(supportcontact.getText().toString())
        );
    }

    /**
     * Open Email App
     */
    private void openEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + supportEmail.getText().toString()));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Support Request");
        startActivity(Intent.createChooser(intent, "Send Email"));
    }

    /**
     * Open Dialer
     */
    private void openDialer(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }
}
