package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;

public class AttendanceStatusResponse {
    @SerializedName("status")
    public String status;

    @SerializedName("isPresent")
    public Boolean isPresent;       // True if Punched In

    @SerializedName("isPresentOut")
    public boolean isPresentOut;    // True if Punched Out

    @SerializedName("isAbleToPunch")
    public boolean isAbleToPunch;   // False if leave/restricted


    @SerializedName("isHoliday")
    public boolean isHoliday;
    @SerializedName("isBetweenTime")
    public boolean isBetweenTime;   // True if between 5 AM - 11 PM

    @SerializedName("presentTime")
    public String intime;           // "10:44:00"

    @SerializedName("ispresentouttime") // Note: using the key from your JSON snippet
    public String outtime;          // "11:30:00"

    // Getters
    public boolean isPresent() { return isPresent; }
    public boolean isPresentOut() { return isPresentOut; }
    public boolean isAbleToPunch() { return isAbleToPunch; }
    public boolean isHoliday() { return isHoliday; }
}
