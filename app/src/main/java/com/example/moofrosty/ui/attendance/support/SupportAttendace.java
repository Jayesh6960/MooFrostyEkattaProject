package com.example.moofrosty.ui.attendance.support;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.moofrosty.R;
import com.example.moofrosty.ToolbarHelper;
//Code Updatee date 23-01-2023
//Toolbarhelper include
public class SupportAttendace extends AppCompatActivity {

    private TextView supportEmail, supportContact, salesContact;
    private CardView cardMailSupport, cardCallSupport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_attendace);

        // ----- EDGE TO EDGE (STATUS BAR PADDING) -----
        View main = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, 0);
            return insets;
        });

        // ----- COMMON TOOLBAR SETUP (FROM HELPER) -----
        ToolbarHelper.setupToolbar(this, "Support", true, false);

        // ----- CARD & TEXT REFERENCES -----
        cardMailSupport = findViewById(R.id.cardMailSupport);
        cardCallSupport = findViewById(R.id.cardCallSupport);

        supportEmail = findViewById(R.id.support_email);
        supportContact = findViewById(R.id.support_call);
        salesContact = findViewById(R.id.salescontact);

        // ----- MAIL SUPPORT CLICK -----
        cardMailSupport.setOnClickListener(v -> {
            String email = supportEmail.getText().toString();

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
        cardCallSupport.setOnClickListener(v -> {
            String number = supportContact.getText().toString();

            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + number));
            startActivity(intent);
        });

        // ----- SALES CONTACT CLICK -----
        salesContact.setOnClickListener(v -> {
            String number = salesContact.getText().toString();

            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + number));
            startActivity(intent);
        });
    }
}
