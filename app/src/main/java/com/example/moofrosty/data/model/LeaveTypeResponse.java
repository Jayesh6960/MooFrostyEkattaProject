package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LeaveTypeResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<LeaveType> data;

    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public List<LeaveType> getData() { return data; }

    public static class LeaveType {

        @SerializedName("leavesId")
        private int leavesId;

        @SerializedName("leaveType")
        private String leaveType;

        public int getLeavesId() {
            return leavesId;
        }

        public String getLeaveType() {
            return leaveType;
        }
    }
}
