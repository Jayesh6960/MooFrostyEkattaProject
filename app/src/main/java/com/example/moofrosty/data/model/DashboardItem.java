package com.example.moofrosty.data.model;

import java.io.Serializable;

public class DashboardItem implements Serializable {

    private String title;
    private int progress;
    private String achievementText;

    // Fields for Detail View
    private int target;
    private int achieved;

    // Constructor for Main Dashboard Cards
    public DashboardItem(String title, int progress, String achievementText) {
        this.title = title;
        this.progress = progress;
        this.achievementText = achievementText;
        this.target = 0;
        this.achieved = 0;
    }

    // Full Constructor for Repository Data
    public DashboardItem(String title, int progress, String achievementText, int target, int achieved) {
        this.title = title;
        this.progress = progress;
        this.achievementText = achievementText;
        this.target = target;
        this.achieved = achieved;
    }

    public String getTitle() { return title; }
    public int getProgress() { return progress; }
    public String getAchievementText() { return achievementText; }
    public int getTarget() { return target; }
    public int getAchieved() { return achieved; }
}
