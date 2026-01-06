package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BeatResponse {
//    @SerializedName("status")
//    private int status;
//    @SerializedName("bdodata")
//    private List<BeatData> beatData;
//
//    public List<BeatData> getBeatData() { return beatData; }
//
//    public static class BeatData {
//        @SerializedName("beatId") private int id;
//        @SerializedName("beatNameFrom") private String nameFrom;
//        @SerializedName("beatNameTo") private String nameTo;
//
//        public int getId() { return id; }
//        public String getName() { return nameFrom + " - " + nameTo; }
//        @Override public String toString() { return getName(); }
//    }

    @SerializedName("status")
    private boolean status;

    @SerializedName("bdodata")   // 🔴 EXACT JSON KEY
    private List<BeatData> bdodata;

    public boolean isStatus() {
        return status;
    }

    public List<BeatData> getBeatData() {
        return bdodata;
    }

    // ================= INNER MODEL =================
    public static class BeatData {

        @SerializedName("beatId")
        private int id;

        @SerializedName("beatNameFrom")
        private String from;

        @SerializedName("beatNameTo")
        private String to;

        public int getId() {
            return id;
        }

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }

        // 🔴 REQUIRED FOR DROPDOWN TEXT
        @Override
        public String toString() {
            return from + " - " + to;
        }
    }
}
