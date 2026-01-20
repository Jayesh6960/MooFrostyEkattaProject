package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryResponse {

    @SerializedName("status")
    public boolean status; // or int 200 depending on api
    @SerializedName("categoryData") // Check your API response Key
    public List<CategoryModel> data;
}
