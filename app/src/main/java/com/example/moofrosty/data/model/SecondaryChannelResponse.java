package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SecondaryChannelResponse {

    @SerializedName("status")
    private boolean status;

    @SerializedName("secondarychanneldata") // Matches JSON key
    private List<ChannelData> channelData;

    public boolean isStatus() { return status; }
    public List<ChannelData> getData() { return channelData; }

    public static class ChannelData {
        @SerializedName("secondarychannelId") // Matches JSON key
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
