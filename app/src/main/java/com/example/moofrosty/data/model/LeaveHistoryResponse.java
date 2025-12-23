package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LeaveHistoryResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<LeaveItem> data;

    @SerializedName("counters")
    private List<LeaveCounter> counters;

    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public List<LeaveItem> getData() { return data; }
    public List<LeaveCounter> getCounters() { return counters; }

    // Inner Class: Individual Leave Item
    public static class LeaveItem {
        @SerializedName("userleavesId")
        private int id;

        @SerializedName("leaveType")
        private String leaveType; // "1"=Casual, "2"=Medical

        @SerializedName("startDate")
        private String startDate;

        @SerializedName("endDate")
        private String endDate;

        @SerializedName("reason")
        private String reason;

        @SerializedName("leaveStatus")
        private int leaveStatus; // 1=Pending, 2=Approved, 3=Rejected (Assumption)

        public int getId() { return id; }
        public String getLeaveType() { return leaveType; }
        public String getStartDate() { return startDate; }
        public String getEndDate() { return endDate; }
        public String getReason() { return reason; }
        public int getLeaveStatus() { return leaveStatus; }
    }

    // Inner Class: Counter (Optional usage)
    public static class LeaveCounter {
        @SerializedName("leaveType")
        private String leaveType;
        @SerializedName("remainingLeaves")
        private int remainingLeaves;

        public String getLeaveType() { return leaveType; }
        public int getRemainingLeaves() { return remainingLeaves; }
    }
}
