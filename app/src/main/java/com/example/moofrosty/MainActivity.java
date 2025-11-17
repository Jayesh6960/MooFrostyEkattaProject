package com.example.moofrosty;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.selector_nav_item));//used to  make the Responvise to all the device
        WindowInsetsControllerCompat insetsController =
                new WindowInsetsControllerCompat(window, window.getDecorView());
        insetsController.setAppearanceLightStatusBars(false);
        //   WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // 🟢 Add padding so content stays visible (not overlapped)
            v.setPadding(
                    systemBars.left,
                    0,
                    systemBars.right,
                    systemBars.bottom
            );
            return insets;
        });
        Button openSheetBtn = findViewById(R.id.btn);
        openSheetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show BottomSheet
                BottomSheetSubCategeries bottomSheet = new BottomSheetSubCategeries();
                bottomSheet.show(getSupportFragmentManager(), "BottomSheetWithTabs");
            }
        });
    }
}