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

    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public List<UserLeaveData> getData() { return data; }

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
}
