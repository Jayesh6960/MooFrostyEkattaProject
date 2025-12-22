package com.example.moofrosty.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.moofrosty.R;
import com.example.moofrosty.ui.attendance.AttendanceMenuModel;

import java.util.ArrayList;
import java.util.List;

public class AttendanceRepository {

    public void fetchMenuItems(MutableLiveData<List<AttendanceMenuModel>> data) {
        List<AttendanceMenuModel> items = new ArrayList<>();

        // Add your 6 items here. Replace 0 with R.drawable.your_icon
        // Since I don't have your specific drawables, I'm using standard ones as placeholders.
        // PLEASE REPLACE WITH YOUR ACTUAL DRAWABLE IDs
        items.add(new AttendanceMenuModel("Attendance", R.drawable.attendanceiconattendance));
        items.add(new AttendanceMenuModel("Leave", R.drawable.calendericonattendance));
        items.add(new AttendanceMenuModel("Support", R.drawable.supporticonattendance));
        items.add(new AttendanceMenuModel("Profile", R.drawable.profileiconattendance));

        data.setValue(items);
    }
}
