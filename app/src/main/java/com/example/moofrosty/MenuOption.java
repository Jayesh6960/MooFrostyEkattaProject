package com.example.moofrosty;

public class MenuOption {
    int iconResId;
    String title;

    public MenuOption() {

    }

    public MenuOption(int iconResId, String title) {
        this.iconResId = iconResId;
        this.title = title;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getTitle() {
        return title;
    }
}