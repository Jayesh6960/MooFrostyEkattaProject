package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;

public class CategoryModel {
    @SerializedName("categoryId")
    public int categoryId;
    @SerializedName("categoryTitle")
    public String categoryTitle;
    @SerializedName("categoryImage")
    public String categoryImage;
}
