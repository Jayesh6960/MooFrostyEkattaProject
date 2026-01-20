package com.example.moofrosty.ui.attendance;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moofrosty.R;
import com.example.moofrosty.ui.attendance.attendancecalender.AttendanceCalendarActivity;
import com.example.moofrosty.ui.attendance.leave.LeaveActivity;
import com.example.moofrosty.ui.attendance.profile.ProfileActivity;
import com.example.moofrosty.ui.attendance.support.SupportAttendace;

import java.util.List;

public class AttendanceMenuAdapter extends RecyclerView.Adapter<AttendanceMenuAdapter.ViewHolder> {

    private Context context;
    private List<AttendanceMenuModel> items;

    public AttendanceMenuAdapter(Context context, List<AttendanceMenuModel> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AttendanceMenuModel item = items.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.imgIcon.setImageResource(item.getIconResId());

        // Icon Tint (Optional, to make them blue)
       // holder.imgIcon.setColorFilter(context.getResources().getColor(R.color.colorPrimary));

//        holder.itemView.setOnClickListener(v -> {
//            // Handle clicks to open different activities here
//            Toast.makeText(context, "Clicked: " + item.getTitle(), Toast.LENGTH_SHORT).show();
//            // Example:
//            // if (item.getTitle().equals("Attendance")) {
//            //     context.startActivity(new Intent(context, MarkAttendanceActivity.class));
//            // }
//        });

        holder.itemView.setOnClickListener(v -> {
            if (item.getTitle().equals("Leave")) {
                // If the "Leave" icon is clicked, open LeaveActivity
                Intent intent = new Intent(context, LeaveActivity.class);
                context.startActivity(intent);
            } else if (item.getTitle().equals("Attendance")) {
                // --- NEW CODE: Open Calendar Activity ---
                Intent intent = new Intent(context, AttendanceCalendarActivity.class);
                context.startActivity(intent);
            }else if (item.getTitle().equals("Support")) {
                // --- NEW CODE: Open Calendar Activity ---
                Intent intent = new Intent(context, SupportAttendace.class);
                context.startActivity(intent);
            }
            else if (item.getTitle().equals("Profile")) {
                // --- NEW CODE: Open Calendar Activity ---
                Intent intent = new Intent(context, ProfileActivity.class);
                context.startActivity(intent);
            }else {
                // For other icons (Attendance, Regularization, etc.), show a toast for now
                Toast.makeText(context, "Clicked: " + item.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView imgIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_menu_title);
            imgIcon = itemView.findViewById(R.id.img_menu_icon);
        }
    }
}
