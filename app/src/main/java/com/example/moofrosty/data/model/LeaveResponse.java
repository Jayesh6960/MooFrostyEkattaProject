package com.example.moofrosty.data.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LeaveResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<UserLeaveData> data;

    // HOLIDAYS (Added this)
    @SerializedName("holidays")
    private List<HolidayData> holidays;

    // ATTENDANCE (Added this)
    @SerializedName("userAttendance")
    private List<AttendanceData> userAttendance;

    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public List<UserLeaveData> getData() { return data; }
    public List<HolidayData> getHolidays() { return holidays; }
    public List<AttendanceData> getUserAttendance() { return userAttendance; }

    public static class UserLeaveData {
        @SerializedName("userleavesId")
        private int id;

        @SerializedName("leaveType")
        private String leaveType; // "1"=Medical, "2"=Loss of Pay, "3"=Casual

        @SerializedName("startDate")
        private String startDate; // YYYY-MM-DD

        @SerializedName("endDate")
        private String endDate;

        @SerializedName("leaveStatus")
        private int leaveStatus; // 1=Pending, 2=Approved, 3=Rejected



        public String getLeaveType() { return leaveType; }
        public String getStartDate() { return startDate; }
        public String getEndDate() { return endDate; }

        // Added Getter for Status
        public int getLeaveStatus() { return leaveStatus; }
    }

    // --- Inner Class: Holiday ---
    public static class HolidayData {
        @SerializedName("holidayId")
        public int id;

        @SerializedName("date")
        public String date; // "2026-01-16"

        @SerializedName("title")
        public String title;

        public String getDate() { return date; }
    }

    // --- Inner Class: Attendance ---
    public static class AttendanceData {
        @SerializedName("attendanceId")
        public int id;

        @SerializedName("attendanceDate")
        public String date; // "2026-01-10"

        @SerializedName("attendanceTime")
        public String time;

        public String getDate() { return date;
        }
    }
}
