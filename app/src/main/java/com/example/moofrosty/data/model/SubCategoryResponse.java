package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubCategoryResponse {

    @SerializedName("status")
    public boolean status;
    @SerializedName("subcategoryData")
    public List<SubCategoryModel> subcategoryData;
}
