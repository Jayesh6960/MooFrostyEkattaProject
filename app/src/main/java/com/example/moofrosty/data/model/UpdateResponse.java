package com.example.moofrosty.data.model;

public class UpdateResponse {

    private int latestVersionCode;
    private String latestVersionName;
    private String apkUrl;
    private boolean forceUpdate;
    private String updateMessage;

    public int getLatestVersionCode() {
        return latestVersionCode;
    }

    public String getLatestVersionName() {
        return latestVersionName;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public boolean isForceUpdate() {
        return forceUpdate;
    }

    public String getUpdateMessage() {
        return updateMessage;
    }
}