package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;

public class ApplyLeaveRequest {
    @SerializedName("leaveType")
    private String leaveType; // "1", "2", "3"

    @SerializedName("startDate")
    private String startDate; // YYYY-MM-DD

    @SerializedName("endDate")
    private String endDate; // YYYY-MM-DD

    @SerializedName("reason")
    private String reason;

    public ApplyLeaveRequest(String leaveType, String startDate, String endDate, String reason) {
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
    }
}
