package com.example.moofrosty;

import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
//Updated date 23-01-2026
// Can to include in the all the Required activity
//import com.example.moofrosty.ui.attendance.ToolbarHelper;
//ToolbarHelper.setupToolbar(this, "Support", true, false);
//Easy and the simple way to Update  the toolbar
public class ToolbarHelper {

    public static void setupToolbar(AppCompatActivity activity,
                                    String title,
                                    boolean showBack,
                                    boolean showMenu) {

        Toolbar toolbar = activity.findViewById(R.id.main_toolbar);
        activity.setSupportActionBar(toolbar);

        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(
                    ContextCompat.getColor(activity, R.color.Pink_color)
            );
        }

        // Custom views
        TextView tvTitle = activity.findViewById(R.id.tv_title);
        ImageView btnBack = activity.findViewById(R.id.btn_back);
        ImageView btnMenu = activity.findViewById(R.id.btn_menu);

        // Set title
        if (tvTitle != null) {
            tvTitle.setText(title);
        }

        // Back button
        if (btnBack != null) {
            if (showBack) {
                btnBack.setVisibility(View.VISIBLE);
                btnBack.setOnClickListener(v -> activity.finish());
            } else {
                btnBack.setVisibility(View.GONE);
            }
        }

        // Menu button
        if (btnMenu != null) {
            if (showMenu) {
                btnMenu.setVisibility(View.VISIBLE);
            } else {
                btnMenu.setVisibility(View.GONE);
            }
        }
    }
}
