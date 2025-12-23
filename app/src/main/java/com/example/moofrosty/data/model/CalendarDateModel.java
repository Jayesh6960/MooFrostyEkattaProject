package com.example.moofrosty.data.model;

public class CalendarDateModel {

    private String day; // e.g., "Mon"
    private String date; // e.g., "25"
    private boolean isSelected;

    public CalendarDateModel(String day, String date, boolean isSelected) {
        this.day = day;
        this.date = date;
        this.isSelected = isSelected;
    }

    public String getDay() { return day; }
    public String getDate() { return date; }
    public boolean isSelected() { return isSelected; }
    public void setSelected(boolean selected) { isSelected = selected; }
}
