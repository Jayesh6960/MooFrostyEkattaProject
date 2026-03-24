package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("token")
    private String token;

    @SerializedName("token_type")
    private String tokenType;


    @SerializedName("user")
    private User user;
    @SerializedName("data")
    private User data;

    // --- Getters ---
    public boolean isStatus() { return status; }
    public String getMessage() { return message; }
    public String getToken() { return token; }
    public User getUser() { return user; }
    public User getData() {
        return data;
    }



    // --- Inner Class: User ---
    public static class User {
        @SerializedName("id")
        private int id;

        @SerializedName("beatId")
        private String beatId; // JSON shows this as a String "3"

        @SerializedName("firstName")
        private String firstName;

        @SerializedName("middleName")
        private String middleName;

        @SerializedName("lastName")
        private String lastName;

        @SerializedName("email")
        private String email;

        @SerializedName("mobileNumber")
        private String mobileNumber;

        @SerializedName("address")
        private String address;

        @SerializedName("userRole")
        private int userRole;

        @SerializedName("beat")
        private Beat beat;
        @SerializedName("status")
        private String status;





        @SerializedName("bank_detail")
        private BankDetail bankDetail;

        // --- Getters ---
        public int getId() { return id; }
        public String getBeatId() { return beatId; }
        public String getFirstName() { return firstName; }
        public String getMiddleName() { return middleName; }
        public String getLastName() { return lastName; }
        public String getEmail() { return email; }
        public String getMobileNumber() { return mobileNumber; }
        public Beat getBeat() { return beat; }
        public String getStatus() {
            return status;
        }


    }

    // --- Inner Class: Beat ---
    public static class Beat {
        @SerializedName("beatId")
        private int beatId; // JSON shows this as int 3

        @SerializedName("beatNameFrom")
        private String beatNameFrom;

        @SerializedName("beatNameTo")
        private String beatNameTo;

        @SerializedName("city")
        private String city;

        @SerializedName("district")
        private String district;

        // --- Getters ---
        public int getBeatId() { return beatId; }
        public String getBeatNameFrom() { return beatNameFrom; }
        public String getBeatNameTo() { return beatNameTo; }
    }

    // --- Inner Class: BankDetail ---
    public static class BankDetail {
        @SerializedName("bankId")
        private int bankId;

        @SerializedName("bankname")
        private String bankName;

        @SerializedName("accountNumber")
        private String accountNumber;

        @SerializedName("IFSCCode")
        private String ifscCode;
    }

    @SerializedName("attendanceStatus")
    private AttendanceStatus attendanceStatus;

    public AttendanceStatus getAttendanceStatus() { return attendanceStatus; }

    public static class AttendanceStatus {
        @SerializedName("isPresent")
        private boolean isPresent;

        @SerializedName("isPresentOut")
        private boolean isPresentOut;



        public boolean isPresent() { return isPresent; }
        public boolean isPresentOut() { return isPresentOut; }
    }


}
