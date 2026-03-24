package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserDetailResponse {
    // Note: JSON shows "status": "success", so we use String here

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private Data data;

    public boolean isSuccess() {
        return "success".equalsIgnoreCase(status);
    }

    public Data getData() {
        return data;
    }

    // ================= DATA =================
    public static class Data {

        @SerializedName("id")
        private int id;

        @SerializedName("firstName")
        private String firstName;

        @SerializedName("middleName")
        private String middleName;

        @SerializedName("lastName")
        private String lastName;

        @SerializedName("iteamsCode")
        private String iteamsCode;

        @SerializedName("address")
        private String address;

        @SerializedName("email")
        private String email;

        @SerializedName("dateofBirth")
        private String dateofBirth;

        @SerializedName("gender")
        private String gender;

        @SerializedName("education")
        private String education;

        @SerializedName("mobileNumber")
        private String mobileNumber;

        @SerializedName("dateofJoining")
        private String dateofJoining;

        @SerializedName("experienceYears")
        private String experienceYears;

        @SerializedName("experienceMonths")
        private String experienceMonths;

        @SerializedName("pastEmployer")
        private String pastEmployer;

        @SerializedName("beats")
        private List<Beat> beats;
        @SerializedName("status")
        private String status;

        @SerializedName("bank_detail")
        private BankDetail bankDetail;

        // -------- Getters (REQUIRED) --------
        public String getFirstName() { return firstName; }
        public String getMiddleName() { return middleName; }
        public String getLastName() { return lastName; }
        public String getIteamsCode() { return iteamsCode; }
        public String getAddress() { return address; }
        public String getEmail() { return email; }
        public String getDateofBirth() { return dateofBirth; }
        public String getGender() { return gender; }
        public String getEducation() { return education; }
        public String getMobileNumber() { return mobileNumber; }
        public String getDateofJoining() { return dateofJoining; }
        public String getExperienceYears() { return experienceYears; }
        public String getExperienceMonths() { return experienceMonths; }
        public String getPastEmployer() { return pastEmployer; }
        public String getstatus() { return status; }

        public List<Beat> getBeats() { return beats; }
        public BankDetail getBankDetail() { return bankDetail; }

        // ================= BEAT =================
        public static class Beat {

            @SerializedName("beatId")
            private int beatId;

            @SerializedName("beatNameFrom")
            private String beatNameFrom;

            @SerializedName("beatNameTo")
            private String beatNameTo;

            // [HIGHLIGHT] Added beat_range
            @SerializedName("beat_range")
            private String beatRange;

            public int getBeatId() {
                return beatId;
            }

            public String getBeatRange() {
                return beatRange;
            }

            public String getFullBeatName() {
                if (beatRange != null && !beatRange.isEmpty()) {
                    return beatRange;
                }
                return (beatNameFrom != null ? beatNameFrom : "") + " - " + (beatNameTo != null ? beatNameTo : "");
            }
        }

        // ================= BANK =================
        public static class BankDetail {

            @SerializedName("bankname")
            private String bankname;

            @SerializedName("branchName")
            private String branchName;

            @SerializedName("accountNumber")
            private String accountNumber;

            @SerializedName("confirmAccountNumber")
            private String confirmAccountNumber;

            @SerializedName("IFSCCode")
            private String IFSCCode;

            @SerializedName("accountType")
            private String accountType;

            public String getBankname() { return bankname; }
            public String getBranchName() { return branchName; }
            public String getAccountNumber() { return accountNumber; }
            public String getConfirmAccountNumber() { return confirmAccountNumber; }
            public String getIFSCCode() { return IFSCCode; }
            public String getAccountType() { return accountType; }
        }
    }
}


//    public static class Beat {
//        @SerializedName("beatId")
//        private int beatId;
//
//        @SerializedName("beatNameFrom")
//        private String beatNameFrom;
//
//        @SerializedName("beatNameTo")
//        private String beatNameTo;
//
//        public int getBeatId() { return beatId; }
//
//        // Helper to combine names
//        public String getFullBeatName() {
//            String from = beatNameFrom != null ? beatNameFrom : "";
//            String to = beatNameTo != null ? beatNameTo : "";
//            if (!from.isEmpty() && !to.isEmpty()) return from + " - " + to;
//            return from + to;
//        }
//    }