package com.example.moofrosty;

public class BrandModel {

    private int imageResId;
    private String name;

    public BrandModel(int imageResId, String name) {
        this.imageResId = imageResId;
        this.name = name;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getName() {
        return name;
    }
}
