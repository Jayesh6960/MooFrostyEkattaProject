package com.example.moofrosty.ui.attendance;

public class AttendanceMenuModel {
    private String title;
    private int iconResId;

    public AttendanceMenuModel(String title, int iconResId) {
        this.title = title;
        this.iconResId = iconResId;
    }

    public String getTitle() { return title; }
    public int getIconResId() { return iconResId; }
}