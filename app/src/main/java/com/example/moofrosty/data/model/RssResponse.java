package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RssResponse {

    @SerializedName("status")
    private boolean status;

    @SerializedName("rssidentifierdata") // Matches JSON key
    private List<RssData> rssData;

    public boolean isStatus() { return status; }
    public List<RssData> getData() { return rssData; }

    public static class RssData {
        @SerializedName("rsssidentifierId") // Matches JSON key (3 's')
        private int id;

        @SerializedName("title")
        private String title;

        public int getId() { return id; }
        public String getTitle() { return title; }

        // Important: This controls what text shows in the Spinner
        @Override
        public String toString() {
            return title;
        }
    }
}
