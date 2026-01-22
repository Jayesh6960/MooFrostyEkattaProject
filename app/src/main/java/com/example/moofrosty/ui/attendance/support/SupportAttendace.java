package com.example.moofrosty.ui.attendance.support;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.moofrosty.R;
//Last Updated date 22-01-2026
public class SupportAttendace extends AppCompatActivity {
    TextView  supportEmail,supportContact,slaescontact;//best prac to declare the variable
    CardView cardMailSupport,cardCallSupport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_support_attendace);

        // Handle status bar padding (Edge to Edge)
        View main = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, 0);
            return insets;
        });

        // Set STATUS BAR COLOR same as toolbar (Pink_color)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.Pink_color));
        }

        // Toolbar Back Button
        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setColorFilter(ContextCompat.getColor(this, R.color.white));
        btnBack.setOnClickListener(v -> finish());

        // Toolbar Title Color
        TextView title = findViewById(R.id.tv_toolbar_title);
        title.setTextColor(ContextCompat.getColor(this, R.color.white));

        // Card references
        CardView cardMailSupport = findViewById(R.id.cardMailSupport);
        CardView cardCallSupport = findViewById(R.id.cardCallSupport);

        // Text references
         TextView supportEmail = findViewById(R.id.support_email);
         TextView supportContact = findViewById(R.id.support_call);
         TextView  slaescontact=findViewById(R.id.salescontact);

        // MAIL SUPPORT CLICK
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

        // CALL SUPPORT CLICK
        cardCallSupport.setOnClickListener(v -> {
            String number = supportContact.getText().toString();

            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + number));
            startActivity(intent);
        });
        slaescontact.setOnClickListener(v -> {
            String number = slaescontact.getText().toString();

            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + number));
            startActivity(intent);
        });
    }
}
