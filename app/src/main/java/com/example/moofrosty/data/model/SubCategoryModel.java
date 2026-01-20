package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;

public class SubCategoryModel {
    @SerializedName("subcategoryId")
    public int subcategoryId;
    @SerializedName("categoryId")
    public int categoryId;
    @SerializedName("subcategoryTitle")
    public String subcategoryTitle;
    @SerializedName("subcategoryImage")
    public String subcategoryImage;
}
